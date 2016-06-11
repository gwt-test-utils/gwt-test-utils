package com.googlecode.gwt.test.csv.internal;

import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.internal.junit.GwtRunListener;
import com.googlecode.gwt.test.spring.GwtSpringCsvRunner;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import org.junit.*;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.TestClass;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * JUnit {@link Runner} implementation for spring CSV tests. Intent to be wrapped by
 * {@link GwtSpringCsvRunner}. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
@SuppressWarnings("deprecation")
public class GwtSpring2CsvJUnit4ClassRunner extends SpringJUnit4ClassRunner {

    class CsvMethodValidator {
        private final List<Throwable> fErrors = new ArrayList<Throwable>();

        private final TestClass fTestClass;

        public CsvMethodValidator(Object testClass) {
            // hack to use when JUnit 4.4 or older is in the classpath
            fTestClass = (TestClass) testClass;
        }

        public void assertValid() throws InitializationError {
            if (!fErrors.isEmpty())
                throw new InitializationError(fErrors);
        }

        public void validateInstanceMethods() {
            validateTestMethods(After.class, false);
            validateTestMethods(Before.class, false);
            validateTestMethods(Test.class, false);

            List<Method> methods = reader.getTestMethods();
            if (methods.size() == 0)
                fErrors.add(new Exception("No runnable methods"));
        }

        public List<Throwable> validateMethodsForDefaultRunner() {
            validateNoArgConstructor();
            validateStaticMethods();
            validateInstanceMethods();
            return fErrors;
        }

        public void validateNoArgConstructor() {
            try {
                fTestClass.getConstructor();
            } catch (Exception e) {
                fErrors.add(new Exception("Test class should have public zero-argument constructor", e));
            }
        }

        public void validateStaticMethods() {
            validateTestMethods(BeforeClass.class, true);
            validateTestMethods(AfterClass.class, true);
        }

        private void validateTestMethods(Class<? extends Annotation> annotation, boolean isStatic) {
            List<Method> methods = fTestClass.getAnnotatedMethods(annotation);

            for (Method each : methods) {
                if (Modifier.isStatic(each.getModifiers()) != isStatic) {
                    String state = isStatic ? "should" : "should not";
                    fErrors.add(new Exception("Method " + each.getName() + "() " + state + " be static"));
                }
                if (!Modifier.isPublic(each.getDeclaringClass().getModifiers()))
                    fErrors.add(new Exception("Class " + each.getDeclaringClass().getName()
                            + " should be public"));
                if (!Modifier.isPublic(each.getModifiers()))
                    fErrors.add(new Exception("Method " + each.getName() + " should be public"));
                if (each.getReturnType() != Void.TYPE)
                    fErrors.add(new Exception("Method " + each.getName() + " should be void"));
                if (each.getParameterTypes().length != 0)
                    fErrors.add(new Exception("Method " + each.getName() + " should have no parameters"));
            }
        }
    }

    private DirectoryTestReader reader;

    public GwtSpring2CsvJUnit4ClassRunner(Class<?> klass) throws Exception {
        super(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(new GwtRunListener());
        GwtConfig.get().setupGwtModule(hackyGetTestClass().getJavaClass());
        super.run(notifier);
    }

    @Override
    protected Object createTest() throws Exception {
        Object testInstance = reader.createObject();
        GwtReflectionUtils.callPrivateMethod(testInstance, "setReader", reader);
        getTestContextManager().prepareTestInstance(testInstance);
        return testInstance;
    }

    protected List<Method> getTestMethods() {
        if (reader == null) {
            reader = new DirectoryTestReader(hackyGetTestClass().getJavaClass());
        }
        return reader.getTestMethods();
    }

    protected void validate() throws InitializationError {
        CsvMethodValidator methodValidator = new CsvMethodValidator(hackyGetTestClass());
        methodValidator.validateMethodsForDefaultRunner();
        methodValidator.assertValid();
    }

    /*
     * Hack for JUnit 4.4 or lower
     */
    private TestClass hackyGetTestClass() {
        return GwtReflectionUtils.callPrivateMethod(this, "getTestClass");
    }

}
