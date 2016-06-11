package com.googlecode.gwt.test.patchers;

import javassist.CtClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Indicates that the annotated method is be used to add some behavior on a class before its
 * {@link PatchMethod} are applied through the <strong>javassist</strong> API.
 * </p>
 * <p>
 * Methods annotated with <code>InitMethod</code> <strong>must</strong> be static an have one and
 * only one parameter of type {@link CtClass} from the <code>javassist</code> API.
 * </p>
 *
 * @author Gael Lazzari
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InitMethod {

    /**
     * <p>
     * Specify if this initialization method should override an existing one, declared in another
     * {@link PatchClass}. Only one <code>InitMethod</code> with override = true can exist.
     * Otherwise, an exception will be thrown.
     * </p>
     * <p>
     * Default value is <strong>false</strong>.
     * </p>
     *
     * @return True is this initialization method should override an existing one, false otherwise.
     */
    boolean override() default false;

}
