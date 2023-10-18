package com.tistory.jaimemin.springbatch;

import org.springframework.batch.core.ItemProcessListener;

public class CustomItemProcessListener implements ItemProcessListener<Integer, String> {

    @Override
    public void beforeProcess(Integer integer) {
        System.out.println(">> before Process");
    }

    @Override
    public void afterProcess(Integer integer, String s) {
        System.out.println(">> after Process");
    }

    @Override
    public void onProcessError(Integer integer, Exception e) {
        System.out.println(">> before Process Error");
    }
}
