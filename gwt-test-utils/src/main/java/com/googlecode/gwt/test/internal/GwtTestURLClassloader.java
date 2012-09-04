package com.googlecode.gwt.test.internal;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.google.gwt.dev.javac.CompilationState;
import com.google.gwt.dev.resource.impl.ResourceOracleImpl;
import com.googlecode.gwt.test.exceptions.GwtTestException;

/**
 * <p>
 * Custom URLClassLoader designed to retrieve every classpath entries necessary to run
 * gwt-test-utils tests, regardless of how tests are runned. It has to be bound to the current
 * thread at the very beginning of gwt-test-utils static initialization. <strong>For internal use
 * only.</strong>
 * </p>
 * 
 * <p>
 * During its instanciation, this classloader checks if tests are launched by maven-surefire-plugin,
 * which bring an isolate classloader, also called "booter" (see :
 * http://maven.apache.org/plugins/maven-surefire -plugin/examples/class-loading.html). This
 * classloader hides classpath resources required by GWT to build module {@link CompilationState}.
 * </p>
 * 
 * @author Gael Lazzari
 * 
 * @see ResourceOracleImpl#preload(com.google.gwt.core.ext.TreeLogger, ClassLoader)
 * 
 */
public class GwtTestURLClassloader extends URLClassLoader {

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

   public GwtTestURLClassloader(URL[] urls) {
      super(urls);
   }

   public GwtTestURLClassloader(URL[] urls, String surefireBooterJarPath) {
      super(computeURLs(urls, surefireBooterJarPath));
   }

}
