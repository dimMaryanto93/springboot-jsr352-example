package com.maryato.dimas.example.executor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BatchExecuteable implements CommandLineRunner {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("jobExcelToExcel")
    private Job excelToExcelJob;

    @Autowired
    @Qualifier("jobCsvToExcel")
    private Job csvToExcelJob;

    @Override
    public void run(String... strings) throws Exception {
        JobParametersBuilder excelToExcelJobParams = new JobParametersBuilder();
        excelToExcelJobParams.addString("fileName", "/data/penduduk.xlsx");
        excelToExcelJobParams.addLong("time", System.currentTimeMillis());
        jobLauncher.run(excelToExcelJob, excelToExcelJobParams.toJobParameters());

        JobParametersBuilder csvToExcelJobParams = new JobParametersBuilder();
        csvToExcelJobParams.addLong("time", System.currentTimeMillis());
        csvToExcelJobParams.addString("fileName", "/data/penduduk.csv");
        jobLauncher.run(csvToExcelJob, csvToExcelJobParams.toJobParameters());
    }
}
