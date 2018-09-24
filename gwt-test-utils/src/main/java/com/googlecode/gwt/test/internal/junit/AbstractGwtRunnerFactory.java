package com.googlecode.gwt.test.internal.junit;

import com.googlecode.gwt.test.internal.GwtClassLoader;
import com.googlecode.gwt.test.internal.GwtFactory;
import org.junit.runner.Runner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Internal {@link Runner} factory which load the runner and the test class through
 * {@link GwtClassLoader}. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public abstract class AbstractGwtRunnerFactory {

    private static boolean hasJUnit45OrHigher;
    private static boolean hasJUnitParams;

    static {
        try {
            Class.forName("org.junit.runners.BlockJUnit4ClassRunner");
            hasJUnit45OrHigher = true;
        } catch (Throwable t) {
            hasJUnit45OrHigher = false;
        }

        try {
            Class.forName("junitparams.JUnitParamsRunner");
            hasJUnitParams = true;
        } catch (Throwable t) {
            hasJUnitParams = false;
        }
    }

    public Runner create(Class<?> clazz) throws Throwable {
        try {
            String runnerClassName = getRunnerClassName(hasJUnit45OrHigher, hasJUnitParams);
            return newInstance(runnerClassName, clazz);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    /**
     * Get the full qualified name of the JUnit {@link Runner} to use to run test class according to
     * the JUnit version available in the classpath.
     *
     * @param hasJUnit45OrHigher True if JUnit 4.5 or higher is available, false otherwise.
     * @param hasJUnitParams     True if JUnitParams is available, false otherwise.
     * @return The full qualified name of the JUnit {@link Runner} to use.
     */
    protected abstract String getRunnerClassName(boolean hasJUnit45OrHigher, boolean hasJUnitParams);

    private Runner newInstance(String runnerClassName, Class<?> constructorParam) throws Exception {
        Constructor<?> constructor;

        Class<?> runnerClass = GwtFactory.get().getClassLoader().loadClass(runnerClassName);
        Class<?> testedClass = GwtFactory.get().getClassLoader().loadClass(constructorParam.getName());
        constructor = runnerClass.getConstructor(Class.class);
        return (Runner) constructor.newInstance(testedClass);
    }

}
