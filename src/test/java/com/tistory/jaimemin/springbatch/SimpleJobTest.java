package com.tistory.jaimemin.springbatch;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SimpleJobConfiguration.class, TestBatchConfig.class})
public class SimpleJobTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void simpleJob_test() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "test")
                .addLong("date", new Date().getTime())
                .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        JobExecution step = jobLauncherTestUtils.launchStep("step");

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        StepExecution stepExecution = (StepExecution) ((List) step.getStepExecutions()).get(0);

        assertThat(stepExecution.getCommitCount()).isEqualTo(11);
        assertThat(stepExecution.getWriteCount()).isEqualTo(1000);
        assertThat(stepExecution.getReadCount()).isEqualTo(1000);
    }

    @After
    public void clear() {
        jdbcTemplate.execute("DELETE FROM customer2");
    }

}
