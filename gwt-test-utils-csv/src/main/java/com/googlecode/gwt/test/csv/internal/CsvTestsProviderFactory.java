package com.googlecode.gwt.test.csv.internal;

import java.lang.annotation.Annotation;

import com.googlecode.gwt.test.internal.GwtFactory;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.googlecode.gwt.test.csv.GwtTestCsvException;
import com.googlecode.gwt.test.csv.NoRegression;
import com.googlecode.gwt.test.internal.GwtClassLoader;

public class CsvTestsProviderFactory {

    public static CsvTestsProvider create(BlockJUnit4ClassRunner runner) {
        String csvFile = System.getProperty("gwt.test.csvFile");
        String javaClass = System.getProperty("gwt.test.javaClass");
        if (csvFile != null && javaClass != null) {
            return createCommandLineProvider(csvFile, javaClass);
        }

        if (isInNoRegressionScope(runner)) {
            return createNoRegressionTestprovider(runner);
        }

        String csvListFile = System.getProperty("gwt.test.csvListFile");
        if (csvListFile != null) {
            return createListProvider(runner, csvListFile);
        }

        return createDefaultProvider(runner);

    }

    private static CsvTestsProvider createCommandLineProvider(String csvFile, String javaClass) {
        try {
            return new ParameterCsvTestsProvider(GwtFactory.get().getClassLoader().loadClass(javaClass), csvFile);
        } catch (ClassNotFoundException e) {
            throw new GwtTestCsvException(e);
        }
    }

    private static CsvTestsProvider createListProvider(BlockJUnit4ClassRunner runner, String csvListFile) {
        return new ListCsvTestsProvider(runner.getTestClass().getJavaClass(), csvListFile);
    }

    private static CsvTestsProvider createDefaultProvider(BlockJUnit4ClassRunner runner) {
        return new DirectoryCsvTestsProvider(runner.getTestClass().getJavaClass());
    }

    private static CsvTestsProvider createNoRegressionTestprovider(BlockJUnit4ClassRunner runner) {
        return new NoRegressionTestProvider(runner.getTestClass().getJavaClass());
    }

    private static boolean isInNoRegressionScope(BlockJUnit4ClassRunner runner) {
        Annotation[] testClassAnnotations = runner.getTestClass().getJavaClass().getAnnotations();
        for (Annotation annotation : testClassAnnotations) {
            if (annotation.annotationType().getName().equals(NoRegression.class.getName())) {
                return true;
            }
        }
        return false;
    }

}
