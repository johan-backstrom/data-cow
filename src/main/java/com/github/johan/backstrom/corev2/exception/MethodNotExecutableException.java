package com.github.johan.backstrom.corev2.exception;

public class MethodNotExecutableException extends RuntimeException {
    public MethodNotExecutableException(String message){
        super(message);
    }

    public MethodNotExecutableException(String message, Exception e){
        super(message, e);
    }
}
