package com.googlecode.gwt.test.csv.runner;

import static com.googlecode.gwt.test.finder.GwtFinder.object;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Fail.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.event.shared.UmbrellaException;
import com.googlecode.gwt.test.csv.CsvMethod;
import com.googlecode.gwt.test.csv.GwtCsvTest;
import com.googlecode.gwt.test.csv.GwtTestCsvException;
import com.googlecode.gwt.test.finder.GwtFinder;
import com.googlecode.gwt.test.finder.GwtInstance;
import com.googlecode.gwt.test.finder.Node;
import com.googlecode.gwt.test.finder.ObjectFinder;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

public class CsvRunner {

   private static Map<Class<?>, Map<String, Method>> csvMethodsCache = new HashMap<Class<?>, Map<String, Method>>();

   private static final Logger logger = LoggerFactory.getLogger(CsvRunner.class);

   private String extendedLineInfo = null;

   private int lineNumber = -1;

   private final List<ObjectFinder> objectFinders = new ArrayList<ObjectFinder>();

   public void addObjectFinder(ObjectFinder objectFinder) {
      objectFinders.add(objectFinder);
   }

   public void executeLine(String methodName, List<String> args, Object fixture)
            throws CsvRunnerException {
      if (methodName.indexOf("**") == 0) {
         // commented line
         return;
      }
      List<String> filterArgs = new ArrayList<String>(args);
      removeEmptyElements(filterArgs);
      transformArgs(filterArgs);
      Method m = getCsvMethod(fixture.getClass(), methodName);
      if (m == null) {
         fail(getAssertionErrorMessagePrefix() + "@" + CsvMethod.class.getSimpleName() + " ' "
                  + methodName + " ' not found in object " + fixture);
      }
      GwtReflectionUtils.makeAccessible(m);
      logger.debug(getProcessingMessagePrefix() + "Executing " + methodName + ", params "
               + Arrays.toString(filterArgs.toArray()));
      List<Object> argList = new ArrayList<Object>();
      for (Class<?> clazz : m.getParameterTypes()) {
         if (filterArgs.size() == 0) {
            if (clazz.isArray()) {
               argList.add(new String[]{});
            } else {
               fail(getAssertionErrorMessagePrefix() + "Too few args for @"
                        + CsvMethod.class.getSimpleName() + " '" + methodName + "'");
            }
         } else {
            if (clazz.isArray()) {
               argList.add(filterArgs.toArray(new String[]{}));
               filterArgs.clear();
            } else {
               argList.add(filterArgs.get(0));
               filterArgs.remove(0);
            }
         }
      }
      if (filterArgs.size() != 0) {
         fail(getAssertionErrorMessagePrefix() + "Too many args for " + methodName);
      }
      try {
         Object[] finalArgList = argList.toArray(new Object[]{});
         m.invoke(fixture, finalArgList);
      } catch (InvocationTargetException e) {
         if (e.getCause() != null) {
            if (e.getCause() instanceof AssertionError) {
               throw (AssertionError) e.getCause();
            } else if (e.getCause() != null && e.getCause().getCause() instanceof AssertionError) {
               throw (AssertionError) e.getCause().getCause();
            }
            throw createCsvExecutionException(e.getCause());
         }
         logger.error(getAssertionErrorMessagePrefix() + "Execution error", e);
         fail(getAssertionErrorMessagePrefix() + "Error invoking " + methodName + " in fixture : "
                  + e.toString());
      } catch (Exception e) {
         logger.error(getAssertionErrorMessagePrefix() + "Execution error", e);
         fail(getAssertionErrorMessagePrefix() + "Error invoking " + methodName + " in fixture : "
                  + e.toString());
      }
   }

   public void executeRow(List<String> row, Object fixture) throws CsvRunnerException {
      if (row.size() == 0) {
         return;
      }
      String methodName = row.get(0).trim();
      if (!"".equals(methodName)) {
         List<String> args = new ArrayList<String>();
         args.addAll(row);
         args.remove(0);
         executeLine(methodName, args, fixture);
      }
   }

   public String getAssertionErrorMessagePrefix() {
      return "Error line " + (lineNumber + 1)
               + (extendedLineInfo == null ? "" : " [" + extendedLineInfo + "]") + ": ";
   }

