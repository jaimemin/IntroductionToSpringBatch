package com.tistory.jaimemin.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.exception.SimpleLimitExceptionHandler;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RepeatConfiguration {

    private final int REPEAT_COUNT = 3;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob() throws Exception {
        return this.jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .build();
    }

    @Bean
    public Step step() throws Exception {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(5)
                .reader(new ItemReader<String>() {
                    int i = 0;

                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        return ++i > REPEAT_COUNT ? null : "item" + i;
                    }
                })
                .processor(new ItemProcessor<String, String>() {
                    RepeatTemplate repeatTemplate = new RepeatTemplate();

                    @Override
                    public String process(String item) throws Exception {
//                        repeatTemplate.setCompletionPolicy(new SimpleCompletionPolicy(REPEAT_COUNT));
//                        repeatTemplate.setCompletionPolicy(new TimeoutTerminationPolicy(3000));

                        CompositeCompletionPolicy completionPolicy = new CompositeCompletionPolicy();
                        CompletionPolicy[] completionPolicies = new CompletionPolicy[]{
                                new SimpleCompletionPolicy(REPEAT_COUNT),
                                new TimeoutTerminationPolicy(3000)
                        };
                        completionPolicy.setPolicies(completionPolicies);
                        repeatTemplate.setCompletionPolicy(completionPolicy);

                        repeatTemplate.setExceptionHandler(new SimpleLimitExceptionHandler(REPEAT_COUNT));

                        repeatTemplate.iterate(repeatContext -> {
                            System.out.println("repeatTemplate is testing");

                            return RepeatStatus.CONTINUABLE;
//                            throw new RuntimeException("Exception occurred");
                        });

                        return item;
                    }
                })
                .writer(items -> System.out.println(items))
                .build();
    }

}
