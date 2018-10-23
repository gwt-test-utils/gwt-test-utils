package com.googlecode.gwt.test.csv.internal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import com.googlecode.gwt.test.csv.CsvMacros;
import com.googlecode.gwt.test.csv.GwtTestCsvException;
import com.googlecode.gwt.test.csv.internal.CsvTestsProviderHelper.CsvGeneration;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

class ListCsvTestsProvider implements CsvTestsProvider {

    private final static String CSV_EXT = ".csv";
    private final CsvTestsProviderDelegate delegate;

    public ListCsvTestsProvider(Class<?> clazz, String csvListFilePath) {
        try {
            Map<String, List<List<String>>> tests = collectCsvTests(csvListFilePath);

            CsvMacros csvMacrosAnnotation = GwtReflectionUtils.getAnnotation(clazz, CsvMacros.class);
            Map<String, List<List<String>>> macros = CsvTestsProviderHelper.collectCsvMacros(csvMacrosAnnotation);

            CsvGeneration csvGeneration = CsvTestsProviderHelper.generateCsvTestClass(clazz, tests, CSV_EXT);

            delegate = new CsvTestsProviderDelegate(csvGeneration, tests, macros, CSV_EXT);

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

    private Map<String, List<List<String>>> collectCsvTests(String csvListFilePath) throws FileNotFoundException, IOException {
        Resource csvListFile = CsvTestsProviderHelper.getResource(csvListFilePath);
        @SuppressWarnings("unchecked")
        List<String> lines = IOUtils.readLines(csvListFile.getInputStream(), "utf-8");
        return CsvTestsProviderHelper.collectCsvTests(lines);
    }

}
