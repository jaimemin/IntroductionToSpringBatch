package com.tistory.jaimemin.springbatch.batch.chunk.processor;

import com.tistory.jaimemin.springbatch.batch.domain.Product;
import com.tistory.jaimemin.springbatch.batch.domain.ProductVO;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor implements ItemProcessor<ProductVO, Product> {

    @Override
    public Product process(ProductVO item) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(item, Product.class);

        return product;
    }
}
