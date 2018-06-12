package com.maryato.dimas.example.config;

import com.maryato.dimas.example.batch.DataPendudukItemProccessor;
import com.maryato.dimas.example.batch.JobCompletionNotificationListener;
import com.maryato.dimas.example.consume.FileDataPenduduk;
import com.maryato.dimas.example.entity.DataPenduduk;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataPendudukItemProccessor proccessor;

    @Bean
    public FlatFileItemReader<FileDataPenduduk> fileReader() throws Exception {
        FlatFileItemReader<FileDataPenduduk> fileItemReader = new FlatFileItemReader<>();
        fileItemReader.setName("dataPenduduk");
        fileItemReader.setResource(new ClassPathResource("/penduduk.csv"));

        BeanWrapperFieldSetMapper<FileDataPenduduk> fieldSetMapper =
                new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(FileDataPenduduk.class);
        fieldSetMapper.afterPropertiesSet();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"id", "name", "birtDate", "address"});

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        fileItemReader.setLineMapper(lineMapper);
        return fileItemReader;
    }


    @Bean
    public JdbcBatchItemWriter<DataPenduduk> jdbcBatchItemWriterDataPenduduk(DataSource dataSource) {
        JdbcBatchItemWriter<DataPenduduk> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
        jdbcBatchItemWriter.setDataSource(dataSource);
        jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        jdbcBatchItemWriter.setSql("update data_penduduk set nama_identitias = :namaIdentitas, tanggal_lahir = :tanggalLahir where nik = :nik");
        jdbcBatchItemWriter.afterPropertiesSet();
        return jdbcBatchItemWriter;
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<DataPenduduk> jdbcWriter, FlatFileItemReader<FileDataPenduduk> fileReader) {
        return stepBuilderFactory.get("step1")
                .<FileDataPenduduk, DataPenduduk>chunk(10)
                .reader(fileReader)
                .processor(proccessor)
                .writer(jdbcWriter)
                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

}
