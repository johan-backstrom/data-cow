package com.github.johan.backstrom.common.corev2.exception;

public class GeneratorNotFoundException extends RuntimeException {
    public GeneratorNotFoundException(){
        super();
    }

    public GeneratorNotFoundException(String message){
        super(message);
    }
}
