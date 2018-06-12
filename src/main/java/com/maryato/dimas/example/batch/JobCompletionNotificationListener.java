package com.maryato.dimas.example.batch;

import com.maryato.dimas.example.entity.DataPenduduk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final static Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            jdbcTemplate.query("select nik, nama_identitias, tanggal_lahir from data_penduduk",
                    (rs, row) -> new DataPenduduk(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getDate(3))
            ).forEach(person -> log.info("data : {} in database", person));
        }
    }
}
