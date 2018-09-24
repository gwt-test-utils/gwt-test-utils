package com.googlecode.gwt.test.internal;

import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.internal.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * Class which provide all necessary information about a GWT module. <strong>For internal use
 * only.</strong>
 *
 * @author Gael Lazzari
 */
public class ModuleData {

    public static class ReplaceWithData {

        private final Map<String, List<String>> anyWhenPropertyIs = new HashMap<>();
        private final String replaceWith;
        private final Map<String, String> whenPropertyIs = new HashMap<>();
        private final String whenTypeIs;

        public ReplaceWithData(String replaceWith, String whenTypeIs) {
            this.replaceWith = replaceWith;
            this.whenTypeIs = whenTypeIs;
        }

        public boolean anyMatch(String propName, String propValue) {
            List<String> any = anyWhenPropertyIs.get(propName);
            if (any == null) {
                return false;
            }

            for (String value : any) {
                if (propValue.equals(value)) {
                    return true;
                }
            }
            return false;
        }

        public String getReplaceWith() {
            return replaceWith;
        }

        public String getWhenTypeIs() {
            return whenTypeIs;
        }

        public boolean hasAnyWhenPropertyIs() {
            return anyWhenPropertyIs.size() > 0;
        }

        public boolean hasWhenPropertyIs() {
            return whenPropertyIs.size() > 0;
        }

        public boolean whenPropertyIsMatch(String propName, String propValue) {
            if (propValue == null) {
                return false;
            }

            return propValue.equals(whenPropertyIs.get(propName));
        }

        void addAny(String propName, String propValue) {
            List<String> any = anyWhenPropertyIs.get(propName);
            if (any == null) {
                any = new ArrayList<>();
                anyWhenPropertyIs.put(propName, any);
            }
            any.add(propValue);
        }

        void addWhenPropertyIs(String propName, String propValue) {
            whenPropertyIs.put(propName, propValue);
        }
    }

    private static final Map<String, ModuleData> CACHE = new HashMap<>();

    private static final String[] CLASSPATH_ROOTS = new String[]{
            "src/main/java/", "src/main/resources/", "src/test/java/", "src/test/resources/",
            "src/", "resources/", "res/"};

    public static ModuleData get(String moduleName) {
        ModuleData moduleData = CACHE.get(moduleName);
        if (moduleData == null) {
            moduleData = new ModuleData(moduleName);
            CACHE.put(moduleName, moduleData);
        }

        return moduleData;
    }

    private String alias;

    private final Set<String> customGeneratedClasses;

    private final Set<String> parsedModules;

    private final Map<String, List<ReplaceWithData>> replaceWithListMap;

    private ModuleData(String moduleName) {
        this.replaceWithListMap = new HashMap<>();
        this.customGeneratedClasses = new HashSet<>();
        this.parsedModules = new HashSet<>();

        parseModule(moduleName);
    }

    public String getAlias() {
        return alias;
    }

    public Set<String> getCustomGeneratedClasses() {
        return customGeneratedClasses;
    }

    public Class<?> getRemoteServiceImplClass(String remoteServicePath) {

        if (!remoteServicePath.startsWith("/")) {
            remoteServicePath = "/" + remoteServicePath;
        }

        String servletClassName = GwtFactory.get().getModuleDef().findServletForPath(
                remoteServicePath);

        if (servletClassName == null) {
            return null;
        }

        try {
            return Class.forName(servletClassName, true, GwtFactory.get().getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new GwtTestConfigurationException("Cannot find servlet class '" + servletClassName
                    + "' configured for servlet path '" + remoteServicePath + "'");
        }
    }

    public Map<String, List<ReplaceWithData>> getReplaceWithListMap() {
        return replaceWithListMap;
    }

    private Document createDocument(String moduleName) throws Exception {

        String moduleFilePath = moduleName.replaceAll("\\.", "/") + ".gwt.xml";

        InputStream is = getModuleFileAsStream(moduleFilePath);

        try {
            DocumentBuilder builder = XmlUtils.newDocumentBuilder();
            return builder.parse(is);
        } finally {
            // close the stream
            try {
                is.close();
            } catch (Exception ioException) {
                // nothing to do
            }
        }
    }

    private String getModuleAlias(Document document, XPath xpath) throws XPathExpressionException {
        return xpath.evaluate("/module/@rename-to", document).trim();
    }

    private InputStream getModuleFileAsStream(String moduleFilePath) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(moduleFilePath);

        if (is != null) {
            return is;
        }

        for (String classpathRoot : CLASSPATH_ROOTS) {
            try {
                return new FileInputStream(classpathRoot + moduleFilePath);
            } catch (FileNotFoundException e) {
                // try next
            }
        }

        throw new GwtTestConfigurationException("Cannot find GWT module configuration file '"
                + moduleFilePath + "' in the classpath");
    }

