package com.maryato.dimas.example.controller;

import com.maryato.dimas.example.models.Penduduk;
import com.maryato.dimas.example.service.StorageService;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Set;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/api/penduduk")
public class PendudukController {

    @Autowired
    @Qualifier("jmsReaderPenduduk")
    private JmsTemplate readerPendudukTemplate;
    @Autowired
    @Qualifier("jmsWriterPenduduk")
    private JmsTemplate writerPendudukTemplate;
    @Autowired
    private StorageService fileService;
    @Autowired
    private JobOperator jobOperator;

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


    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<?> upload(
            @RequestPart("content") @Valid @NotNull MultipartFile file,
            @RequestPart String description) {

        try {
            String filePath = fileService.storeFile(file);
            return ok().body(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
            return badRequest().body("Job instanse already complate");
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
            return badRequest().body("Job execution already running");
        } catch (JobRestartException e) {
            e.printStackTrace();
            return badRequest().body("Job restarting");
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
            return badRequest().body("invalid job parameters");
        }

    }

    @GetMapping("/jobs")
    public ResponseEntity<?> findJobs() {
        return ok(jobOperator.getJobNames());
    }

    @PostMapping("/job/stop")
    public ResponseEntity<?> stopBatch() throws NoSuchJobException, NoSuchJobExecutionException, JobExecutionNotRunningException {
        Set<Long> executions = jobOperator.getRunningExecutions("excelToExcelJob");
        if (!executions.isEmpty()) {
            jobOperator.stop(executions.iterator().next());
            return ok().build();
        }

        return noContent().build();
    }

}
