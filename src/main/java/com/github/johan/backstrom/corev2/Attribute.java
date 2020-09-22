package com.github.johan.backstrom.corev2;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Attribute {
    String value() default "";
}
