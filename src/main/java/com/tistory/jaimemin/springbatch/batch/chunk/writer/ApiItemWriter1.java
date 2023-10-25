package com.tistory.jaimemin.springbatch.batch.chunk.writer;

import com.tistory.jaimemin.springbatch.batch.domain.ApiRequestVO;
import com.tistory.jaimemin.springbatch.batch.domain.ApiResponseVO;
import com.tistory.jaimemin.springbatch.service.AbstractApiService;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class ApiItemWriter1 implements ItemWriter<ApiRequestVO> {

    private final AbstractApiService apiService;

    public ApiItemWriter1(AbstractApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void write(List<? extends ApiRequestVO> list) throws Exception {
        ApiResponseVO responseVO = apiService.service(list);

        System.out.println("responseVO = " + responseVO);
    }
}
