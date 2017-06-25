package com.github.johan.backstrom.common.core;

import java.util.Map;

public interface DataGeneration<T> {

    T getData(Map<String, Attribute> params);
}