package com.maryato.dimas.example.config.items;

import com.maryato.dimas.example.models.Penduduk;
import org.springframework.batch.item.jms.JmsItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataPendudukJmsItemWriter {

    @Bean
    public JmsItemWriter<Penduduk> writer(@Qualifier("jmsWriterPenduduk") JmsTemplate template) {
        JmsItemWriter<Penduduk> itemWriter = new JmsItemWriter<>();
        itemWriter.setJmsTemplate(template);
        return itemWriter;
    }
}
