package com.tistory.jaimemin.springbatch;

import lombok.NoArgsConstructor;

public class CustomSkipException extends Exception {

    public CustomSkipException() {
        super();
    }

    public CustomSkipException(String message) {
        super(message);
    }
}
