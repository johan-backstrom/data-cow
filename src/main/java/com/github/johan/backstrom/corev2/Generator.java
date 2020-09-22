package com.github.johan.backstrom.corev2;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Generator {
    String value() default "";
}
