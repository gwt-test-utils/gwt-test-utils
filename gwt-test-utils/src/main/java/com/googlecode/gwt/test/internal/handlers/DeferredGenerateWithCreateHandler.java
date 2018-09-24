package com.googlecode.gwt.test.internal.handlers;

import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.internal.ModuleData;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.util.HashSet;
import java.util.Set;

public class DeferredGenerateWithCreateHandler implements GwtCreateHandler {

    private Set<Class<?>> customGeneratedClasses;

    public Object create(Class<?> classLiteral) {
        if (customGeneratedClasses == null) {
            customGeneratedClasses = initCustomGeneratedClasses();
        }

        for (Class<?> clazz : customGeneratedClasses) {
            if (clazz.isAssignableFrom(classLiteral)) {
                throw new GwtTestConfigurationException(
                        "A custom Generator should be used to instanciate '"
                                + classLiteral.getName()
                                + "', but gwt-test-utils does not support GWT compiler API, so you have to add our own GwtCreateHandler with 'GwtTest.addGwtCreateHandler(..)' method or to declare your tested object with @Mock");
            }
        }

        return null;
    }

    private Set<Class<?>> initCustomGeneratedClasses() {
        Set<Class<?>> result = new HashSet<>();
        String moduleName = GwtConfig.get().getTestedModuleName();
        for (String className : ModuleData.get(moduleName).getCustomGeneratedClasses()) {
            try {
                result.add(GwtReflectionUtils.getClass(className));
            } catch (ClassNotFoundException e) {
                throw new GwtTestConfigurationException(
                        "Cannot find class configured to be instanced with a custom 'generate-with' Generator : '"
                                + className + "'");
            }
        }

        return result;
    }

}
