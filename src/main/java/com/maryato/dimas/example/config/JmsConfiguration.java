package com.maryato.dimas.example.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

@Configuration
@EnableJms
public class JmsConfiguration {

    @Bean
    public JmsListenerContainerFactory<?> messageFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer,
            MessageConverter converter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setReceiveTimeout(50000l);
        factory.setMessageConverter(converter);
        factory.setSessionTransacted(true);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public ActiveMQQueue queuePendudukReader() {
        return new ActiveMQQueue("/penduduk/reader");
    }

    @Bean
    public ActiveMQQueue queuePendudukWriter() {
        return new ActiveMQQueue("/penduduk/writer");
    }

    @Bean
    public JmsTemplate jmsReaderPenduduk(
            MessageConverter converter,
            ConnectionFactory connection,
            @Qualifier("queuePendudukReader") ActiveMQQueue queue) throws JMSException {
        JmsTemplate template = new JmsTemplate(connection);
        template.setMessageConverter(converter);
        template.setReceiveTimeout(Long.MAX_VALUE);
        template.setDeliveryPersistent(true);
        template.setDefaultDestinationName(queue.getQueueName());
        template.setSessionTransacted(true);
        return template;
    }

    @Bean
    public JmsTemplate jmsWriterPenduduk(
            MessageConverter converter,
            ConnectionFactory connection,
            @Qualifier("queuePendudukWriter") ActiveMQQueue queue) throws JMSException {
        JmsTemplate template = new JmsTemplate(connection);
        template.setMessageConverter(converter);
        template.setReceiveTimeout(Long.MAX_VALUE);
        template.setDefaultDestinationName(queue.getQueueName());
        template.setDeliveryPersistent(true);
        template.setSessionTransacted(true);
        return template;
    }
}
