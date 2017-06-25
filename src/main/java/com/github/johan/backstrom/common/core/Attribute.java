package com.github.johan.backstrom.common.core;

import java.util.Map;

public interface Attribute<T> {

    void generateAttributeData(Map<String, Attribute> parents);
    boolean validateAttributeData(Map<String, Attribute> parents);
    String getName();
    T getValue();
    void setValue(T value);
}
