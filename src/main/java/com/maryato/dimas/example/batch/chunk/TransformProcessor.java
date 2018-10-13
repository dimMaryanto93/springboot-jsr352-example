package com.maryato.dimas.example.batch.chunk;

import com.maryato.dimas.example.models.Penduduk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TransformProcessor implements ItemProcessor<Penduduk, Penduduk> {

    private static final Logger console = LoggerFactory.getLogger(TransformProcessor.class);

    @Override
    public Penduduk process(Penduduk old) throws Exception {
        console.info(">> data transform {}", old);
        return old;
    }
}
