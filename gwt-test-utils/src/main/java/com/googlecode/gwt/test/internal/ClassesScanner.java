package com.googlecode.gwt.test.internal;

import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import javassist.CtClass;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The class used to scan and filter classes. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
final class ClassesScanner {

    static interface ClassVisitor {

        void visit(CtClass ctClass);
    }

    private static final ClassesScanner INSTANCE = new ClassesScanner();

    private static Logger logger = LoggerFactory.getLogger(ClassesScanner.class);

    public static ClassesScanner getInstance() {
        return INSTANCE;
    }

    private ClassesScanner() {
    }

    public void scanPackages(ClassVisitor classVisitor, Set<String> rootPackages) {

        for (String rootPackage : rootPackages) {
            String path = rootPackage.replaceAll("\\.", "/");
            logger.debug("Scan package " + rootPackage);

            if (rootPackage.endsWith(".")) {
                rootPackage = rootPackage.substring(0, rootPackage.length() - 1);
            }
            try {
                Enumeration<URL> l = Thread.currentThread().getContextClassLoader().getResources(path);
                while (l.hasMoreElements()) {
                    URL url = l.nextElement();
                    String u = url.toExternalForm();
                    if (u.startsWith("file:")) {
                        String directoryName = u.substring("file:".length());
                        directoryName = URLDecoder.decode(directoryName, "UTF-8");
                        scanClassesFromDirectory(new File(directoryName), rootPackage, classVisitor);
                    } else if (u.startsWith("jar:file:")) {
                        scanClassesFromJarFile(u.substring("jar:file:".length()), path, classVisitor);
                    } else {
                        throw new IllegalArgumentException("Not managed class container " + u);
                    }
                }
            } catch (Exception e) {
                throw new GwtTestPatchException("Error while scanning package '" + rootPackage + "'", e);
            }
        }
    }

    private void scanClassesFromDirectory(File directoryToScan, String scanPackage,
                                          ClassVisitor classVisitor) {
        logger.debug("Scan directory " + directoryToScan);
        for (File f : directoryToScan.listFiles()) {
            if (f.isDirectory()) {
                if (!".".equals(f.getName()) && !"..".equals(f.getName())) {
                    scanClassesFromDirectory(f, scanPackage + "." + f.getName(), classVisitor);
                }
            } else if (f.getName().endsWith(".class")) {
                visitClass(scanPackage + "." + f.getName(), classVisitor);
            }
        }
        logger.debug("Directory scanned " + directoryToScan);
    }

    private void scanClassesFromJarFile(String path, String scanPackage, ClassVisitor classVisitor)
            throws Exception {
        String prefix = path.substring(path.indexOf("!") + 2);
        String jarName = path.substring(0, path.indexOf("!"));
        jarName = URLDecoder.decode(jarName, "UTF-8");
        logger.debug("Load classes from jar " + jarName);
        JarFile jar = new JarFile(jarName);
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().startsWith(prefix) && entry.getName().endsWith(".class")) {
                visitClass(entry.getName().replaceAll("\\/", "."), classVisitor);
            }
        }
        logger.debug("Classes loaded from jar " + jarName);
    }

    private void visitClass(String classFileName, ClassVisitor classVisitor) {
        try {
            CtClass current = GwtClassPool.getClass(classFileName.substring(0, classFileName.length()
                    - ".class".length()));

            classVisitor.visit(current);

            for (CtClass innerClass : current.getNestedClasses()) {
                classVisitor.visit(innerClass);
            }

        } catch (NotFoundException e) {
            // do nothing
        }
    }

}
