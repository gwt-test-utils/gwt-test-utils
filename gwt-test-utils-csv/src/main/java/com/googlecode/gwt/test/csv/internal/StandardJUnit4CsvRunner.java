package com.googlecode.gwt.test.csv.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.gwt.test.internal.GwtFactory;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import com.googlecode.gwt.test.internal.GwtClassLoader;
import com.googlecode.gwt.test.internal.junit.GwtRunListener;

public class StandardJUnit4CsvRunner extends BlockJUnit4ClassRunner {

    private CsvTestsProvider csvTestsProvider;

    public StandardJUnit4CsvRunner(Class<?> clazz) throws InitializationError,
            ClassNotFoundException {
        super(GwtFactory.get().getClassLoader().loadClass(clazz.getName()));
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(new GwtRunListener());
        super.run(notifier);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if (csvTestsProvider == null) {
            csvTestsProvider = CsvTestsProviderFactory.create(this);
        }
        List<FrameworkMethod> frameworkMethods = new ArrayList<FrameworkMethod>();
        for (Method m : csvTestsProvider.getTestMethods()) {
            frameworkMethods.add(new FrameworkMethod(m));
        }
        return frameworkMethods;
    }

    @Override
    protected Object createTest() throws Exception {
        Object testInstance = csvTestsProvider.newTestClassInstance();
        return testInstance;
    }

}
