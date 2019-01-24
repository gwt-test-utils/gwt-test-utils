package com.googlecode.gwt.test.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.internal.ClassesScanner.ClassVisitor;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.utils.JavassistUtils;

import javassist.CtClass;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * Class in charge of parsing META-INF/gwt-test-utils.properties configuration files. <strong>For
 * internal use only.</strong>
 *
 * @author Bertrand Paquet
 * @author Gael Lazzari
 */
public final class ConfigurationLoader {

    private static final String CONFIG_FILENAME = "META-INF/gwt-test-utils.properties";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationLoader.class);

    private final Set<String> delegates;

    private final List<String> gwtModules;
    private PatcherFactory patcherFactory;
    private final Set<String> scanPackages;
    private final List<URL> srcDirectories;

    ConfigurationLoader(URL surefireBooterJarUrl) {
        this.gwtModules = new ArrayList<>();
        this.delegates = new HashSet<>();
        this.scanPackages = new HashSet<>();
        this.srcDirectories = new ArrayList<>();

        readFiles();
        visitPatchClasses();
        processRelatedProjectSrcDirectories(surefireBooterJarUrl);
    }

    public Set<String> getDelegates() {
        return delegates;
    }

    public List<String> getGwtModules() {
        return gwtModules;
    }

    public PatcherFactory getPatcherFactory() {
        return patcherFactory;
    }

    public Set<String> getScanPackages() {
        return scanPackages;
    }

    public URL[] getSrcUrls() {
        return srcDirectories.toArray(new URL[srcDirectories.size()]);
    }

    private void addToSrcUrls(File file) {
        try {
            srcDirectories.add(file.toURI().toURL());

        } catch (Exception e) {
            // skip : should never happen
        }
    }

    /**
     * Search recursively for java source directories related to a classpathEntry which isn't a .jar
     * file. Typically, those entries would be 'target/classes' (maven) or 'bin' folder of java
     * project which the current project depends on.
     *
     * @param classpathEntry The classpath entry to look around
     * @param distance       The number of super folders in the folder hierarchy to scan
     * @param currentLevel   The current level in the super fold hierarchy. Starts at index 0.
     */
    private void collectEventualSourceDirectories(File classpathEntry, int distance, int currentLevel) {

        String projectRootPath = classpathEntry.getAbsolutePath();
        for (String srcDir : SrcDirectoriesHolder.SRC_DIRECTORIES) {
            StringBuilder sb = new StringBuilder(projectRootPath).append("/").append(srcDir);
            if (!srcDir.endsWith("/")) {
                sb.append("/");
            }

            File file = new File(sb.toString());

            if (file.exists()) {
                addToSrcUrls(file);
            }
        }

        if (currentLevel < distance) {
            collectEventualSourceDirectories(classpathEntry.getParentFile(), distance, ++currentLevel);
        }
    }

    private URL[] extractSrcUrlsFromBooterJar(String surefireBooterJarPath) {

        try {
            // handles spaces encoded %20 to fix
            // https://github.com/gwt-test-utils/gwt-test-utils/issues/17
            String decodedJarPath = URLDecoder.decode(surefireBooterJarPath, "utf-8");
            JarFile surefireBooterJar = new JarFile(decodedJarPath);
            Manifest mf = surefireBooterJar.getManifest();
            Attributes a = mf.getMainAttributes();

            String[] classpathEntries = a.getValue("Class-Path").split(" ");

            URL[] urls = new URL[classpathEntries.length];

            for (int i = 0; i < classpathEntries.length; i++) {
                urls[i] = new URL(classpathEntries[i]);
            }

            return urls;
        } catch (Exception e) {
            throw new GwtTestException("Error while parsing maven-surefire-plugin booter jar: "
                    + surefireBooterJarPath, e);
        }

    }

    private void process(Properties p, URL url) {
        for (Entry<Object, Object> entry : p.entrySet()) {
            String key = ((String) entry.getKey()).trim();
            String value = ((String) entry.getValue()).trim();
            if ("gwt-module".equals(value)) {
                gwtModules.add(key);
            } else if ("scan-package".equals(value)) {
                scanPackages.add(key);
            } else if ("delegate".equals(value)) {
                delegates.add(key);
            } else if ("src-directory".equals(value)) {
                processSrcDirectory(key);
            } else {
                throw new GwtTestConfigurationException("Error in '" + url.getPath()
                        + "' : unknown value '" + value + "'");
            }
        }

    }

    private void processRelatedProjectSrcDirectories(URL surefireBooterJarUrl) {

       	String pathSeparator = System.getProperty("path.separator");
    	String[] classPathEntries = System.getProperty("java.class.path").split(pathSeparator);
    	
    	Collection<URL> urls = new ArrayList<>();
    	for (final String cpe : classPathEntries) {
    		try {
				urls.add(new File(cpe).toURI().toURL());
			} catch (MalformedURLException e) {
				throw new IllegalStateException("Invalid ClassPathURL", e);
			}
    	}
    	URL[] classpathUrls = urls.toArray(new URL[0]);

        for (URL classpathUrl : classpathUrls) {
            srcDirectories.add(classpathUrl);

            if (!classpathUrl.getPath().endsWith(".jar")) {
                // we are only interested in directory files from eventual referenced java project in
                // workspace
                try {
                    collectEventualSourceDirectories(new File(classpathUrl.toURI()), 2, 0);
                } catch (URISyntaxException e) {
                    throw new GwtTestConfigurationException(
                            "Error while getting source folder(s) related to path "
                                    + classpathUrl.getPath(), e);
                }
            }
        }

    }

    private void processSrcDirectory(String srcDir) {
        SrcDirectoriesHolder.SRC_DIRECTORIES.add(srcDir);
    }

    private void readFiles() {
        try {
            Enumeration<URL> configFiles = Thread.currentThread().getContextClassLoader().getResources(
                    CONFIG_FILENAME);
            while (configFiles.hasMoreElements()) {
                URL url = configFiles.nextElement();
                LOGGER.debug("Load config file " + url.toString());
                Properties p = new Properties();
                InputStream inputStream = url.openStream();
                p.load(inputStream);
                inputStream.close();
                process(p, url);
                LOGGER.debug("File loaded and processed " + url.toString());
            }

            if (gwtModules.size() == 0) {
                throw new GwtTestConfigurationException(
                        "No declared module. Did you forget to add your own META-INF/gwt-test-utils.properties file with a 'gwt-module' property in the test classpath?");
            }

        } catch (IOException e) {
            throw new GwtTestConfigurationException("Error while reading '" + CONFIG_FILENAME
                    + "' files", e);
        }
    }

    private void visitPatchClasses() {
        final Map<String, Set<CtClass>> patchClassMap = new HashMap<>();

        ClassVisitor patchClassVisitor = new ClassVisitor() {

            public void visit(CtClass ctClass) {

                try {
                    if (ctClass.hasAnnotation(PatchClass.class)) {

                        Annotation annotation = JavassistUtils.getAnnotation(ctClass, PatchClass.class);

                        String classToPatchName = PatchClass.class.getName();

                        ClassMemberValue value = (ClassMemberValue) annotation.getMemberValue("value");

                        if (value != null) {
                            classToPatchName = value.getValue();
                        }

                        if (classToPatchName.equals(PatchClass.class.getName())) {
                            StringMemberValue target = (StringMemberValue) annotation.getMemberValue("target");
                            classToPatchName = (target != null) ? target.getValue() : "";
                        }

                        if (!"".equals(classToPatchName)) {
                            addPatchClass(classToPatchName, ctClass);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    // should never happen
                    throw new GwtTestPatchException(e);
                }
            }

            private void addPatchClass(String targetName, CtClass patchClass) {
                Set<CtClass> patchClasses = patchClassMap.get(targetName);
                if (patchClasses == null) {
                    patchClasses = new HashSet<>();
                    patchClassMap.put(targetName, patchClasses);
                }

                patchClasses.add(patchClass);

                LOGGER.debug("Add patch for class '" + targetName + "' : '" + patchClass.getName()
                        + "'");
            }

        };

        ClassesScanner.getInstance().scanPackages(patchClassVisitor, scanPackages);

        // create all patchers
        patcherFactory = new PatcherFactory(patchClassMap);
    }

}
