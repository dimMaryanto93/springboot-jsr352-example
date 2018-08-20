package com.maryato.dimas.example.controller;

import com.maryato.dimas.example.models.Penduduk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/penduduk")
public class PendudukController {

    @Autowired
    @Qualifier("jmsReaderPenduduk")
    private JmsTemplate readerPendudukTemplate;

    @Autowired
    @Qualifier("jmsWriterPenduduk")
    private JmsTemplate writerPendudukTemplate;

    @PostMapping("/send")
    public ResponseEntity<?> messageSend(@RequestBody Penduduk penduduk) {
        try {
            readerPendudukTemplate.convertAndSend(penduduk);
            Penduduk pdata = (Penduduk) writerPendudukTemplate.receiveAndConvert();
            return ok().body(pdata);
        } catch (JmsException e) {
            e.printStackTrace();
            return noContent().build();
        }
    }
}
