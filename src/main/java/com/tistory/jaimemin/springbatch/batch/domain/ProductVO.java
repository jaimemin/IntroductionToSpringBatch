package com.tistory.jaimemin.springbatch.batch.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = ProductVODeserializer.class)
public class ProductVO {

    private Long id;

    private String name;

    private int price;

    private String type;
}
