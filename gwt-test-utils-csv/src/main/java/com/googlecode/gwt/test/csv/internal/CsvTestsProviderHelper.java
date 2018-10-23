package com.googlecode.gwt.test.csv.internal;

import static org.assertj.core.util.Strings.isNullOrEmpty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.googlecode.gwt.test.internal.GwtFactory;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.googlecode.gwt.test.csv.CsvMacros;
import com.googlecode.gwt.test.internal.GwtClassLoader;
import com.googlecode.gwt.test.internal.GwtClassPool;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

class CsvTestsProviderHelper {
    private static PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver(
            CsvTestsProviderHelper.class.getClassLoader());

    static class CsvGeneration {

        final Class<?> generatedTestClass;
        final List<Method> generatedTestMethods;

        private CsvGeneration(Class<?> generatedTestClass, List<Method> generatedTestMethods) {
            this.generatedTestClass = generatedTestClass;
            this.generatedTestMethods = generatedTestMethods;
        }
    }

    static Map<String, List<List<String>>> collectCsvMacros(CsvMacros csvMacros) throws FileNotFoundException, IOException {

        if (csvMacros == null) {
            return new HashMap<String, List<List<String>>>();
        }

        return CsvTestsProviderHelper.collectMacros(csvMacros.value(), csvMacros.pattern());
    }

    static Map<String, List<List<String>>> collectCsvTests(String path, String extension) throws IOException {
        return collectCsvTests(path, extension, null);
    }

    static Map<String, List<List<String>>> collectCsvTests(String path, String extension, String tag) throws IOException {
        Map<String, List<List<String>>> tests = new HashMap<String, List<List<String>>>();
        collectCsvTests(path, extension, tag, tests);
        return tests;
    }

    static void collectCsvTests(String path, String extension, String tag, Map<String, List<List<String>>> tests) throws IOException {
        if (path.endsWith(extension)) {
            List<List<String>> csv = CsvReader.readCsv(new InputStreamReader(getResource(path).getInputStream()));
            if (isNullOrEmpty(tag) || csvContainsTag(csv, tag)) {
                tests.put(path, csv);
            }
        } else {
            Resource[] fileResources = getResources(path, extension);
            for (int i = 0; i < fileResources.length; i++) {
                collectCsvTests(fileResources[i].getURL().getPath(), extension, tag, tests);

            }
        }
    }

    private static boolean csvContainsTag(List<List<String>> csv, String tag) {
        for (List<String> csvLine : csv) {
            if (csvLine.contains(tag)) {
                return true;
            }
        }
        return false;
    }

    static void collectCsvTests(Resource resource, String extension, Map<String, List<List<String>>> tests) throws IOException {
        if (resource.getURL().getPath().endsWith(extension)) {
            tests.put(resource.getURL().getPath(), CsvReader.readCsv(new InputStreamReader(resource.getInputStream())));
        } else {
            Resource[] fileResources = getResources(resource.getURL().getPath(), extension);
            for (int i = 0; i < fileResources.length; i++) {
                collectCsvTests(fileResources[i], extension, tests);

            }
        }
    }

    static Map<String, List<List<String>>> collectCsvTests(Resource file, String extension) throws IOException {
        Map<String, List<List<String>>> tests = new HashMap<String, List<List<String>>>();
        collectCsvTests(file, extension, tests);
        return tests;
    }

    static Map<String, List<List<String>>> collectCsvTests(List<String> explicitCsvList) throws IOException {
        Map<String, List<List<String>>> tests = new HashMap<String, List<List<String>>>();
        collectCsvTests(explicitCsvList, tests);
        return tests;
    }

    private static void collectCsvTests(List<String> explicitCsvList, Map<String, List<List<String>>> tests) throws IOException {
        for (String fullPath : explicitCsvList) {
            Resource file = getResource(fullPath);
            tests.put(file.getURL().getPath(), CsvReader.readCsv(new InputStreamReader(file.getInputStream())));
        }
    }

