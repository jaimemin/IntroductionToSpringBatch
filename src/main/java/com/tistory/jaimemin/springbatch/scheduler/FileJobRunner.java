package com.tistory.jaimemin.springbatch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileJobRunner extends JobRunner {

    private final Scheduler scheduler;

    @Override
    protected void doRun(ApplicationArguments args) {
        String[] sourceArgs = args.getSourceArgs();
        JobDetail jobDetail = buildJobDetail(FileSchJob.class, "fileJob", "batch", new HashMap());
        Trigger trigger = buildJobTrigger("0/50 * * * * ?");

        if (!ObjectUtils.isEmpty(sourceArgs)) {
            jobDetail.getJobDataMap().put("requestDate", sourceArgs[0]);
        }

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            log.error("[FileJobRunner] ERROR " + e);
        }
    }
}
