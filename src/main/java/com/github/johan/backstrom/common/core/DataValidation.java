package com.github.johan.backstrom.common.core;

import java.util.Map;

public interface DataValidation {
    boolean validateData(Map<String, Attribute> params);
}