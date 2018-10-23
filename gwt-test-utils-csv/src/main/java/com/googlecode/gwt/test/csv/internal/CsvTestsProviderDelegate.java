package com.googlecode.gwt.test.csv.internal;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.googlecode.gwt.test.csv.internal.CsvTestsProviderHelper.CsvGeneration;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

class CsvTestsProviderDelegate implements CsvTestsProvider {

    private final Class<?> generatedClazz;
    private final Map<String, List<List<String>>> macros;
    private final List<Method> testMethods;
    private final Map<String, List<List<String>>> tests;
    private final String csvExtension;

    CsvTestsProviderDelegate(CsvGeneration csvGeneration, Map<String, List<List<String>>> tests, Map<String, List<List<String>>> macros,
                             String csvExtension) {

        this.generatedClazz = csvGeneration.generatedTestClass;
        this.testMethods = csvGeneration.generatedTestMethods;
        this.tests = tests;
        this.macros = macros;
        this.csvExtension = csvExtension;
    }

    @Override
    public List<List<String>> getMacroFile(String macroName) {
        return macros.get(macroName);
    }

    @Override
    public Set<String> getMacroFileList() {
        return Collections.unmodifiableSet(macros.keySet());
    }

    @Override
    public List<List<String>> getTest(String filePath) {
        return tests.get(filePath);
    }

    @Override
    public Set<String> getTestList() {
        return Collections.unmodifiableSet(tests.keySet());
    }

    @Override
    public List<Method> getTestMethods() {
        Collections.sort(testMethods, new Comparator<Method>() {

            @Override
            public int compare(Method o1, Method o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return testMethods;
    }

    @Override
    public String getTestName(String csvTestFileAbsolutePath) {
        return CsvTestsProviderHelper.getTestName(csvTestFileAbsolutePath, csvExtension);
    }

    @Override
    public Object newTestClassInstance() throws Exception {
        Object testInstance = generatedClazz.newInstance();
        GwtReflectionUtils.callPrivateMethod(testInstance, "setReader", this);
        return testInstance;
    }

}
