package com.googlecode.gwt.test.csv.internal;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public interface CsvTestsProvider {

    List<List<String>> getMacroFile(String macroName);

    Set<String> getMacroFileList();

    List<List<String>> getTest(String filePath);

    Set<String> getTestList();

    List<Method> getTestMethods();

    Object newTestClassInstance() throws Exception;

    String getTestName(String csvTestFileAbsolutePath);

}