    private void initializeGenerateWith(Document document, XPath xpath)
            throws XPathExpressionException {

        NodeList whenTypeAssignableList = (NodeList) xpath.evaluate(
                "/module/generate-with/when-type-assignable[@class]", document,
                XPathConstants.NODESET);

        for (int i = 0; i < whenTypeAssignableList.getLength(); i++) {
            Node whenTypeAssignableWith = whenTypeAssignableList.item(i);

            String className = xpath.evaluate("@class", whenTypeAssignableWith);
            customGeneratedClasses.add(className);
        }
    }

    private void initializeInherits(Document document, XPath xpath) throws Exception {
        NodeList inherits = (NodeList) xpath.evaluate("/module/inherits", document,
                XPathConstants.NODESET);

        for (int i = 0; i < inherits.getLength(); i++) {
            Node inherit = inherits.item(i);
            String inheritName = xpath.evaluate("@name", inherit).trim();

            if (parsedModules.contains(inheritName) || inheritName.startsWith("com.google.gwt")
                    || inheritName.startsWith("com.google.web.bindery.")) {
                continue;
            }

            parseModuleFile(inheritName, createDocument(inheritName), xpath);
        }
    }

    private void initializeReplaceWith(Document document, XPath xpath)
            throws XPathExpressionException {
        NodeList replaceWithList = (NodeList) xpath.evaluate("/module/replace-with[@class]",
                document, XPathConstants.NODESET);

        for (int i = 0; i < replaceWithList.getLength(); i++) {
            Node replaceWith = replaceWithList.item(i);

            String replaceClass = xpath.evaluate("@class", replaceWith);
            String whenTypeIsClass = xpath.evaluate("when-type-is/@class", replaceWith);

            ReplaceWithData data = new ReplaceWithData(replaceClass, whenTypeIsClass);

            List<ReplaceWithData> list = replaceWithListMap.get(data.whenTypeIs);
            if (list == null) {
                list = new ArrayList<>();
                replaceWithListMap.put(data.whenTypeIs, list);
            }

            // Handle when-property-is list
            NodeList whenPropertyIsList = (NodeList) xpath.evaluate("when-property-is", replaceWith,
                    XPathConstants.NODESET);

            for (int j = 0; j < whenPropertyIsList.getLength(); j++) {
                Node whenPropertyIs = whenPropertyIsList.item(j);
                String name = xpath.evaluate("@name", whenPropertyIs);
                String value = xpath.evaluate("@value", whenPropertyIs);
                data.addWhenPropertyIs(name, value);
            }

            // Handle any/when-property-is list
            NodeList anyWhenPropertyIsList = (NodeList) xpath.evaluate("any/when-property-is",
                    replaceWith, XPathConstants.NODESET);

            for (int j = 0; j < anyWhenPropertyIsList.getLength(); j++) {
                Node anyWhenPropertyIs = anyWhenPropertyIsList.item(j);
                String name = xpath.evaluate("@name", anyWhenPropertyIs);
                String value = xpath.evaluate("@value", anyWhenPropertyIs);

                data.addAny(name, value);
            }

            list.add(data);
        }
    }

    private void parseModule(String moduleName) {

        try {

            Document document = createDocument(moduleName);
            XPath xpath = XPathFactory.newInstance().newXPath();
            parseModuleFile(moduleName, document, xpath);

            alias = getModuleAlias(document, xpath);

        } catch (Exception e) {
            if (GwtTestException.class.isInstance(e)) {
                throw (GwtTestException) e;
            } else {
                throw new GwtTestConfigurationException("Error while parsing GWT module '" + moduleName
                        + "'", e);
            }
        }
    }

    /**
     * parse .gwt.xml file to get fill {@link ModuleData} information
     *
     * @param moduleName The module name
     * @param xpath
     * @throws Exception
     */
    private void parseModuleFile(String moduleName, Document document, XPath xpath) throws Exception {

        parsedModules.add(moduleName);

        initializeInherits(document, xpath);
        initializeReplaceWith(document, xpath);
        initializeGenerateWith(document, xpath);
    }

}
