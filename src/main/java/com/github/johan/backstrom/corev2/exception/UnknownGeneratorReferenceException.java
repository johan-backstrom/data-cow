package com.github.johan.backstrom.corev2.exception;

public class UnknownGeneratorReferenceException extends RuntimeException {

    public UnknownGeneratorReferenceException(String message){
        super(message);
    }

    public UnknownGeneratorReferenceException(String message, Exception e){
        super(message, e);
    }
}
