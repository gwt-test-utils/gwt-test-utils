package com.googlecode.gwt.test.csv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Markup annotation for integration test classes to specify where related scenario files can be
 * found.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface CsvDirectory {

   String extension() default ".csv";

   String value();
}
