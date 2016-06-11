package com.googlecode.gwt.test.internal.utils;

import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * <strong>For internal use only</strong>
 *
 * @author Gael Lazzari
 */
public class XmlUtils {

    private static DocumentBuilderFactory documentBuilderFactory;

    static {

        documentBuilderFactory = DocumentBuilderFactory.newInstance();

        documentBuilderFactory.setAttribute(
                "http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        documentBuilderFactory.setAttribute(
                "http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        documentBuilderFactory.setNamespaceAware(false);
        documentBuilderFactory.setValidating(false);

    }

    /**
     * Creates a new DocumentBuilder which does not validate document an is not aware of XML
     * namespaces.
     *
     * @return The created DocumentBuilder
     */
    public static DocumentBuilder newDocumentBuilder() {
        try {
            return documentBuilderFactory.newDocumentBuilder();
        } catch (Exception e) {
            // should never happen
            throw new GwtTestConfigurationException("Error while creating a DocumentBuilder", e);
        }
    }

    public static XMLReader newXMLReader() {
        try {
            XMLReader saxReader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
            saxReader.setFeature("http://xml.org/sax/features/validation", false);
            saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
                    false);
            saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",
                    false);
            return saxReader;

        } catch (Exception e) {
            // should never happen..
            throw new GwtTestConfigurationException("Error while creating a XMLReader", e);
        }

    }

    private XmlUtils() {

    }
}
