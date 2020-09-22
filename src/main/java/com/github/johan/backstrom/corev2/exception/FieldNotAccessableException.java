package com.github.johan.backstrom.corev2.exception;

public class FieldNotAccessableException extends RuntimeException {
    public FieldNotAccessableException(String message){
        super(message);
    }

    public FieldNotAccessableException(String message, Exception e){
        super(message, e);
    }
}
