package com.tistory.jaimemin.springbatch;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

public class CustomItemWriteListener implements ItemWriteListener<Customer> {


    @Override
    public void beforeWrite(List<? extends Customer> list) {

    }

    @Override
    public void afterWrite(List<? extends Customer> list) {
        System.out.println("Thread : " + Thread.currentThread().getName() + ", write items : " + list.size());
    }

    @Override
    public void onWriteError(Exception e, List<? extends Customer> list) {

    }
}
