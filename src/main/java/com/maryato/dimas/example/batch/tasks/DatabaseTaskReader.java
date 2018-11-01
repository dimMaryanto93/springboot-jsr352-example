package com.maryato.dimas.example.batch.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DatabaseTaskReader implements Tasklet, StepExecutionListener {

    private List<String> arrays;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.arrays = new ArrayList<>();
//        this.arrays = (List<String>) stepExecution.getExecutionContext().get("listNik");
        log.info("data: {}", this.arrays);
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("size of files: {}", this.arrays.size());
        for (String nik : arrays) {
//            log.info("");
        }
        return RepeatStatus.FINISHED;
    }


    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}
