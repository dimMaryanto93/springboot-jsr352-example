package com.maryato.dimas.example.batch;

import com.maryato.dimas.example.consume.FileDataPenduduk;
import com.maryato.dimas.example.entity.DataPenduduk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class DataPendudukItemProccessor implements ItemProcessor<FileDataPenduduk, com.maryato.dimas.example.entity.DataPenduduk> {

    @Override
    public DataPenduduk process(FileDataPenduduk csvFile) throws Exception {
        DataPenduduk dataDb = new DataPenduduk();
        dataDb.setNik(csvFile.getId());
        dataDb.setNamaIdentitas(csvFile.getName());
        dataDb.setTanggalLahir(Date.valueOf(csvFile.getBirtDate()));
        return dataDb;
    }
}
