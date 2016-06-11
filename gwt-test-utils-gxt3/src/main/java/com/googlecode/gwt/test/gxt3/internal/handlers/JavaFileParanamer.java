package com.googlecode.gwt.test.gxt3.internal.handlers;

import com.thoughtworks.paranamer.ParameterNamesNotFoundException;
import com.thoughtworks.paranamer.Paranamer;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.visitor.GenericVisitorAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaFileParanamer implements Paranamer {

    private static class MethodParametersVisitor extends
            GenericVisitorAdapter<Void, Map<Method, String[]>> {

        private final StringBuilder currentClassName = new StringBuilder();
        private String packageName;

        @Override
        public Void visit(ClassOrInterfaceDeclaration n,
                          java.util.Map<Method, String[]> arg) {

            currentClassName.append("$").append(n.getName());

            super.visit(n, arg);

            currentClassName.delete(currentClassName.length() - n.getName().length()
                    - 1, currentClassName.length());

            return null;
        }

        @Override
        public Void visit(MethodDeclaration n, Map<Method, String[]> arg) {

            Method m = findMethod(n);

            int length = (n.getParameters() == null) ? 0 : n.getParameters().size();
            String[] params = new String[length];

            for (int i = 0; i < params.length; i++) {
                params[i] = n.getParameters().get(i).getId().getName();
            }

            arg.put(m, params);

            return super.visit(n, arg);

        }

        @Override
        public Void visit(PackageDeclaration n, Map<Method, String[]> arg) {

            packageName = n.getName().toString();
            return super.visit(n, arg);
        }

        private boolean argsMatch(Class<?>[] parameterTypes,
                                  List<Parameter> parameters) {

            if (parameters == null) {
                return parameterTypes.length == 0;
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

        private Method findMethod(MethodDeclaration n) {
            Class<?> currentVisitedClass = getCurrentVisitedClass();

            for (Method method : currentVisitedClass.getMethods()) {
                if (!method.getName().equals(n.getName())) {
                    continue;
                } else if (argsMatch(method.getParameterTypes(), n.getParameters())) {
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
                throw new ParameterNamesNotFoundException(
                        "Error while trying to retrieve class " + className + " :", e);
            }
        }

        ;

    }

    private static JavaFileParanamer instance;

    public static JavaFileParanamer get() {
        if (instance == null) {
            instance = new JavaFileParanamer();
        }

        return instance;
    }

    private final Map<Method, String[]> cache;

    private JavaFileParanamer() {
        this.cache = new HashMap<Method, String[]>();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.thoughtworks.paranamer.Paranamer#lookupParameterNames(java.lang.reflect
     * .AccessibleObject)
     */
    public String[] lookupParameterNames(AccessibleObject methodOrConstructor) {
        return lookupParameterNames(methodOrConstructor, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.thoughtworks.paranamer.Paranamer#lookupParameterNames(java.lang.reflect
     * .AccessibleObject, boolean)
     */
    public String[] lookupParameterNames(AccessibleObject methodOrConstructor,
                                         boolean throwExceptionIfMissing) {

        if (methodOrConstructor == null) {
            throw new NullPointerException(
                    "method or constructor to inspect cannot be null");
        }

        String[] result = cache.get(methodOrConstructor);

        if (result == null) {
            if (methodOrConstructor instanceof Method) {
                Class<?> declaringClass = ((Method) methodOrConstructor).getDeclaringClass();
                try {
                    visitJavaFile(declaringClass);
                } catch (Exception e) {
                    throw new ParameterNamesNotFoundException(
                            "Error while trying to read parameter names from the Java file which contains the declaration of "
                                    + declaringClass.getName(), e);
                }

                result = cache.get(methodOrConstructor);

                if (result == null && throwExceptionIfMissing) {
                    throw new ParameterNamesNotFoundException(
                            "Cannot retrieve parameter names for method "
                                    + methodOrConstructor.toString());
                }
            } else {
                throw new UnsupportedOperationException("Not managed type : "
                        + methodOrConstructor.getClass().getSimpleName());
            }

        }
        return result;
    }

    private InputStream getJavaFile(Class<?> declaringClass) {
        if (declaringClass.isMemberClass() || declaringClass.isLocalClass()) {
            declaringClass = declaringClass.getDeclaringClass();

        }
        String javaPath = declaringClass.getCanonicalName().replaceAll("\\.", "/")
                + ".java";

        return declaringClass.getClassLoader().getResourceAsStream(javaPath);
    }

    private void visitJavaFile(Class<?> declaringClass) throws ParseException,
            IOException {
        InputStream is = getJavaFile(declaringClass);

        if (is == null) {
            return;
        }

        CompilationUnit cu;
        try {
            // parse the file
            cu = JavaParser.parse(is);
        } finally {
            is.close();
        }

        MethodParametersVisitor visitor = new MethodParametersVisitor();

        cu.accept(visitor, cache);

    }
}
