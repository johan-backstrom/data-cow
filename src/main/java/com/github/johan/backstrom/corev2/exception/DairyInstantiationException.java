package com.github.johan.backstrom.corev2.exception;

public class DairyInstantiationException extends RuntimeException {

    public DairyInstantiationException(String message){
        super(message);
    }

    public DairyInstantiationException(String message, Exception e){
        super(message, e);
    }
}
