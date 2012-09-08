package com.googlecode.gwt.test.internal;

import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.Loader;
import javassist.NotFoundException;
import javassist.Translator;

import com.google.gwt.dev.javac.CompilationState;
import com.google.gwt.dev.resource.impl.ResourceOracleImpl;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;

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
 * 
 * @author Gael Lazzari
 * 
 */
class CompilationStateClassLoader extends Loader {

   /**
    * javassist translator to make loaded classes public by default
    * 
    */
   private class MakeClassPublicTranslator implements Translator {

      public void onLoad(ClassPool pool, String classname) throws NotFoundException,
               CannotCompileException {

         CtClass ctClass = pool.get(classname);

         int modifiers = ctClass.getModifiers();
         if (!Modifier.isPublic(modifiers)) {
            ctClass.setModifiers(modifiers + Modifier.PUBLIC);
         }

      }

      public void start(ClassPool pool) throws NotFoundException, CannotCompileException {

      }

   }

   private static URL[] computeURLs(URL[] srcUrls, String surefireBooterJarPath) {

      try {
         JarFile surefireBooterJar = new JarFile(surefireBooterJarPath);
         Manifest mf = surefireBooterJar.getManifest();
         Attributes a = mf.getMainAttributes();

         String[] classpathEntries = a.getValue("Class-Path").split(" ");

         URL[] urls = new URL[classpathEntries.length + srcUrls.length];

         System.arraycopy(srcUrls, 0, urls, 0, srcUrls.length);

         for (int i = 0; i < classpathEntries.length; i++) {
            urls[i + srcUrls.length] = new URL(classpathEntries[i]);
         }

         return urls;
      } catch (Exception e) {
         throw new GwtTestException("Error while parsing maven-surefire-plugin booter jar: "
                  + surefireBooterJarPath, e);
      }

   }

   private static URLClassLoader createGwtGeneratedSourceLoader(URL surefireBooterJarUrl,
            URL[] srcUrls, ClassLoader parent) {

      URLClassLoader urlClassLoader = null;

      if (surefireBooterJarUrl == null) {
         urlClassLoader = new URLClassLoader(srcUrls, parent);
      } else {
         String surefireBooterJarPath = surefireBooterJarUrl.getFile();
         urlClassLoader = new URLClassLoader(computeURLs(srcUrls, surefireBooterJarPath), parent);
      }

      return urlClassLoader;
   }

   CompilationStateClassLoader(ClassLoader parent, URL surefireBooterJarUrl,
            ConfigurationLoader configurationLoader) {
      super(createGwtGeneratedSourceLoader(surefireBooterJarUrl, configurationLoader.getSrcUrls(),
               parent), null);
      ClassPool cp = new ClassPool(null);
      cp.appendSystemPath();

      for (String delegate : configurationLoader.getDelegates()) {
         delegateLoadingOf(delegate);
      }

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
