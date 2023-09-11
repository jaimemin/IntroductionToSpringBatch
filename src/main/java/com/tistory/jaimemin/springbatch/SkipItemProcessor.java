package com.tistory.jaimemin.springbatch;

import org.springframework.batch.item.ItemProcessor;

public class SkipItemProcessor implements ItemProcessor<String, String> {

    private int cnt = 0;

    @Override
    public String process(String item) throws Exception {
        if ("6".equals(item)
                || "7".equals(item)) {
            throw new SkippableException("Process failed cnt : " + cnt++);
        }

        System.out.println("ItemProcess : " + item);

        return String.valueOf(Integer.valueOf(item) * -1);
    }
}
