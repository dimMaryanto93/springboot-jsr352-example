package com.maryato.dimas.example.batch.chunk;

import com.maryato.dimas.example.models.Penduduk;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DataPendudukJdbcItemWriter {

    @Bean(name = "dataPendudukJdbcWriter")
    public JdbcBatchItemWriter<Penduduk> jdbcWriter(
            @Qualifier("dataSource") DataSource dataSource) {
        JdbcBatchItemWriter<Penduduk> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
        jdbcBatchItemWriter.setDataSource(dataSource);
        jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        jdbcBatchItemWriter.setSql("update data_penduduk set nama_identitias = :namaLengkap where nik = :nik");
        jdbcBatchItemWriter.afterPropertiesSet();
        return jdbcBatchItemWriter;
    }
}
