package com.tistory.jaimemin.springbatch;

import javax.batch.api.chunk.listener.ItemReadListener;

public class CustomItemReadListener implements ItemReadListener {

    @Override
    public void beforeRead() throws Exception {
        System.out.println(">> before Read");
    }

    @Override
    public void afterRead(Object o) throws Exception {
        System.out.println(">> after Read");
    }

    @Override
    public void onReadError(Exception e) throws Exception {
        System.out.println(">> after Read Error");
    }
}
