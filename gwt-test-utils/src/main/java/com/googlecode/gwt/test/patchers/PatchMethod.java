package com.googlecode.gwt.test.patchers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method is used to provide a JVM-compliant version of a particular
 * method.
 *
 * @author Bertrand Paquet
 * @author Gael Lazzari
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PatchMethod {

    /**
     * Indicates the actual class of a parameter. Use this annotation for a PatchMethod parameter
     * whenever the type of the argument is not accessible.
     *
     * @author Gael Lazzari
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface ParamType {

        /**
         * The full qualifed name of the parameter's type
         *
         * @return The type of the parameter in the method to patch.
         * @see Class#forName(String)
         */
        String value() default "";
    }

    /**
     * <p>
     * Specify if this patch method should override an existing one, declared in another
     * {@link PatchClass}. Only one <code>PatchMethod</code> with override = true can exist.
     * Otherwise, an exception will be thrown.
     * </p>
     * <p>
     * Default value is <strong>false</strong>.
     * </p>
     *
     * @return True is this patch method should override an existing one, false otherwise.
     */
    boolean override() default false;

    /**
     * The name of the method to patch. If not set, gwt-test-utils will check for a method with the
     * same name as the annotated one.
     *
     * @return The name of the method to patch.
     */
    String value() default "";

}
