package com.github.johan.backstrom.corev2.exception;

public class UnknownGeneratorArgumentException extends RuntimeException {

    public UnknownGeneratorArgumentException(String message){
        super(message);
    }

    public UnknownGeneratorArgumentException(String message, Exception e){
        super(message, e);
    }
}
