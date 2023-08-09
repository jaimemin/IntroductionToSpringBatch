package com.tistory.jaimemin.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ItemWriterAdapterConfiguration {

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
        return stepBuilderFactory.get("step")
                .<String, String>chunk(10)
                .reader(new ItemReader<String>() {
                    int i = 0;

                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        i++;

                        return i > 10 ? null : "item" + i;
                    }
                })
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public ItemWriter<? super String> customItemWriter() {
        ItemWriterAdapter<String> writer = new ItemWriterAdapter<>();
        writer.setTargetObject(customService());
        writer.setTargetMethod("customWrite");

        return writer;
    }

    @Bean
    public CustomService customService() {
        return new CustomService();
    }

}
