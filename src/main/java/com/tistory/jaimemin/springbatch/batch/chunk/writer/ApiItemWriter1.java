package com.tistory.jaimemin.springbatch.batch.chunk.writer;

import com.tistory.jaimemin.springbatch.batch.domain.ApiRequestVO;
import com.tistory.jaimemin.springbatch.batch.domain.ApiResponseVO;
import com.tistory.jaimemin.springbatch.service.AbstractApiService;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

public class ApiItemWriter1 extends FlatFileItemWriter<ApiRequestVO> {

    private final AbstractApiService apiService;

    public ApiItemWriter1(AbstractApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void write(List<? extends ApiRequestVO> items) throws Exception {
        ApiResponseVO responseVO = apiService.service(items);
        System.out.println("responseVO = " + responseVO);

        items.forEach(item -> item.setApiResponseVO(responseVO));

        super.setResource(new FileSystemResource("/Users/jaimemin/IdeaProjects/IntroductionToSpringBatch/src/main/resources/product1.txt"));
        super.open(new ExecutionContext());
        super.setLineAggregator(new DelimitedLineAggregator<>());
        super.setAppendAllowed(true);
        super.write(items);
    }
}
