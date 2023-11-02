package com.tistory.jaimemin.springbatch.scheduler;

import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FileSchJob extends QuartzJobBean {

    @Autowired
    private Job fileJob;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String requestDate = (String) context.getJobDetail().getJobDataMap().get("requestDate");
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("id", new Date().getTime())
                .addString("requestDate", requestDate)
                .toJobParameters();
        int jobInstanceCount = jobExplorer.getJobInstanceCount(fileJob.getName());
        List<JobInstance> jobInstances = jobExplorer.getJobInstances(fileJob.getName(), 0, jobInstanceCount);

        if (!jobInstances.isEmpty()) {
            for (JobInstance jobInstance : jobInstances) {
                List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
                List<JobExecution> jobExecutionList = jobExecutions.stream()
                        .filter(jobExecution -> jobExecution.getJobParameters().getString("requestDate").equals(requestDate))
                        .collect(Collectors.toList());

                if (!jobExecutionList.isEmpty()) {
                    throw new JobExecutionException(String.format("%s already exists", requestDate));
                }
            }
        }

        jobLauncher.run(fileJob, jobParameters);
    }
}
