package com.tistory.jaimemin.springbatch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class SkipItemWriter implements ItemWriter<String> {

    private int cnt = 0;

    @Override
    public void write(List<? extends String> items) throws Exception {
        for (String item : items) {
            if ("-12".equals(item)) {
                throw new SkippableException("Write failed cnt : " + cnt++);
            }

            System.out.println("ItemWriter: " + item);
        }
    }
}
