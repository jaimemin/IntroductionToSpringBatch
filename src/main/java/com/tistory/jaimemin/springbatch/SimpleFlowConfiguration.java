package com.tistory.jaimemin.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SimpleFlowConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob() {
        return this.jobBuilderFactory.get("batchJob")
                .start(flow())
                    .on("COMPLETED")
                    .to(flow2())
                .from(flow())
                    .on("FAILED")
                    .to(flow3())
                .end()
                .build();
    }

    @Bean
    public Flow flow() {
        FlowBuilder<Flow> builder = new FlowBuilder<>("flow");
        builder.start(step())
                .next(step2())
                .end();

        return builder.build();
    }

    @Bean
    public Flow flow2() {
        FlowBuilder<Flow> builder = new FlowBuilder<>("flow2");
        builder.start(flow3())
                .next(step5())
                .next(step6())
                .end();

        return builder.build();
    }

    @Bean
    public Flow flow3() {
        FlowBuilder<Flow> builder = new FlowBuilder<>("flow3");
        builder.start(step3())
                .next(step4())
                .end();

        return builder.build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("step has executed");

                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("step2 has executed");

                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("step3 has executed");

                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step step4() {
        return stepBuilderFactory.get("step4")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("step4 has executed");

                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step step5() {
        return stepBuilderFactory.get("step5")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("step5 has executed");

                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step step6() {
        return stepBuilderFactory.get("step6")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("step6 has executed");

                    return RepeatStatus.FINISHED;
                }))
                .build();
    }
}
