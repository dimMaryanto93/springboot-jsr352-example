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
    @Qualifier("excelToJdbcAndExcelFileJob")
    private Job excelToExcelJob;

    @Override
    public void run(String... strings) throws Exception {
        JobParametersBuilder params = new JobParametersBuilder();
        params.addString("fileName", "/data/penduduk.xlsx");
        params.addLong("time", System.currentTimeMillis());
        jobLauncher.run(excelToExcelJob, params.toJobParameters());
    }
}
