package com.tistory.jaimemin.springbatch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class CustomJobExecutionListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Job started");
        System.out.println(String.format("jobName : %s", jobExecution.getJobInstance().getJobName()));
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        Long startTime = jobExecution.getStartTime().getTime();
        Long endTime = jobExecution.getEndTime().getTime();

        System.out.println(String.format("총 소요시간 : %d", endTime - startTime));
    }
}
