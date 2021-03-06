package com.github.johan.backstrom.corev2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface WithGenerators {
    Class[] value();
}
