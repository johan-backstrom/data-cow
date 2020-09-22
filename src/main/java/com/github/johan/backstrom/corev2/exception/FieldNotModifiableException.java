package com.github.johan.backstrom.corev2.exception;

public class FieldNotModifiableException extends RuntimeException {
    public FieldNotModifiableException(String message){
        super(message);
    }

    public FieldNotModifiableException(String message, Exception e){
        super(message, e);
    }
}
