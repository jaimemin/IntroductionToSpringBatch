package com.tistory.jaimemin.springbatch.service;

import com.tistory.jaimemin.springbatch.batch.domain.ApiInfo;
import com.tistory.jaimemin.springbatch.batch.domain.ApiResponseVO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService1 extends AbstractApiService {

    private static final String url = "http://localhost:8081/api/product/1";

    @Override
    protected ApiResponseVO doApiService(RestTemplate restTemplate, ApiInfo apiInfo) {
        ResponseEntity<String> responseEntity
                = restTemplate.postForEntity(url, apiInfo, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        ApiResponseVO apiResponseVO = ApiResponseVO.builder()
                .status(statusCodeValue)
                .message(responseEntity.getBody())
                .build();

        return apiResponseVO;
    }
}
