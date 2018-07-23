package com.maryato.dimas.example.config.items;

import com.maryato.dimas.example.models.Penduduk;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.batch.item.excel.support.rowset.DefaultRowSetFactory;
import org.springframework.batch.item.excel.support.rowset.RowSet;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class DataPendudukExcelItemReader {

    @Bean(name = "dataExcelItemReader")
    public PoiItemReader<Penduduk> itemReader() throws Exception {
        PoiItemReader reader = new PoiItemReader();
        reader.setResource(new ClassPathResource("/data/penduduk.xlsx"));
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