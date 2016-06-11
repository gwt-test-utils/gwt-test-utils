package com.googlecode.gwt.test.csv;

import java.lang.annotation.*;

/**
 * Markup annotation to specify which methods can be called from scenario files.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface CsvMethod {

    public String methodName() default "";

}
