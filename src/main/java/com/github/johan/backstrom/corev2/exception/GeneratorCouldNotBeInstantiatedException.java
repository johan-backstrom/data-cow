package com.github.johan.backstrom.corev2.exception;

public class GeneratorCouldNotBeInstantiatedException extends RuntimeException {

    public GeneratorCouldNotBeInstantiatedException(String message, Exception e){
        super(message, e);
    }
}
