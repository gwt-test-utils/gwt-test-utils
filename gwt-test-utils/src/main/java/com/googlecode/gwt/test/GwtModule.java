package com.googlecode.gwt.test;

import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;

import java.lang.annotation.*;

/**
 * Specifies the GWT module to test in a {@link GwtModuleRunner}.
 *
 * @author Gael Lazzari
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface GwtModule {

    /**
     * Specifies the full qualified name of the GWT module under test. A
     * {@link GwtTestConfigurationException} would be thrown if the name is null or empty or is not
     * declared as a 'gwt-module' in the META-INF/gwt-test-utils.properties
     *
     * @return the full qualified name of the GWT module under test
     */
    String value();

}
