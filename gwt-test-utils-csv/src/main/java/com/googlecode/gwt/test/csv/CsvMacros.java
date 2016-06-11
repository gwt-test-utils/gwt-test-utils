package com.googlecode.gwt.test.csv;

import java.lang.annotation.*;

/**
 * Markup annotation for integration test classes to specify where related macro files can be found.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface CsvMacros {
    String pattern() default ".+\\..*";

    String value();
}