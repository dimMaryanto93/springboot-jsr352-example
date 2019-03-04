package com.maryato.dimas.example.batch.chunk;

import com.maryato.dimas.example.models.Penduduk;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class DataPendudukJdbcItemReader {

    @Bean
    public JdbcCursorItemReader<Penduduk> jdbcReader(
            @Qualifier("dataSource") DataSource dataSource,
            PreparedStatementSetter setter) throws Exception {
        JdbcCursorItemReader<Penduduk> itemReader = new JdbcCursorItemReader<>();
        itemReader.setDataSource(dataSource);
        itemReader.setSql("select * from data_penduduk where nik = ?");
        itemReader.setPreparedStatementSetter(setter);
        itemReader.setRowMapper((resultSet, i) -> {
            Penduduk p = new Penduduk();
            p.setNik(resultSet.getString("nik"));
            p.setNamaLengkap(resultSet.getString("nama_identitias"));
            p.setTanggalLahir(resultSet.getString("tanggal_lahir"));
            return p;
        });
        itemReader.afterPropertiesSet();
        return itemReader;
    }

    @StepScope
    @Component
    public class JdbcCursorReaderPreparedStatementSetter implements PreparedStatementSetter {

        @Value("#{jobParameters['nik']}")
        public String nik;

        @Override
        public void setValues(PreparedStatement preparedStatement) throws SQLException {
            preparedStatement.setString(1, nik);
        }
    }
}
