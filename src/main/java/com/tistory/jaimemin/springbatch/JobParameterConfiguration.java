package com.tistory.jaimemin.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JobParameterConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(step1())
                .next(step2())
                .build();
    }

    public Step step2() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        // 실제 저장된 parameter
                        JobParameters jobParameters = stepContribution
                                .getStepExecution()
                                .getJobExecution()
                                .getJobParameters();
                        System.out.println("name: " + jobParameters.getString("name"));
                        System.out.println("seq: " + jobParameters.getLong("seq"));
                        System.out.println("date: " + jobParameters.getDate("date"));
                        System.out.println("age: " + jobParameters.getDouble("age"));

                        // 값만 확인하는 용도
                        Map<String, Object> jobParameters2 = chunkContext
                                .getStepContext()
                                .getJobParameters();

                        System.out.println("step2 was executed");

                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step1 was executed");

                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
}

