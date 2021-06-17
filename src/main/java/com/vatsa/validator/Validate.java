package com.vatsa.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Validate {

    ReturnType returnType() default ReturnType.BOOLEAN;
    Class<?> clazz();
    String method();
    Class<?>[] params() default {};
    String message() default "";
}
