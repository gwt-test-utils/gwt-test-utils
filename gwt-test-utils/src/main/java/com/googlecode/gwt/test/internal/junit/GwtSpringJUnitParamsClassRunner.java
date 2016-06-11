package com.googlecode.gwt.test.internal.junit;

import junitparams.internal.ParameterisedTestClassRunner;
import junitparams.internal.TestMethod;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.List;

/**
 * gwt-test-utils {@link Runner} with support for Spring and JUnitParams <strong>For internal use
 * only.</strong>
 *
 * @author Gael Lazzari
 */
public class GwtSpringJUnitParamsClassRunner extends GwtSpringJUnit4ClassRunner {

    private Description description;
    private final ParameterisedTestClassRunner parameterisedRunner;

    public GwtSpringJUnitParamsClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
        parameterisedRunner = new ParameterisedTestClassRunner(getTestClass());
    }

    @Override
    public Description getDescription() {
        if (description == null) {
            description = Description.createSuiteDescription(getName(),
                    getTestClass().getAnnotations());
            List<FrameworkMethod> resultMethods = parameterisedRunner.returnListOfMethods();

            for (FrameworkMethod method : resultMethods)
                description.addChild(describeMethod(method));
        }

        return description;
    }

    @Override
    protected void collectInitializationErrors(List<Throwable> errors) {
        for (Throwable throwable : errors)
            throwable.printStackTrace();
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return parameterisedRunner.computeFrameworkMethods();
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        Statement methodInvoker = parameterisedRunner.parameterisedMethodInvoker(method, test);
        if (methodInvoker == null)
            methodInvoker = super.methodInvoker(method, test);

        return methodInvoker;
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        if (handleIgnored(method, notifier))
            return;

        TestMethod testMethod = parameterisedRunner.testMethodFor(method);
        if (parameterisedRunner.shouldRun(testMethod))
            parameterisedRunner.runParameterisedTest(testMethod, methodBlock(method), notifier);
        else
            super.runChild(method, notifier);
    }

    private Description describeMethod(FrameworkMethod method) {
        Description child = parameterisedRunner.describeParameterisedMethod(method);

        if (child == null)
            child = describeChild(method);

        return child;
    }

    private boolean handleIgnored(FrameworkMethod method, RunNotifier notifier) {
        TestMethod testMethod = parameterisedRunner.testMethodFor(method);
        if (testMethod.isIgnored())
            notifier.fireTestIgnored(describeMethod(method));

        return testMethod.isIgnored();
    }
}
