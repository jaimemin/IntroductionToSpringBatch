package com.tistory.jaimemin.springbatch.batch.job.api;

import com.tistory.jaimemin.springbatch.batch.chunk.processor.ApiItemProcessor1;
import com.tistory.jaimemin.springbatch.batch.chunk.processor.ApiItemProcessor2;
import com.tistory.jaimemin.springbatch.batch.chunk.processor.ApiItemProcessor3;
import com.tistory.jaimemin.springbatch.batch.chunk.writer.ApiItemWriter1;
import com.tistory.jaimemin.springbatch.batch.chunk.writer.ApiItemWriter2;
import com.tistory.jaimemin.springbatch.batch.chunk.writer.ApiItemWriter3;
import com.tistory.jaimemin.springbatch.batch.classifier.ProcessorClassifier;
import com.tistory.jaimemin.springbatch.batch.classifier.WriterClassifier;
import com.tistory.jaimemin.springbatch.batch.domain.ApiRequestVO;
import com.tistory.jaimemin.springbatch.batch.domain.ProductVO;
import com.tistory.jaimemin.springbatch.batch.partition.ProductPartitioner;
import com.tistory.jaimemin.springbatch.service.ApiService1;
import com.tistory.jaimemin.springbatch.service.ApiService2;
import com.tistory.jaimemin.springbatch.service.ApiService3;
import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration {

    private final DataSource dataSource;

    private final StepBuilderFactory stepBuilderFactory;

    private final ApiService1 apiService1;

    private final ApiService2 apiService2;

    private final ApiService3 apiService3;

    private int chunkSize = 10;

    @Bean
    public Step apiMasterStep() throws Exception {
        return stepBuilderFactory.get("apiMasterStep")
                .partitioner(apiSlaveStep().getName(), partitioner())
                .step(apiSlaveStep())
                .gridSize(3) // 임시
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(6);
        taskExecutor.setThreadNamePrefix("api-thread-");

        return taskExecutor;
    }

    @Bean
    public Step apiSlaveStep() throws Exception {
        return stepBuilderFactory.get("apiSlaveStep")
                .<ProductVO, ProductVO>chunk(chunkSize)
                .reader(itemReader(null))
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ProductPartitioner partitioner() {
        ProductPartitioner productPartitioner = new ProductPartitioner();
        productPartitioner.setDataSource(dataSource);

        return productPartitioner;
    }

    @Bean
    @StepScope
    public ItemReader<ProductVO> itemReader(@Value("#{stepExecutionContext['product']}") ProductVO productVO) throws Exception {
        JdbcPagingItemReader<ProductVO> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setPageSize(chunkSize);
        reader.setRowMapper(new BeanPropertyRowMapper<>(ProductVO.class));
        reader.setParameterValues(QueryGenerator.getParameterForQuery("type", productVO.getType()));
        reader.setQueryProvider(getQueryProvider());
        reader.afterPropertiesSet();

        return reader;
    }

    @Bean
    public ItemProcessor itemProcessor() {
        ClassifierCompositeItemProcessor<ProductVO, ApiRequestVO> processor
                = new ClassifierCompositeItemProcessor<>();
        processor.setClassifier(getProcessorClassifier());

        return processor;
    }

    @Bean
    public ItemWriter itemWriter() {
        ClassifierCompositeItemWriter<ApiRequestVO> writer
                = new ClassifierCompositeItemWriter<>();
        writer.setClassifier(getWriterClassifier());

        return writer;
    }

    private WriterClassifier<ApiRequestVO, ItemWriter<? super ApiRequestVO>> getWriterClassifier() {
        WriterClassifier<ApiRequestVO, ItemWriter<? super ApiRequestVO>> classifier
                = new WriterClassifier<>();
        classifier.setWriterMap(getWriterMap());

        return classifier;
    }

    private Map<String, ItemWriter<ApiRequestVO>> getWriterMap() {
        Map<String, ItemWriter<ApiRequestVO>> writerMap = new HashMap<>();
        writerMap.put("1", new ApiItemWriter1(apiService1));
        writerMap.put("2", new ApiItemWriter2(apiService2));
        writerMap.put("3", new ApiItemWriter3(apiService3));

        return writerMap;
    }

    private ProcessorClassifier<ProductVO, ItemProcessor<?, ? extends ApiRequestVO>> getProcessorClassifier() {
        ProcessorClassifier<ProductVO, ItemProcessor<?, ? extends ApiRequestVO>> classifier
                = new ProcessorClassifier<>();
        classifier.setProcessorMap(getProcessorMap());

        return classifier;
    }

    private Map<String, ItemProcessor<ProductVO, ApiRequestVO>> getProcessorMap() {
        Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap = new HashMap<>();
        processorMap.put("1", new ApiItemProcessor1());
        processorMap.put("2", new ApiItemProcessor2());
        processorMap.put("3", new ApiItemProcessor3());

        return processorMap;
    }

    private MySqlPagingQueryProvider getQueryProvider() {
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, name, price, type");
        queryProvider.setFromClause("from product");
        queryProvider.setWhereClause("where type = :type");
        queryProvider.setSortKeys(getSortKeys());

        return queryProvider;
    }

    private Map<String, Order> getSortKeys() {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.DESCENDING);

        return sortKeys;
    }
}
