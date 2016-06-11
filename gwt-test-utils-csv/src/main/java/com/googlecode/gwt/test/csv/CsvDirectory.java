package com.googlecode.gwt.test.csv;

import java.lang.annotation.*;

/**
 * Markup annotation for integration test classes to specify where related scenario files can be
 * found.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface CsvDirectory {

    String extension() default ".csv";

    String value();
}
