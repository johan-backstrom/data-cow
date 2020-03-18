package com.github.johan.backstrom.common.corev2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
public @interface References {
    String value();
}
