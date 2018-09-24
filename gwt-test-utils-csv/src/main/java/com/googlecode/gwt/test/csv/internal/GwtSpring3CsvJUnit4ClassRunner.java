package com.googlecode.gwt.test.csv.internal;

import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.internal.junit.GwtRunListener;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GwtSpring3CsvJUnit4ClassRunner extends SpringJUnit4ClassRunner {

    private DirectoryTestReader reader;

    public GwtSpring3CsvJUnit4ClassRunner(Class<?> klass) throws InitializationError,
            ClassNotFoundException {
        super(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(new GwtRunListener());
        GwtConfig.get().setupGwtModule(getTestClass().getJavaClass());
        super.run(notifier);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if (reader == null) {
            reader = new DirectoryTestReader(getTestClass().getJavaClass());
        }
        List<FrameworkMethod> frameworkMethods = new ArrayList<>();
        for (Method csvMethod : reader.getTestMethods()) {
            frameworkMethods.add(new FrameworkMethod(csvMethod));
        }

        return frameworkMethods;
    }

    @Override
    protected Object createTest() throws Exception {
        Object testInstance = reader.createObject();
        GwtReflectionUtils.callPrivateMethod(testInstance, "setReader", reader);
        getTestContextManager().prepareTestInstance(testInstance);
        return testInstance;
    }

}
