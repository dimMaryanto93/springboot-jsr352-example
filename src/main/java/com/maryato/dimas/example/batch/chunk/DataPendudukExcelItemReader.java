package com.maryato.dimas.example.batch.chunk;

import com.maryato.dimas.example.models.Penduduk;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.batch.item.excel.support.rowset.DefaultRowSetFactory;
import org.springframework.batch.item.excel.support.rowset.RowSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DataPendudukExcelItemReader {

    @Bean(name = "dataExcelItemReader")
    @StepScope
    public PoiItemReader<Penduduk> itemReader(
            @Value("#{jobParameters['sourceExcelPath']}") String fileName) throws Exception {
        PoiItemReader reader = new PoiItemReader();
        File file = new File(fileName);
        byte[] resource = null;
        if (file.exists()) {
            resource = FileUtils.readFileToByteArray(file);
        }

        reader.setResource(new ByteArrayResource(resource));
        reader.setLinesToSkip(1);
        reader.setRowMapper(new PendudukExcelRowMapper());
        reader.setRowSetFactory(new DefaultRowSetFactory());
        reader.afterPropertiesSet();
        return reader;
    }

    public class PendudukExcelRowMapper implements RowMapper<Penduduk> {
        @Override
        public Penduduk mapRow(RowSet rs) throws Exception {
            return new Penduduk(
                    rs.getColumnValue(0),
                    rs.getColumnValue(1),
                    rs.getColumnValue(2));
        }
    }

}
