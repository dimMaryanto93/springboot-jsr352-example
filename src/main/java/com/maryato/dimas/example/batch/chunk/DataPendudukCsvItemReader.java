package com.maryato.dimas.example.batch.chunk;

import com.maryato.dimas.example.models.Penduduk;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class DataPendudukCsvItemReader {

    @Bean(name = "dataCsvItemReader")
    @StepScope
    public FlatFileItemReader<Penduduk> fileReader(
            @Value("#{jobParameters['sourceCsvResource']}") String fileName) throws Exception {
        FlatFileItemReader<Penduduk> fileItemReader = new FlatFileItemReader<>();
        fileItemReader.setName("dataPenduduk");
        fileItemReader.setResource(new ClassPathResource(fileName));

        BeanWrapperFieldSetMapper<Penduduk> fieldSetMapper =
                new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Penduduk.class);
        fieldSetMapper.afterPropertiesSet();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"nik", "namaLengkap", "tanggalLahir"});

        DefaultLineMapper lineMapper = new DefaultLineMapper();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        fileItemReader.setLineMapper(lineMapper);
        return fileItemReader;
    }
}
