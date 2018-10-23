package com.googlecode.gwt.test.csv.internal;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.googlecode.gwt.test.csv.CsvDirectory;
import com.googlecode.gwt.test.csv.CsvMacros;
import com.googlecode.gwt.test.csv.GwtTestCsvException;
import com.googlecode.gwt.test.csv.internal.CsvTestsProviderHelper.CsvGeneration;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

class NoRegressionTestProvider implements CsvTestsProvider {

    private final static String NO_REGRESSION_TAG = "#TNR";

    private final CsvTestsProviderDelegate delegate;

    public NoRegressionTestProvider(Class<?> clazz) {
        try {
            CsvDirectory csvDirectoryAnnotation = GwtReflectionUtils.getAnnotation(clazz, CsvDirectory.class);

            if (csvDirectoryAnnotation == null) {
                throw new GwtTestCsvException(
                        "Missing annotation \'@" + CsvDirectory.class.getSimpleName() + "\' on class [" + clazz.getName() + "]");
            }

            Map<String, List<List<String>>> tests = collectCsvTests(csvDirectoryAnnotation);
            CsvGeneration csvGeneration = CsvTestsProviderHelper.generateCsvTestClass(clazz, tests, csvDirectoryAnnotation.extension());
            CsvMacros csvMacrosAnnotation = GwtReflectionUtils.getAnnotation(clazz, CsvMacros.class);
            Map<String, List<List<String>>> macros = CsvTestsProviderHelper.collectCsvMacros(csvMacrosAnnotation);
            delegate = new CsvTestsProviderDelegate(csvGeneration, tests, macros, csvDirectoryAnnotation.extension());
        } catch (Exception e) {
            if (GwtTestException.class.isInstance(e)) {
                throw (GwtTestException) e;
            }

            throw new GwtTestCsvException(e);
        }
    }

    private Map<String, List<List<String>>> collectCsvTests(CsvDirectory csvDirectoryAnnotation) throws IOException {
        return CsvTestsProviderHelper.collectCsvTests(csvDirectoryAnnotation.value(), csvDirectoryAnnotation.extension(), NO_REGRESSION_TAG);
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
    public Object newTestClassInstance() throws Exception {
        return delegate.newTestClassInstance();
    }

    @Override
    public String getTestName(String csvTestFileAbsolutePath) {
        return delegate.getTestName(csvTestFileAbsolutePath);
    }

}
