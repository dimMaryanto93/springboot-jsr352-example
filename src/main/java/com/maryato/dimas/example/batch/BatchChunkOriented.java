package com.maryato.dimas.example.batch;

import com.maryato.dimas.example.batch.chunk.DataPendudukExcelAndJdbcItemWriter;
import com.maryato.dimas.example.batch.chunk.DataPendudukExcelItemWriter;
import com.maryato.dimas.example.batch.chunk.TransformProcessor;
import com.maryato.dimas.example.models.Penduduk;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.jms.JmsItemReader;
import org.springframework.batch.item.jms.JmsItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class BatchChunkOriented {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    @Bean(name = "stepExcelToExcel")
    public Step excelToExcelFileStep(
            PoiItemReader<Penduduk> reader,
            TransformProcessor processor,
            DataPendudukExcelItemWriter writer) {
        return stepBuilderFactory.get("excelToExcelFileStep")
                .<Penduduk, Penduduk>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean(name = "stepCsvToExcel")
    public Step csvToExcelStep(
            @Qualifier("dataCsvItemReader") FlatFileItemReader<Penduduk> reader,
            TransformProcessor processor,
            DataPendudukExcelItemWriter writer) {
        return stepBuilderFactory.get("csvToExcelStep")
                .<Penduduk, Penduduk>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean(name = "jobExcelToExcel")
    public Job excelToExcelJob(
            @Qualifier("stepExcelToExcel") Step job) {
        return jobBuilderFactory.get("excelToExcelJob")
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .flow(job)
                .end()
                .build();
    }

    @Bean(name = "jobCsvToExcel")
    public Job csvToExcelJob(
            @Qualifier("stepCsvToExcel") Step job) {
        return jobBuilderFactory.get("csvToExcelJob")
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .flow(job)
                .end()
                .build();
    }

    @Bean(name = "csvToExcelAndJdbcStep")
    public Step csvToExcelAndJdbcStep(
            @Qualifier("dataCsvItemReader") FlatFileItemReader<Penduduk> reader,
            TransformProcessor processor,
            DataPendudukExcelAndJdbcItemWriter writer) {
        return stepBuilderFactory.get("csvToExcelStep")
                .<Penduduk, Penduduk>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean(name = "csvToExcelAndJdbcJob")
    public Job csvToExcelAndJdbcJob(
            @Qualifier("csvToExcelAndJdbcStep") Step job) {
        return jobBuilderFactory.get("csvToExcelAndJdbcJob")
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .flow(job)
                .end()
                .build();
    }

    @Bean
    public Step jmsToJdbcStep(
            JmsItemReader<Penduduk> reader,
            TransformProcessor processor,
            @Qualifier("groupJmsWriter") CompositeItemWriter<Penduduk> groupWriter) {
        return stepBuilderFactory.get("jmsToJdbcStep")
                .<Penduduk, Penduduk>chunk(1)
                .reader(reader)
                .processor(processor)
                .writer(groupWriter)
                .build();
    }

    @Bean(name = "jmsToJdbcJob")
    public Job jmsToJdbcJob(
            @Qualifier("jmsToJdbcStep") Step job) {
        return jobBuilderFactory.get("jmsToJdbcJob")
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .flow(job)
                .end()
                .build();
    }

    @Bean
    public CompositeItemWriter<Penduduk> groupJmsWriter(
            JmsItemWriter<Penduduk> jmsWriter,
            JdbcBatchItemWriter<Penduduk> jdbcWriter) throws Exception {
        CompositeItemWriter<Penduduk> groupWriters = new CompositeItemWriter<>();
        groupWriters.setIgnoreItemStream(true);
        groupWriters.setDelegates(Arrays.asList(jmsWriter, jdbcWriter));
        groupWriters.afterPropertiesSet();
        return groupWriters;
    }
}
