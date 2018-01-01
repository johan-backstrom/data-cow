package com.github.johan.backstrom.common.core;

import java.util.Map;

public interface DataGeneration<T> {

    T generateData(Map<String, Attribute> params);
}