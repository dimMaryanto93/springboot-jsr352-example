package com.maryato.dimas.example.config.items;

import com.maryato.dimas.example.models.Penduduk;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@StepScope
public class DataPendudukExcelItemWriterListener implements ItemStreamWriter<Penduduk> {

    @Autowired
    private DataPendudukExcelItemWriter excelItemWriter;
    @Autowired
    @Qualifier("dataPendudukJdbcWriter")
    private JdbcBatchItemWriter<Penduduk> jdbcItemWriter;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        excelItemWriter.beforeStep(stepExecution);
    }

    @AfterStep
    public void afterStep(StepExecution stepExecution) throws IOException {
        excelItemWriter.afterStep(stepExecution);
    }

    @Override
    public void close() throws ItemStreamException {
    }

    @Override
    public void write(List<? extends Penduduk> list) throws Exception {
        excelItemWriter.write(list);
        jdbcItemWriter.write(list);
    }
}
