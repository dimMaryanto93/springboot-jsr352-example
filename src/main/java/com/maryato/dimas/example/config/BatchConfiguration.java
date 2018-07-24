package com.maryato.dimas.example.config;

import com.maryato.dimas.example.config.items.DataPendudukExcelItemWriter;
import com.maryato.dimas.example.config.items.DataPendudukExcelItemWriterListener;
import com.maryato.dimas.example.config.items.TransformProcessor;
import com.maryato.dimas.example.models.Penduduk;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public JobRepository jobRepository(
            @Qualifier("dataSource") DataSource dataSource,
            PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean repository = new JobRepositoryFactoryBean();
//        repository.setDatabaseType();
        repository.setDataSource(dataSource);
        repository.setTransactionManager(transactionManager);
        return repository.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

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

//    /**
//     * use this if no need @AfterStep or @BeforeStep because not working
//     *
//     * @param excel
//     * @param jdbc
//     * @return
//     * @throws Exception
//     */
//    @Bean
//    public CompositeItemWriter<Penduduk> groupStepJdbcAndExcel(
//            DataPendudukExcelItemWriter excel,
//            @Qualifier("dataPendudukJdbcWriter") JdbcBatchItemWriter<Penduduk> jdbc) throws Exception {
//        CompositeItemWriter compose = new CompositeItemWriter();
//        compose.setDelegates(Arrays.asList(jdbc, excel));
//        compose.setIgnoreItemStream(false);
//        compose.afterPropertiesSet();
//        return compose;
//    }

    @Bean(name = "csvToExcelAndJdbcStep")
    public Step csvToExcelAndJdbcStep(
            @Qualifier("dataCsvItemReader") FlatFileItemReader<Penduduk> reader,
            TransformProcessor processor,
            DataPendudukExcelItemWriterListener writer) {
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


}
