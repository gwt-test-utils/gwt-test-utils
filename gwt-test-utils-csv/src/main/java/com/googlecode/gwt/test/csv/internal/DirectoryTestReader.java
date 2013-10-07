package com.googlecode.gwt.test.csv.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javassist.CtClass;
import javassist.CtMethod;

import org.apache.commons.lang.StringEscapeUtils;

import com.googlecode.gwt.test.csv.CsvDirectory;
import com.googlecode.gwt.test.csv.CsvMacros;
import com.googlecode.gwt.test.csv.GwtTestCsvException;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.internal.GwtClassPool;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

public class DirectoryTestReader {

   static class CsvReader {

      private static final Pattern REPLACE_PATTERN = Pattern.compile("\\\\;");

      private static final String SEPARATOR = ";";
      /**
       * any ';' character which has not been escaped with a '\'
       */
      private static final Pattern SEPARATOR_PATTERN = Pattern.compile("(?<!\\\\);");

      public static List<List<String>> readCsv(Reader reader) throws IOException {
         BufferedReader br = new BufferedReader(reader);
         List<List<String>> l = new ArrayList<List<String>>();
         while (true) {
            String line = br.readLine();
            if (line == null) {
               break;
            }
            line = new String(line.getBytes(), "UTF-8").trim();
            if ("".equals(line)) {
               l.add(new ArrayList<String>());
            } else if (!line.startsWith("//")) {
               String[] tab = SEPARATOR_PATTERN.split(line);
               l.add(treatParams(tab));
            }
         }
         return l;
      }

      private static List<String> treatParams(String[] csvParams) {
         List<String> list = new ArrayList<String>(csvParams.length);
         for (String csvParam : csvParams) {
            list.add(REPLACE_PATTERN.matcher(csvParam).replaceAll(SEPARATOR));
         }

         return list;
      }
   }

   private static File getDirectory(String path) throws IOException {
      File directory = new File(path);
      if (!directory.exists()) {
         throw new FileNotFoundException("Directory [" + path + "] does not exist");
      } else if (!directory.isDirectory()) {
         throw new IOException("A directory was expected for path [" + path
                  + "] but a file has been found");
      }

      return directory;
   }

   private Class<?> generatedClazz;

   private Map<String, List<List<String>>> macros;

   private List<Method> testMethods;

   private Map<String, List<List<String>>> tests;

   public DirectoryTestReader(Class<?> clazz) {
      try {
         CsvDirectory csvDirectoryAnnotation = GwtReflectionUtils.getAnnotation(clazz,
                  CsvDirectory.class);

         if (csvDirectoryAnnotation == null) {
            throw new GwtTestCsvException("Missing annotation \'@"
                     + CsvDirectory.class.getSimpleName() + "\' on class [" + clazz.getName() + "]");
         }

         initCsvTests(csvDirectoryAnnotation);

         CsvMacros csvMacrosAnnotation = GwtReflectionUtils.getAnnotation(clazz, CsvMacros.class);
         initCsvMacros(csvMacrosAnnotation);

         initTestMethods(clazz, csvDirectoryAnnotation);
      } catch (Exception e) {
         if (GwtTestException.class.isInstance(e)) {
            throw (GwtTestException) e;
         }

         throw new GwtTestCsvException(e);
      }
   }

   public Object createObject() throws InstantiationException, IllegalAccessException {
      return generatedClazz.newInstance();
   }

   public List<List<String>> getMacroFile(String macroName) {
      return macros.get(macroName);
   }

   public Set<String> getMacroFileList() {
      return Collections.unmodifiableSet(macros.keySet());
   }

   public List<List<String>> getTest(String filePath) {
      return tests.get(filePath);
   }

   public Set<String> getTestList() {
      return Collections.unmodifiableSet(tests.keySet());
   }

   public List<Method> getTestMethods() {
      Collections.sort(testMethods, new Comparator<Method>() {

         public int compare(Method o1, Method o2) {
            return o1.getName().compareTo(o2.getName());
         }
      });

      return testMethods;
   }

   private void collectCsvTests(File directory, String extension,
            Map<String, List<List<String>>> tests) throws IOException {
      for (File f : directory.listFiles()) {
         if (f.getName().endsWith(extension)) {
            tests.put(f.getAbsolutePath(), CsvReader.readCsv(new FileReader(f)));
         } else if (f.isDirectory()) {
            collectCsvTests(f, extension, tests);
         }
      }
   }

   private String getTestName(String csvTestFileAbsolutePath, String csvExtension) {
      String fileName = new File(csvTestFileAbsolutePath).getName();
      String nameWithoutExtension = fileName.substring(0, fileName.length() - csvExtension.length());
      return nameWithoutExtension;
   }

   private void initCsvMacros(CsvMacros csvMacros) throws FileNotFoundException, IOException {
      macros = new HashMap<String, List<List<String>>>();

      if (csvMacros == null) {
         return;
      }

      Pattern macroNamePattern = csvMacros.pattern() != null ? Pattern.compile(csvMacros.pattern())
               : null;
      File macrosDirectory = getDirectory(csvMacros.value());

      for (File file : macrosDirectory.listFiles()) {
         String fileName = file.getName();
         if (macroNamePattern == null || macroNamePattern.matcher(file.getName()).matches()) {
            FileReader reader = new FileReader(file);
            List<List<String>> sheet = CsvReader.readCsv(reader);
            macros.put(fileName, sheet);
         }
      }
   }

   private void initCsvTests(CsvDirectory csvDirectory) throws FileNotFoundException, IOException {
      File testsRoot = getDirectory(csvDirectory.value());
      String extension = csvDirectory.extension();

      testMethods = new ArrayList<Method>();

      tests = new HashMap<String, List<List<String>>>();
      collectCsvTests(testsRoot, extension, tests);
   }

   private void initTestMethods(Class<?> clazz, CsvDirectory csvDirectory) throws Exception {
      testMethods = new ArrayList<Method>();

      CtClass newClazz = GwtClassPool.get().makeClass(
               clazz.getName() + ".generated" + System.nanoTime());
      newClazz.setSuperclass(GwtClassPool.getCtClass(clazz));
      List<String> methodList = new ArrayList<String>();
      for (Entry<String, List<List<String>>> entry : tests.entrySet()) {
         String fileAbsolutePath = entry.getKey();

         String methodName = getTestName(fileAbsolutePath, csvDirectory.extension());
         CtMethod m = new CtMethod(CtClass.voidType, methodName, new CtClass[0], newClazz);
         methodList.add(methodName);
         m.setBody("launchTest(\"" + StringEscapeUtils.escapeJava(fileAbsolutePath) + "\");");
         newClazz.addMethod(m);
      }
      generatedClazz = newClazz.toClass(getClass().getClassLoader(), null);
      for (String methodName : methodList) {
         Method m = generatedClazz.getMethod(methodName);
         testMethods.add(m);
      }
   }

}
