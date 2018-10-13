package com.maryato.dimas.example.batch;

import com.maryato.dimas.example.batch.tasks.DatabaseTaskReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchTaskletOriented {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobRepository jobRepository;

    @Bean
    public Step taskletStepReader(DatabaseTaskReader reader) {
        return this.stepBuilderFactory
                .get("taskletStepReader")
                .tasklet(reader)
                .repository(jobRepository)
                .build();
    }

    @Bean
    public Job taskletJob(
            @Qualifier("taskletStepReader") Step databaseTaskletStop,
            @Qualifier("csvToExcelAndJdbcStep") Step csvToExecAndJdbcStep) {
        return jobBuilderFactory.get("taskletJob")
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .repository(jobRepository)
                .start(csvToExecAndJdbcStep)
                .next(databaseTaskletStop)
                .build();
    }

}
