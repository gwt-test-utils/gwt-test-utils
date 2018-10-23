package com.googlecode.gwt.test.spring;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.gwt.test.internal.GwtFactory;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.googlecode.gwt.test.csv.internal.CsvTestsProvider;
import com.googlecode.gwt.test.csv.internal.CsvTestsProviderFactory;
import com.googlecode.gwt.test.internal.GwtClassLoader;
import com.googlecode.gwt.test.internal.junit.GwtRunListener;

public class GwtSpringCsvRunner extends SpringJUnit4ClassRunner {

    private CsvTestsProvider csvTestsProvider;

    public GwtSpringCsvRunner(Class<?> clazz) throws InitializationError, ClassNotFoundException {
        super(GwtFactory.get().getClassLoader().loadClass(clazz.getCanonicalName()));
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
        List<FrameworkMethod> list = new ArrayList<FrameworkMethod>();
        for (Method csvMethod : csvTestsProvider.getTestMethods()) {
            list.add(new FrameworkMethod(csvMethod));
        }

        return list;
    }

    @Override
    protected Object createTest() throws Exception {
        Object testInstance = csvTestsProvider.newTestClassInstance();
        getTestContextManager().prepareTestInstance(testInstance);
        return testInstance;
    }

}
