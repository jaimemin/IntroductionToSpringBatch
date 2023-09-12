package com.tistory.jaimemin.springbatch;

import org.springframework.batch.item.ItemProcessor;

public class RetryItemProcessor implements ItemProcessor<String, String> {

    private int cnt = 0;

    @Override
    public String process(String item) throws Exception {
        if ("2".equals(item) || "3".equals(item)) {
            throw new RetryableException("failed cnt : " + ++cnt);
        }

        return item;
    }
}
