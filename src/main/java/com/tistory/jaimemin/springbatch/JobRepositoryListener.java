package com.tistory.jaimemin.springbatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobRepositoryListener implements JobExecutionListener {

    private final JobRepository jobRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance()
                .getJobName();
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "20210102")
                .toJobParameters();

        JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);

        if (ObjectUtils.isEmpty(lastJobExecution)) {
            return;
        }

        for (StepExecution execution : lastJobExecution.getStepExecutions()) {
            BatchStatus status = execution.getStatus();
            ExitStatus exitStatus = execution.getExitStatus();
            String stepName = execution.getStepName();

            log.info("status: {}, exitStatus: {}, stepName: {}", status, exitStatus, stepName);
        }
    }
}
