package com.googlecode.gwt.test.csv;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Markup annotation for integration test classes to specify where related macro files can be found.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface CsvMacros {
   String pattern() default ".+\\..*";

   String value();
}