   @SuppressWarnings("unchecked")
   public <T> T getNodeValue(Object o, Node node) {
      Object current = o;
      Node currentNode = node;
      while (currentNode != null) {
         if (current == null) {
            return null;
         }
         String currentName = currentNode.getLabel();
         boolean mapEqIsProcessed = false;
         logger.trace(getProcessingMessagePrefix() + "Processing " + currentName);
         boolean ok = false;
         if (!ok) {
            Method m = null;
            if (m == null) {
               m = GwtReflectionUtils.getMethod(current.getClass(), currentName);
            }
            if (m == null) {
               m = GwtReflectionUtils.getMethod(current.getClass(), "get" + currentName);
            }
            if (m != null) {
               try {
                  if (m.getParameterTypes().length == 0 || currentNode.getParams() != null) {
                     current = invoke(current, m, currentNode.getParams());
                     ok = true;
                  } else {
                     current = findInList(current, m, currentNode.getMapEq(), currentNode.getMap());
                     mapEqIsProcessed = true;
                     ok = true;
                  }

               } catch (Exception e) {
                  logger.error(getAssertionErrorMessagePrefix() + "Execution error", e);
                  fail(getAssertionErrorMessagePrefix() + "Unable to get method result on "
                           + o.getClass().getCanonicalName() + ", method " + m.getName()
                           + ", params " + currentNode.getParams());
               }
            }
         }
         if (!ok) {
            Field f = getField(current, current.getClass(), currentName);
            if (f != null) {
               try {
                  current = f.get(current);
                  ok = true;
               } catch (Exception e) {
                  logger.error(getAssertionErrorMessagePrefix() + "Execution error", e);
                  fail(getAssertionErrorMessagePrefix() + "Unable to get field value on "
                           + o.getClass().getCanonicalName() + ", field " + f.getName()
                           + ", params " + node);
               }
            }
         }
         if (ok && currentNode.getMap() != null) {
            if (currentNode.getMapEq() == null) {
               current = proccessMap(current, currentNode.getMap());
            } else {
               if (!mapEqIsProcessed) {
                  if (current instanceof Iterable<?>) {
                     Iterable<Object> list = (Iterable<Object>) current;
                     current = findInIterable(list, currentNode.getMapEq(), currentNode.getMap(),
                              current, null);
                  } else {
                     fail(getAssertionErrorMessagePrefix() + "Not managed type for iteration "
                              + current.getClass().getCanonicalName());
                  }
               }
            }
         }
         if (!ok) {
            return null;
         }
         currentNode = currentNode.getNext();
      }

      return (T) current;
   }

   /**
    * 
    * @param clazz
    * @param failOnError
    * @param params
    * @return The found object.
    * 
    * @deprecated use {@link GwtFinder#object(String...)} instead
    */
   @Deprecated
   @SuppressWarnings("unchecked")
   public <T> T getObject(Class<T> clazz, boolean failOnError, String... params) {

      if (failOnError) {
         return object(params).ofType(clazz);
      } else {
         GwtInstance gwtInstance = object(params);
         if (clazz.isInstance(gwtInstance.getRaw())) {
            return (T) gwtInstance.getRaw();
         } else {
            return null;
         }
      }
   }

   /**
    * 
    * @param clazz
    * @param params
    * @return The found object.
    * 
    * @deprecated use {@link GwtFinder#object(String...)} instead
    */
   @Deprecated
   public <T> T getObject(Class<T> clazz, String... params) {
      return getObject(clazz, true, params);
   }

   public String getProcessingMessagePrefix() {
      return "Processing line " + (lineNumber + 1)
               + (extendedLineInfo == null ? "" : " [" + extendedLineInfo + "]") + ": ";
   }

   public int runSheet(List<List<String>> sheet, Object fixture) throws Exception {
      assertThat(fixture).as("Fixture have to be not null").isNotNull();
      boolean execute = false;
      int lineExecuted = 0;
      lineNumber = 0;
      for (List<String> row : sheet) {
         if (execute) {
            executeRow(row, fixture);
            lineExecuted++;
         }
         if (row != null && row.size() > 0 && "start".equals(row.get(0))) {
            execute = true;
         }
         lineNumber++;
      }
      lineNumber = -1;
      return lineExecuted;
   }

   public void setExtendedLineInfo(String extendedLineInfo) {
      this.extendedLineInfo = extendedLineInfo;
   }

   private boolean checkCondition(Object n, Node before, String after) {
      Object result = getNodeValue(n, before);
      String s = result == null ? null : result.toString();
      return after.equals(s);
   }

   private CsvRunnerException createCsvExecutionException(Throwable cause) {
      if (cause instanceof CsvRunnerException) {
         return (CsvRunnerException) cause;
      } else if (UmbrellaException.class.isInstance(cause)
               || AssertionError.class.isInstance(cause)) {
         cause = cause.getCause();
      }

      return new CsvRunnerException(this, cause);
   }

   private Object findInIterable(Iterable<Object> list, Node before, String after, Object current,
            Method m) {
      Object found = null;
      for (Object n : list) {
         if (checkCondition(n, before, after)) {
            if (found != null) {
               fail("Not unique object with condition " + before + "=" + after);
            }
            found = n;
         }
      }
      assertThat(found).as(
               getAssertionErrorMessagePrefix() + "Not found " + before + "=" + after + " in "
                        + current.getClass().getCanonicalName()
                        + (m != null ? " method " + m.getName() : "")).isNotNull();

      return found;
   }

