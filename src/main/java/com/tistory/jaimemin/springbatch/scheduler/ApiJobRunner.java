package com.tistory.jaimemin.springbatch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiJobRunner extends JobRunner {

    private final Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {
        JobDetail jobDetail = buildJobDetail(ApiSchJob.class, "apiJob", "batch", new HashMap());
        Trigger trigger = buildJobTrigger("0/30 * * * * ?");

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            log.error("[ApiJobRunner] ERROR " + e);
        }
    }

}
