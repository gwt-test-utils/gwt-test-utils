package com.googlecode.gwt.test.csv.internal;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.googlecode.gwt.test.csv.CsvMacros;
import com.googlecode.gwt.test.csv.GwtTestCsvException;
import com.googlecode.gwt.test.csv.internal.CsvTestsProviderHelper.CsvGeneration;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

class ParameterCsvTestsProvider implements CsvTestsProvider {

    private final CsvTestsProviderDelegate delegate;

    public ParameterCsvTestsProvider(Class<?> baseTestClass, String csvTestPath) {
        this(baseTestClass, csvTestPath, ".csv", GwtReflectionUtils.getAnnotation(baseTestClass, CsvMacros.class).value(), ".+\\..*");
    }

    public ParameterCsvTestsProvider(Class<?> baseTestClass, String csvTestPath, String csvExtension, String macroDirectory, String csvMacroPattern) {
        try {

            Map<String, List<List<String>>> tests = CsvTestsProviderHelper.collectCsvTests(csvTestPath, csvExtension);
            Map<String, List<List<String>>> macros = CsvTestsProviderHelper.collectMacros(macroDirectory, csvMacroPattern);

            CsvGeneration csvGeneration = CsvTestsProviderHelper.generateCsvTestClass(baseTestClass, tests, csvExtension);

            delegate = new CsvTestsProviderDelegate(csvGeneration, tests, macros, csvExtension);

        } catch (Exception e) {
            if (GwtTestException.class.isInstance(e)) {
                throw (GwtTestException) e;
            }

            throw new GwtTestCsvException(e);
        }
    }

    @Override
    public List<List<String>> getMacroFile(String macroName) {
        return delegate.getMacroFile(macroName);
    }

    @Override
    public Set<String> getMacroFileList() {
        return delegate.getMacroFileList();
    }

    @Override
    public List<List<String>> getTest(String filePath) {
        return delegate.getTest(filePath);
    }

    @Override
    public Set<String> getTestList() {
        return delegate.getTestList();
    }

    @Override
    public List<Method> getTestMethods() {
        return delegate.getTestMethods();
    }

    @Override
    public String getTestName(String csvTestFileAbsolutePath) {
        return delegate.getTestName(csvTestFileAbsolutePath);
    }

    @Override
    public Object newTestClassInstance() throws Exception {
        return delegate.newTestClassInstance();
    }

}