   private Object findInList(final Object current, final Method m, Node mapEq, String map)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
      if (m.getParameterTypes().length != 1 && m.getParameterTypes()[0] != int.class) {
         fail("Unable to navigate " + current.getClass().getCanonicalName() + " with method "
                  + m.getName());
      }
      Method countM = GwtReflectionUtils.getMethod(current.getClass(), m.getName() + "Count");
      if (countM == null) {
         fail("Count method not found in " + current.getClass().getCanonicalName() + " method "
                  + m.getName());
      }
      if (countM.getParameterTypes().length > 0) {
         fail("Too many parameter in count method " + current.getClass().getCanonicalName()
                  + " method " + countM.getName());
      }

      logger.debug("Searching in list, field " + mapEq + ", value " + map);
      final int count = (Integer) countM.invoke(current);
      return findInIterable(new Iterable<Object>() {

         public Iterator<Object> iterator() {
            return new Iterator<Object>() {

               int counter = 0;

               public boolean hasNext() {
                  return counter < count;
               }

               public Object next() {
                  try {
                     return m.invoke(current, counter++);
                  } catch (Exception e) {
                     throw new GwtTestCsvException("Iterator exception", e);
                  }
               }

               public void remove() {
                  throw new UnsupportedOperationException("Remove not implemented");
               }

            };
         }

      }, mapEq, map, current, m);
   }

   private Method getCsvMethod(Class<?> clazz, String methodName) {
      Map<String, Method> classCsvMethods = csvMethodsCache.get(clazz);

      if (classCsvMethods == null) {
         classCsvMethods = new HashMap<String, Method>();
         csvMethodsCache.put(clazz, classCsvMethods);

         Map<Method, CsvMethod> csvMethods = GwtReflectionUtils.getAnnotatedMethod(clazz,
                  CsvMethod.class);
         for (Map.Entry<Method, CsvMethod> entry : csvMethods.entrySet()) {
            classCsvMethods.put(getMethodName(entry), entry.getKey());
         }

      }
      Method res = classCsvMethods.get(methodName);
      if (res != null) {
         return res;
      } else if (clazz == GwtCsvTest.class) {
         return null;
      } else {
         Class<?> superClass = clazz.getSuperclass();
         if (superClass != null)
            return getCsvMethod(clazz.getSuperclass(), methodName);
         else
            return null;
      }
   }

   private Field getField(Object fixture, Class<?> clazz, String fieldName) {
      for (Field f : clazz.getDeclaredFields()) {
         if (f.getName().equalsIgnoreCase(fieldName)) {
            GwtReflectionUtils.makeAccessible(f);
            return f;
         }
      }
      Class<?> superClazz = clazz.getSuperclass();
      if (superClazz != null) {
         return getField(fixture, superClazz, fieldName);
      }
      return null;
   }

   private String getMethodName(Entry<Method, CsvMethod> entry) {
      String methodname = entry.getValue().methodName();

      return (methodname.equals("")) ? entry.getKey().getName() : methodname;
   }

   private Object invoke(Object current, Method m, List<String> list)
            throws IllegalArgumentException, IllegalAccessException {
      logger.debug("Invoking " + m.getName() + " on " + current.getClass().getCanonicalName()
               + " with param " + list);
      GwtReflectionUtils.makeAccessible(m);
      if (list == null) {
         if (m.getParameterTypes().length == 0) {
            try {
               return m.invoke(current);
            } catch (InvocationTargetException e) {
               return null;
            }
         }
      }
      Object[] tab = new Object[m.getParameterTypes().length];
      for (int index = 0; index < m.getParameterTypes().length; index++) {
         if (m.getParameterTypes()[index] == String.class) {
            tab[index] = list.get(index);
         } else if (m.getParameterTypes()[index] == Integer.class
                  || m.getParameterTypes()[index] == int.class) {
            tab[index] = Integer.parseInt(list.get(index));
         } else {
            fail(getAssertionErrorMessagePrefix() + "Not managed type "
                     + m.getParameterTypes()[index]);
         }
      }
      try {
         return m.invoke(current, tab);
      } catch (InvocationTargetException e) {
         return null;
      }
   }

   private Object proccessMap(Object current, String map) {
      if (current instanceof Map<?, ?>) {
         Map<?, ?> m = (Map<?, ?>) current;
         current = m.get(map);
      } else if (current instanceof List<?>) {
         List<?> l = (List<?>) current;
         current = l.get(Integer.parseInt(map));
      } else {
         fail(getAssertionErrorMessagePrefix() + "Object not a map "
                  + current.getClass().getCanonicalName() + " : " + map);
      }
      return current;
   }

   private void removeEmptyElements(List<String> list) {
      List<String> newList = new ArrayList<String>();
      for (String s : list) {
         if (!"".equals(s)) {
            newList.add(s);
         }
      }
      list.clear();
      list.addAll(newList);
   }

   private void transformArgs(List<String> list) {
      List<String> newList = new ArrayList<String>();
      for (String s : list) {
         String out = s;
         if ("*empty*".equals(s)) {
            out = "";
         } else if ("*null*".equals(s)) {
            out = null;
         }
         newList.add(out);
      }
      list.clear();
      list.addAll(newList);
   }

}
