package com.maryato.dimas.example.config.items;

import com.maryato.dimas.example.models.Penduduk;
import org.springframework.batch.item.jms.JmsItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataPendudukJmsItemReader {

    @Bean
    public JmsItemReader<Penduduk> fileReader(
            @Qualifier("jmsReaderPenduduk") JmsTemplate template) throws Exception {
        JmsItemReader<Penduduk> itemReader = new JmsItemReader<>();
        itemReader.setJmsTemplate(template);
        itemReader.setItemType(Penduduk.class);
        itemReader.afterPropertiesSet();
        return itemReader;
    }
}
