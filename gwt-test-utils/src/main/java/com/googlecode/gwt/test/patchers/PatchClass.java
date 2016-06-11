package com.googlecode.gwt.test.patchers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated class is used to generate a JVM-compliant version of a particular
 * class.
 *
 * @author Bertrand Paquet
 * @author Gael Lazzari
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PatchClass {

    /**
     * Name of the class to patch, in case it is internal.
     *
     * @return The name of the class to patch.
     */
    String target() default "";

    /**
     * Class to patch.
     *
     * @return The class to patch
     */
    Class<?> value() default PatchClass.class;

}
