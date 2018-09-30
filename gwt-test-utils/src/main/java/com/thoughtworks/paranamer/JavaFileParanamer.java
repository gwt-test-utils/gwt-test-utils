package com.thoughtworks.paranamer;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;

import java.io.InputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .java parser implementation of Paranamer. It relies on <a
 * href="https://javaparser.org/">javaparser-core library</a> 3.6.23+ to parse java files
 * retrieved by a {@link JavaFileFinder} implementation.
 *
 * @author Gael Lazzari
 */
public class JavaFileParanamer implements Paranamer {

    /**
     * Interface in charge of retrieving .java file where a specific {@link Method} or
     * {@link Constructor} is declared.
     *
     * @author Gael Lazzari
     */
    public interface JavaFileFinder {

        /**
         * Retrieve and open the .java file where the specified {@link Method} or {@link Constructor}
         * is declared
         *
         * @param methodOrConstructor the {@link Method} or {@link Constructor} for which the
         *                            parameter names are looked up.
         * @return an opened input stream on the corresponding .java file, or null if not found.
         */
        InputStream openJavaFile(AccessibleObject methodOrConstructor);

    }

    /**
     * Paranamer internal implementation of javaparser {@link GenericVisitor}.
     *
     * @author Gael Lazzari
     */
    private static class MethodParametersVisitor extends
            GenericVisitorAdapter<Void, Map<AccessibleObject, String[]>> {

        private final StringBuilder currentClassName = new StringBuilder();
        private String packageName;

        @Override
        public Void visit(ClassOrInterfaceDeclaration n, Map<AccessibleObject, String[]> arg) {

            currentClassName.append("$").append(n.getName());

            super.visit(n, arg);

            currentClassName.delete(currentClassName.length() - n.getName().asString().length() - 1,
                    currentClassName.length());

            return null;
        }

        @Override
        public Void visit(ConstructorDeclaration n, Map<AccessibleObject, String[]> arg) {
            Constructor<?> c = findConstructor(n);

            String[] paramNames = extractParameterNames(n.getParameters());
            arg.put(c, paramNames);

            return super.visit(n, arg);

        }

        @Override
        public Void visit(MethodDeclaration n, Map<AccessibleObject, String[]> arg) {

            Method m = findMethod(n);
            String[] paramNames = extractParameterNames(n.getParameters());
            arg.put(m, paramNames);

            return super.visit(n, arg);
        }

        @Override
        public Void visit(PackageDeclaration n, Map<AccessibleObject, String[]> arg) {

            packageName = n.getName().toString();
            return super.visit(n, arg);
        }

        private boolean argsMatch(Class<?>[] parameterTypes, List<Parameter> parameters) {

            if (parameters == null) {
                return parameterTypes.length == 0;
            } else if (parameters.size() != parameterTypes.length) {
                return false;
            }

            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> paramType = parameterTypes[i];
                Parameter param = parameters.get(i);

                if (!paramType.getSimpleName().equals(param.getType().toString())) {
                    return false;
                }
            }

            return true;
        }

        private String[] extractParameterNames(List<Parameter> parameters) {
            int length = (parameters == null) ? 0 : parameters.size();
            String[] params = new String[length];

            for (int i = 0; i < params.length; i++) {
                params[i] = parameters.get(i).getNameAsString();
            }

            return params;
        }

        private Constructor<?> findConstructor(ConstructorDeclaration n) {
            Class<?> currentVisitedClass = getCurrentVisitedClass();

            for (Constructor<?> constructor : currentVisitedClass.getDeclaredConstructors()) {
                if (argsMatch(constructor.getParameterTypes(), n.getParameters())) {
                    return constructor;
                }
            }

            return null;
        }

        private Method findMethod(MethodDeclaration n) {
            Class<?> currentVisitedClass = getCurrentVisitedClass();

            for (Method method : currentVisitedClass.getDeclaredMethods()) {
               if (method.getName().equals(n.getNameAsString())
                       && argsMatch(method.getParameterTypes(), n.getParameters())) {
                    return method;
                }
            }

            return null;
        }

        private Class<?> getCurrentVisitedClass() {
            String className = packageName + "." + currentClassName.substring(1);

            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new ParameterNamesNotFoundException("Error while trying to retrieve class "
                        + className + " :", e);
            }
        }

    }

    private final Map<AccessibleObject, String[]> cache;
    private final JavaFileFinder javaFileFinder;

    /**
     * @param javaFileFinder Object responsible for opening .java files where requested
     *                       {@link Method} or {@link Constructor} are declared.
     */
    public JavaFileParanamer(JavaFileFinder javaFileFinder) {
        this.javaFileFinder = javaFileFinder;
        this.cache = new HashMap<>();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.thoughtworks.paranamer.Paranamer#lookupParameterNames(java.lang.reflect
     * .AccessibleObject)
     */
    public String[] lookupParameterNames(AccessibleObject methodOrConstructor) {
        return lookupParameterNames(methodOrConstructor, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.thoughtworks.paranamer.Paranamer#lookupParameterNames(java.lang.reflect
     * .AccessibleObject, boolean)
     */
    @Override
    public String[] lookupParameterNames(AccessibleObject methodOrConstructor,
                                         boolean throwExceptionIfMissing) {

        if (methodOrConstructor == null) {
            throw new NullPointerException("method or constructor to inspect cannot be null");
        }

        String[] result = cache.get(methodOrConstructor);

        if (result == null) {
            visitJavaFileToPopulateCache(methodOrConstructor);
            result = cache.get(methodOrConstructor);
        }

        if (result == null && throwExceptionIfMissing) {
            throw new ParameterNamesNotFoundException("Cannot retrieve parameter names for method "
                    + methodOrConstructor.toString());
        }

        return result;
    }

    private void visitJavaFileToPopulateCache(AccessibleObject methodOrConstructor) {

        try (InputStream is = javaFileFinder.openJavaFile(methodOrConstructor)) {
            if (is != null) {
                // visit .java file using our custom GenericVisitorAdapter
                CompilationUnit cu = JavaParser.parse(is);
                MethodParametersVisitor visitor = new MethodParametersVisitor();
                cu.accept(visitor, cache);
            }
        } catch (Exception e) {
            throw new ParameterNamesNotFoundException(
                    "Error while trying to read parameter names from the Java file which contains the declaration of "
                            + methodOrConstructor.toString(), e);
        }
    }

}
