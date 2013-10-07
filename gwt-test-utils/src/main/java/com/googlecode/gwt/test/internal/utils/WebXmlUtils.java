package com.googlecode.gwt.test.internal.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.exceptions.GwtTestException;

/**
 * Utility class for web.xml parsing. <strong>For internal use only</strong>
 * 
 * @author Gael Lazzari
 * 
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

   private final Set<String> listenerClasses;
   // a map with servletUrl as key and serviceImpl className as value
   private final Map<String, String> servletClassMap;
   private String firstWelcomeFile;

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
         } else {
            throw new GwtTestConfigurationException("Error while parsing web.xml", e);
         }
      } finally {
         // close the stream
         try {
            is.close();
         } catch (Exception ioException) {
            // nothing to do
         }
      }
   }

   public Set<String> getListenerClasses() {
      return listenerClasses;
   }

   public String getServletClass(String servletPath) {
      return servletClassMap.get(servletPath);
   }

   public String getFirstWelcomeFile() {
      return firstWelcomeFile;
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

   private Set<String> parseListeners(Document document, XPath xpath)
            throws XPathExpressionException {
      Set<String> result = new HashSet<String>();

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

      Map<String, String> mappingsMap = new HashMap<String, String>();

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
      Map<String, String> result = new HashMap<String, String>();
      for (Map.Entry<String, String> mappingsEntry : mappingsMap.entrySet()) {
         result.put(mappingsEntry.getValue(), servletsMap.get(mappingsEntry.getKey()));
      }
      return result;
   }

   private Map<String, String> parseServletElements(Document document, XPath xpath)
            throws XPathExpressionException {
      NodeList servlets = (NodeList) xpath.evaluate("/web-app/servlet", document,
               XPathConstants.NODESET);

      Map<String, String> servletsMap = new HashMap<String, String>();

      for (int i = 0; i < servlets.getLength(); i++) {
         Node servlet = servlets.item(i);
         String servletName = xpath.evaluate("servlet-name", servlet);
         String servletClass = xpath.evaluate("servlet-class", servlet);
         servletsMap.put(servletName, servletClass);
      }
      return servletsMap;
   }

   private String parseFirstWelcomFile(Document document, XPath xpath)
            throws XPathExpressionException {
      return (String) xpath.evaluate("/web-app/welcome-file-list/welcome-file", document,
               XPathConstants.STRING);
   }

}
