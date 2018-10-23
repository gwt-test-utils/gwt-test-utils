package com.googlecode.gwt.test.csv.internal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.googlecode.gwt.test.csv.CsvDirectory;
import com.googlecode.gwt.test.csv.CsvMacros;
import com.googlecode.gwt.test.csv.GwtTestCsvException;
import com.googlecode.gwt.test.csv.internal.CsvTestsProviderHelper.CsvGeneration;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

class DirectoryCsvTestsProvider implements CsvTestsProvider {

    private final CsvTestsProviderDelegate delegate;

    public DirectoryCsvTestsProvider(Class<?> clazz) {
        try {
            CsvDirectory csvDirectoryAnnotation = GwtReflectionUtils.getAnnotation(clazz, CsvDirectory.class);

            if (csvDirectoryAnnotation == null) {
                throw new GwtTestCsvException(
                        "Missing annotation \'@" + CsvDirectory.class.getSimpleName() + "\' on class [" + clazz.getName() + "]");
            }

            Map<String, List<List<String>>> tests = collectCsvTests(csvDirectoryAnnotation);

            CsvMacros csvMacrosAnnotation = GwtReflectionUtils.getAnnotation(clazz, CsvMacros.class);
            Map<String, List<List<String>>> macros = CsvTestsProviderHelper.collectCsvMacros(csvMacrosAnnotation);

            CsvGeneration csvGeneration = CsvTestsProviderHelper.generateCsvTestClass(clazz, tests, csvDirectoryAnnotation.extension());

            delegate = new CsvTestsProviderDelegate(csvGeneration, tests, macros, csvDirectoryAnnotation.extension());

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

    private Map<String, List<List<String>>> collectCsvTests(CsvDirectory csvDirectory) throws FileNotFoundException, IOException {
        return CsvTestsProviderHelper.collectCsvTests(csvDirectory.value(), csvDirectory.extension());
    }

}
