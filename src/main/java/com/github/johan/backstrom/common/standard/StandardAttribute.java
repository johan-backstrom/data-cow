package com.github.johan.backstrom.common.standard;

import com.github.johan.backstrom.common.core.Attribute;
import com.github.johan.backstrom.common.core.DataGeneration;
import com.github.johan.backstrom.common.core.DataValidation;

import java.util.Map;

public class StandardAttribute<T> implements Attribute<T> {

    private String name;
    private DataGeneration<T> dataGeneration;
    private DataValidation dataValidation;
    private T value;

    public StandardAttribute(String name, DataGeneration<T> generation) {
        this.name = name;
        this.dataGeneration = generation;
        this.dataValidation = params -> true;
    }

    public StandardAttribute(String name, DataGeneration<T> dataGeneration, DataValidation dataValidation) {
        this.name = name;
        this.dataGeneration = dataGeneration;
        this.dataValidation = dataValidation;
    }

    @Override
    public void generateAttributeData(Map<String, Attribute> parents) {
        value = dataGeneration.generateData(parents);
    }

    @Override
    public boolean validateAttributeData(Map<String, Attribute> parents) {
        return dataValidation.validateData(parents);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }
}
