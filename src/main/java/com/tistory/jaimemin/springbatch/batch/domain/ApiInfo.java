package com.tistory.jaimemin.springbatch.batch.domain;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ApiInfo {

    private String url;

    private List<? extends ApiRequestVO> apiRequests = new ArrayList<>();
}
