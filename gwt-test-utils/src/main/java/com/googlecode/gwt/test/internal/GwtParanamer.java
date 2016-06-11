package com.googlecode.gwt.test.internal;

import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.thoughtworks.paranamer.JavaFileParanamer;
import com.thoughtworks.paranamer.JavaFileParanamer.JavaFileFinder;
import com.thoughtworks.paranamer.Paranamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * gwt-test-utils custom {@link Paranamer} manager, which relies on {@link JavaFileParanamer}.
 * <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public class GwtParanamer {

    /**
     * GWT implementation of {@link JavaFileFinder}
     *
     * @author Gael Lazzari
     */
    private static class GwtTestJavaFileFinder implements JavaFileFinder {

        private static final Logger LOGGER = LoggerFactory.getLogger(GwtTestJavaFileFinder.class);

        /*
         * (non-Javadoc)
         *
         * @see com.thoughtworks.paranamer.JavaFileParanamer.JavaFileFinder#openJavaFile
         * (java.lang.reflect.AccessibleObject)
         */
        public InputStream openJavaFile(AccessibleObject methodOrConstructor) {

            Class<?> declaringClass = null;

            if (methodOrConstructor instanceof Method) {
                declaringClass = ((Method) methodOrConstructor).getDeclaringClass();
            } else if (methodOrConstructor instanceof Constructor) {
                declaringClass = ((Constructor<?>) methodOrConstructor).getDeclaringClass();
            } else {
                throw new UnsupportedOperationException("Not managed type : "
                        + methodOrConstructor.getClass().getSimpleName());
            }

            return getInputStream(declaringClass);
        }

        private InputStream getInputStream(Class<?> declaringClass) {
            if (declaringClass.isMemberClass() || declaringClass.isLocalClass()) {
                declaringClass = declaringClass.getDeclaringClass();

            }
            String javaPath = declaringClass.getCanonicalName().replaceAll("\\.", "/") + ".java";

            // 1. try to get the java file from the classpath : case where the java
            // file is in a 3rd party GWT module (e.g. a .jar with .java files in
            // it)
            InputStream is = declaringClass.getClassLoader().getResourceAsStream(javaPath);

            if (is != null) {
                return is;
            }

            // 2. the java file should be in the currently tested java project :
            // browse registered src directories ('src-directory' entries in
            // gwt-test-utils.properties)

            for (String srcDirectory : SrcDirectoriesHolder.SRC_DIRECTORIES) {
                File root = new File(srcDirectory);
                File javaFile = new File(root, javaPath);
                if (javaFile.exists()) {
                    try {
                        return new FileInputStream(javaFile);
                    } catch (Exception e) {
                        LOGGER.error(
                                "error while trying to retrieve java file corresponding to class "
                                        + declaringClass.getName() + ", deducted path is "
                                        + javaFile.getAbsolutePath(), e);
                    }
                }

            }

            throw new GwtTestConfigurationException(
                    "Cannot find the .java file which contains declaration of "
                            + declaringClass.getName()
                            + ". Did you forget to add your java source directory path by declaring a 'src-directory' property in your META-INF/gwt-test-utils.properties configuration file ? (example : src/main/java = src-directory)");
        }
    }

    private static final Paranamer INSTANCE = new JavaFileParanamer(new GwtTestJavaFileFinder());

    public static Paranamer get() {
        return INSTANCE;
    }

}
