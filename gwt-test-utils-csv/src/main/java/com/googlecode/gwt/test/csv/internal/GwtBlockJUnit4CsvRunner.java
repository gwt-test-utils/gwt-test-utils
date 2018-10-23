package com.googlecode.gwt.test.csv.internal;

import com.googlecode.gwt.test.internal.junit.GwtBlockJUnit4ClassRunner;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gael Lazzari
 */
public class GwtBlockJUnit4CsvRunner extends GwtBlockJUnit4ClassRunner {

    private CsvTestsProvider csvTestsProvider;

    public GwtBlockJUnit4CsvRunner(Class<?> clazz) throws InitializationError,
            ClassNotFoundException {
        super(clazz);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if (csvTestsProvider == null) {
            csvTestsProvider = CsvTestsProviderFactory.create(this);
        }
        List<FrameworkMethod> frameworkMethods = new ArrayList<>();
        for (Method csvMethod : csvTestsProvider.getTestMethods()) {
            frameworkMethods.add(new FrameworkMethod(csvMethod));
        }

        return frameworkMethods;
    }

    @Override
    protected Object createTest() throws Exception {
        Object testInstance = csvTestsProvider.newTestClassInstance();
        return testInstance;
    }

}
