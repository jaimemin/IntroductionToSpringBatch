package com.tistory.jaimemin.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class JpaPagingConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

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
                .<Customer , Customer >chunk(10)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    private ItemReader<? extends Customer> customItemReader() {
        return new JpaPagingItemReaderBuilder<Customer>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(10)
                .queryString("SELECT c FROM Customer c FETCH c.address")
                .build();
    }

    @Bean
    public ItemWriter<Customer> customItemWriter() {
        return items -> {
            for (Customer item : items) {
                System.out.println(item.getAddress().getLocation());
            }
        };
    }

}
