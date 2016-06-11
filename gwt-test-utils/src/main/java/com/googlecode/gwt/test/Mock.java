package com.googlecode.gwt.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Indicates that the annotated field should be initialized as a mock instance of the field type.
 * </p>
 * <p>
 * <p>
 * The class declaring such a field must extend {@link GwtTestWithEasyMock} in order to make the
 * mock injection possible.
 * </p>
 * <p>
 * <p>
 * Mock objects initialized using this annotation will be replayed, verified and reseted when
 * calling the corresponding methods of {@link GwtTestWithEasyMock} .
 * </p>
 *
 * @author Bertrand Paquet
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Mock {

    /**
     * Specifies th unique identifier of the mock
     *
     * @return The unique identifier of the mock
     */
    String value() default "";

}
