package com.googlecode.gwt.test.internal.utils;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.exceptions.GwtTestException;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for web.xml parsing. <strong>For internal use only</strong>
 *
 * @author Gael Lazzari
 */
public class WebXmlUtils {

    private static WebXmlUtils INSTANCE;

    private static final String[] WAR_ROOTS = new String[]{"war/", "src/main/webapp/"};

    private static final String WEB_XML = "WEB-INF/web.xml";

    public static WebXmlUtils get() {
        if (INSTANCE == null) {
            INSTANCE = new WebXmlUtils();
        }
        return INSTANCE;
    }

    /**
     * Gets a WebXmlUtils, or null if it fails.
     *
     * @return
     */
    public static WebXmlUtils maybeGet() {
        try {
            return get();
        } catch (GwtTestConfigurationException exception) {
            return null;
        }
    }

    private String firstWelcomeFile;
    private final Set<String> listenerClasses;
    // a map with servletUrl as key and serviceImpl className as value
    private final Map<String, String> servletClassMap;

    private WebXmlUtils() {
        InputStream is = getWebXmlAsStream();
        try {
            DocumentBuilder builder = XmlUtils.newDocumentBuilder();
            Document document = builder.parse(is);
            XPath xpath = XPathFactory.newInstance().newXPath();
            servletClassMap = parseServletClassMap(document, xpath);
            listenerClasses = parseListeners(document, xpath);
            firstWelcomeFile = parseFirstWelcomFile(document, xpath);
        } catch (Exception e) {
            if (GwtTestException.class.isInstance(e)) {
                throw (GwtTestException) e;
            }
            throw new GwtTestConfigurationException("Error while parsing web.xml", e);
        } finally {
            // close the stream
            try {
                is.close();
            } catch (Exception ioException) {
                // nothing to do
            }
        }
    }

    public String getFirstWelcomeFile() {
        return firstWelcomeFile;
    }

    public Set<String> getListenerClasses() {
        return listenerClasses;
    }

    public String getServletClass(String servletPath) {
        return servletClassMap.get(servletPath);
    }

    private InputStream getWebXmlAsStream() {
        for (String warRoot : WAR_ROOTS) {
            try {
                return new FileInputStream(warRoot + WEB_XML);
            } catch (FileNotFoundException e) {
                // try next
            }
        }

        throw new GwtTestConfigurationException("Cannot find 'web.xml' file ' for GWT module "
                + GWT.getModuleName());
    }

    private String parseFirstWelcomFile(Document document, XPath xpath)
            throws XPathExpressionException {
        return (String) xpath.evaluate("/web-app/welcome-file-list/welcome-file", document,
                XPathConstants.STRING);
    }

    private Set<String> parseListeners(Document document, XPath xpath)
            throws XPathExpressionException {
        Set<String> result = new HashSet<>();

        NodeList listeners = (NodeList) xpath.evaluate("/web-app/listener", document,
                XPathConstants.NODESET);

        for (int i = 0; i < listeners.getLength(); i++) {
            Node listenerCandidate = listeners.item(i);
            String listenerClassName = xpath.evaluate("listener-class", listenerCandidate);
            result.add(listenerClassName);
        }

        return result;
    }

    private Map<String, String> parseMappingElements(Document document, XPath xpath)
            throws XPathExpressionException {
        NodeList mappings = (NodeList) xpath.evaluate("/web-app/servlet-mapping", document,
                XPathConstants.NODESET);

        Map<String, String> mappingsMap = new HashMap<>();

        for (int i = 0; i < mappings.getLength(); i++) {
            Node mapping = mappings.item(i);
            String servletName = xpath.evaluate("servlet-name", mapping);
            String urlPattern = xpath.evaluate("url-pattern", mapping);
            if (!urlPattern.startsWith("/")) {
                urlPattern = "/" + urlPattern;
            }
            mappingsMap.put(servletName, urlPattern);
        }
        return mappingsMap;
    }

    /**
     * Retrieve Servlets information in the web.xml file.
     *
     * @param document Object representing the web.xml file
     * @param xpath
     * @return A map with servletUrl as key and serviceImpl className as value
     * @throws XPathExpressionException
     */
    private Map<String, String> parseServletClassMap(Document document, XPath xpath)
            throws XPathExpressionException {

        Map<String, String> servletsMap = parseServletElements(document, xpath);
        Map<String, String> mappingsMap = parseMappingElements(document, xpath);

        // create the final map with servlets and servlet-mappings information
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> mappingsEntry : mappingsMap.entrySet()) {
            result.put(mappingsEntry.getValue(), servletsMap.get(mappingsEntry.getKey()));
        }
        return result;
    }

    private Map<String, String> parseServletElements(Document document, XPath xpath)
            throws XPathExpressionException {
        NodeList servlets = (NodeList) xpath.evaluate("/web-app/servlet", document,
                XPathConstants.NODESET);

        Map<String, String> servletsMap = new HashMap<>();

        for (int i = 0; i < servlets.getLength(); i++) {
            Node servlet = servlets.item(i);
            String servletName = xpath.evaluate("servlet-name", servlet);
            String servletClass = xpath.evaluate("servlet-class", servlet);
            servletsMap.put(servletName, servletClass);
        }
        return servletsMap;
    }

}
