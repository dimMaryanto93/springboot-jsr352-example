package com.maryato.dimas.example.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class BatchTaskletExecution implements CommandLineRunner {

    private JobParametersBuilder params;

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    @Qualifier("taskletJob")
    private Job taskletJob;

    @Override
    public void run(String... strings) throws Exception {
//        this.params = new JobParametersBuilder();
//        params.addLong("time", System.currentTimeMillis());
//        params.addString("sourceCsvResource", "/data/penduduk.csv");
//        List<String> values = Arrays.asList("1234", "1235", "1236");
//        JobExecution run = jobLauncher.run(taskletJob, params.toJobParameters());
//
//        while (true) {
//            if (!run.getExitStatus().getExitCode().equals("UNKNOWN")) {
//                break;
//            }
//        }
//
//        log.info("job finished!");
    }
}