    static Map<String, List<List<String>>> collectMacros(String macrosDirectoryPath, String csvMacroPattern) throws IOException {
        Map<String, List<List<String>>> macros = new HashMap<String, List<List<String>>>();

        Pattern macroNamePattern = csvMacroPattern != null ? Pattern.compile(csvMacroPattern) : null;
        Resource[] fileResources = getResources(macrosDirectoryPath);
        for (int i = 0; i < fileResources.length; i++) {
            String fileName = fileResources[i].getFilename();
            if (macroNamePattern == null || macroNamePattern.matcher(fileName).matches()) {
                Reader reader = new InputStreamReader(fileResources[i].getInputStream());
                List<List<String>> sheet = CsvReader.readCsv(reader);
                macros.put(fileName, sheet);
            }

        }

        return macros;
    }

    static CsvGeneration generateCsvTestClass(Class<?> clazz, Map<String, List<List<String>>> tests, String csvExtension) throws Exception {
        List<Method> generatedTestMethods = new ArrayList<Method>();

        CtClass newClazz = GwtClassPool.get().makeClass(clazz.getName() + ".generated" + System.nanoTime());
        newClazz.setSuperclass(GwtClassPool.getCtClass(clazz));
        List<String> methodList = new ArrayList<String>();
        for (Entry<String, List<List<String>>> entry : tests.entrySet()) {
            String fileAbsolutePath = entry.getKey();

            CtMethod m = createTestMethod(newClazz, fileAbsolutePath, csvExtension);
            newClazz.addMethod(m);
            methodList.add(m.getName());

        }
        Class<?> generatedTestClass = newClazz.toClass(GwtFactory.get().getClassLoader(), null);
        for (String methodName : methodList) {
            Method m = generatedTestClass.getMethod(methodName);
            generatedTestMethods.add(m);
        }

        return new CsvGeneration(generatedTestClass, generatedTestMethods);
    }

    static Resource getResource(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            return new FileSystemResource(file);
        } else {
            Resource resource = pathMatchingResourcePatternResolver.getResource(path);
            if (resource.exists()) {
                return resource;
            }
            throw new FileNotFoundException("Resource  [" + path + "] does not exist");
        }
    }

    private static Resource[] getResources(String path, String extention) throws IOException {

        File directory = new File(path);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            List<Resource> resources = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(extention) || files[i].isDirectory())
                    resources.add(new FileSystemResource(files[i]));
            }
            return resources.toArray(new Resource[0]);
        } else {
            Resource directoryResource = pathMatchingResourcePatternResolver.getResource(path);
            if (directoryResource.exists()) {
                return filter(pathMatchingResourcePatternResolver.getResources(path.endsWith("/") ? path + "*" : path + "/*"), extention);
            }
            throw new FileNotFoundException("Resource Directory [" + path + "] does not exist");
        }
    }

    private static Resource[] filter(Resource[] resources, String extention) {
        List<Resource> list = new ArrayList<>();
        for (int i = 0; i < resources.length; i++) {
            if (resources[i].getFilename().endsWith(extention) || isDirectoryResource(resources[i])) {
                list.add(resources[i]);
            }
        }

        return list.toArray(new Resource[0]);
    }

    private static boolean isDirectoryResource(Resource resource) {
        return resource.getFilename().split(".").length == 0;
    }

    private static Resource[] getResources(String path) throws IOException {
        return getResources(path, "");
    }

    private static CtMethod createTestMethod(CtClass newClass, String fileAbsolutePath, String csvExtension) throws CannotCompileException {
        String methodName = getTestName(fileAbsolutePath, csvExtension);
        CtMethod m = new CtMethod(CtClass.voidType, methodName, new CtClass[0], newClass);
        m.setBody("launchTest(\"" + StringEscapeUtils.escapeJava(fileAbsolutePath) + "\");");

        return m;
    }

    static String getTestName(String csvTestFileAbsolutePath, String csvExtension) {
        String fileName = new File(csvTestFileAbsolutePath).getName();
        String nameWithoutExtension = fileName.substring(0, fileName.length() - csvExtension.length());
        return nameWithoutExtension;
    }

}
