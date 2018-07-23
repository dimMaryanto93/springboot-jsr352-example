package com.maryato.dimas.example.config;

import com.maryato.dimas.example.config.items.TransformProcessor;
import com.maryato.dimas.example.models.Penduduk;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step excelToJdbcAndExcelFileStep(
            PoiItemReader<Penduduk> reader,
            TransformProcessor processor) {
        return stepBuilderFactory.get("excelToJdbcAndExcelFileStep")
                .<Penduduk, Penduduk>chunk(10)
                .reader(reader)
                .processor(processor)
//                .writer(jdbcWriter)
                .build();
    }

    @Bean
    public Job excelToJdbcAndExcelFileJob(Step excelToJdbcAndExcelFile) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(excelToJdbcAndExcelFile)
                .end()
                .build();
    }

}
