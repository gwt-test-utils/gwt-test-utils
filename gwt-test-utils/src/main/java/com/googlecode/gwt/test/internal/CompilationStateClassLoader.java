package com.googlecode.gwt.test.internal;

import com.google.gwt.dev.javac.CompilationState;
import com.google.gwt.dev.resource.impl.ResourceOracleImpl;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import javassist.*;

import java.lang.reflect.Modifier;
import java.net.URLClassLoader;

/**
 * Classloader used by gwt-test-utils when computing the CompilationState. It is able to load both
 * classes and .java files of the current project, relying on a parent {@link URLClassLoader} to
 * collect the necessaries classpath entries setup in META-INF.gwt-test-utils.properties with
 * 'src-directory' key/value pairs.
 * <p>
 * Setting an instance of {@link URLClassLoader} in the classloader hierarchy is mandatory : see the
 * private static "addAllClassPathEntries" in {@link ResourceOracleImpl} for more information.
 * </p>
 * <p>
 * During its instanciation, this classloader checks if tests are launched by maven-surefire-plugin,
 * which bring an isolate classloader, also called "booter" (see :
 * http://maven.apache.org/plugins/maven-surefire -plugin/examples/class-loading.html). This
 * classloader hides classpath resources required by GWT to build module {@link CompilationState}.
 * </p>
 * <p>
 * This classloader also perform some bytecode manipulation : it make every classes / annotation
 * public by default. See <a
 * href="http://code.google.com/p/gwt-test-utils/issues/detail?id=161">issue with gwt-guava
 * GwtTransiant anntation</a>.
 * </p>
 *
 * @author Gael Lazzari
 */
class CompilationStateClassLoader extends Loader {

    /**
     * javassist translator to make loaded classes public by default
     */
    private class MakeClassPublicTranslator implements Translator {

        public void onLoad(ClassPool pool, String classname) throws NotFoundException,
                CannotCompileException {

            CtClass ctClass = pool.get(classname);

            ClassVisibilityModifier.setPublic(ctClass, false);
        }

        public void start(ClassPool pool) throws NotFoundException, CannotCompileException {

        }

    }

    CompilationStateClassLoader(ClassLoader parent, ConfigurationLoader configurationLoader) {
        super(new URLClassLoader(configurationLoader.getSrcUrls(), parent), null);
        ClassPool cp = new ClassPool(null);
        cp.appendSystemPath();

        for (String delegate : configurationLoader.getDelegates()) {
            delegateLoadingOf(delegate);
        }
        delegateLoadingOf("jdk.internal.reflect.");

        try {
            addTranslator(cp, new MakeClassPublicTranslator());
        } catch (Exception e) {
            // should never happen
            throw new GwtTestPatchException(
                    "Error while trying to setup the temporary classloader to load GWT generated .java files",
                    e);
        }
    }

}
