package com.maryato.dimas.example.controller;

import com.maryato.dimas.example.models.Penduduk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/penduduk")
public class PendudukController {

    @Autowired
    @Qualifier("jmsReaderPenduduk")
    private JmsTemplate template;

    @PostMapping("/send")
    public ResponseEntity<?> messageSend(@RequestBody Penduduk penduduk) {
        template.convertAndSend(penduduk);
        return ResponseEntity.ok().build();
    }
}
