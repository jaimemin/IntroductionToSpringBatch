package com.tistory.jaimemin.springbatch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import java.util.Random;

public class CustomDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution
            , StepExecution stepExecution) {
        int count = new Random().nextInt() % 2;

        if (count % 2 == 0) {
            return new FlowExecutionStatus("EVEN");
        } else {
            return new FlowExecutionStatus("ODD");
        }
    }
}
