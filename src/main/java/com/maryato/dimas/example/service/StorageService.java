package com.maryato.dimas.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class StorageService {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    @Qualifier("jobExcelToExcel")
    private Job excelToExcelJob;


    public String storeFile(MultipartFile file) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobRestartException, JobParametersInvalidException {
        String path = new StringBuilder(System.getProperty("user.home"))
                .append(File.separator).append("Documents")
                .append(File.separator).append("itime").toString();
        String fileName = new StringBuilder()
                .append(file.getName()).append("-").append(new SimpleDateFormat("yyyy-MM-dd-HH-MM-ss").format(new Date()))
                .append(".xlsx").toString();
        log.info("filename replacement: {}, new path : {}", fileName, path);

        File dir = new File(path);
        if (!dir.exists()) dir.mkdirs();

        if (!file.isEmpty()) {
            Path uploadPath = Paths.get(path, File.separator, fileName);
            Files.write(uploadPath, file.getBytes(), StandardOpenOption.CREATE_NEW);

            JobParametersBuilder excelToExcelJobParams = new JobParametersBuilder();
            excelToExcelJobParams.addString("sourceExcelPath",
                    new StringBuilder(path)
                            .append(File.separator)
                            .append(fileName).toString()
            );
            excelToExcelJobParams.addLong("time", System.currentTimeMillis());
            jobLauncher.run(excelToExcelJob, excelToExcelJobParams.toJobParameters());
        }
        return new StringBuilder(path).append(File.separator).append(fileName).toString();
    }
}
