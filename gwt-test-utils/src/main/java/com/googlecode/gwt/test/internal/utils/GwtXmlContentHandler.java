package com.googlecode.gwt.test.internal.utils;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.googlecode.gwt.test.utils.JavaScriptObjects;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * SAX handler for GWT DOM parsing from an XML source. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
class GwtXmlContentHandler implements ContentHandler {

    private Node currentNode;
    private Document document;

    public void characters(char[] ch, int start, int length) throws SAXException {

        String text = String.valueOf(ch, start, length).trim();

        if (text.length() > 0) {
            currentNode.appendChild(document.createTextNode(text));
        }
    }

    public void endDocument() throws SAXException {
    }

    public void endElement(String nameSpaceURI, String localName, String rawName)
            throws SAXException {
        currentNode = currentNode.getParentNode();
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public Document getParsedDocument() {
        return document;
    }

    public void ignorableWhitespace(char[] ch, int start, int end) throws SAXException {
    }

    public void processingInstruction(String target, String data) throws SAXException {
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void skippedEntity(String arg0) throws SAXException {
    }

    public void startDocument() throws SAXException {
        document = JsoUtils.newDocument();
        currentNode = document;
    }

    public void startElement(String nameSpaceURI, String localName, String rawName,
                             Attributes attributes) throws SAXException {

        Element element = document.createElement(localName);

        JavaScriptObjects.setProperty(element, JsoProperties.NODE_NAMESPACE_URI, nameSpaceURI);

        currentNode.appendChild(element);
        currentNode = element;

        Set<String> attrs = new LinkedHashSet<String>();
        JavaScriptObjects.setProperty(element, JsoProperties.XML_ATTR_SET, attrs);

        for (int index = 0; index < attributes.getLength(); index++) {
            String attrName = attributes.getLocalName(index);
            String attrValue = attributes.getValue(index);
            element.setAttribute(attrName, attrValue);
            attrs.add(attrName);
        }
    }

    public void startPrefixMapping(String prefix, String URI) throws SAXException {
    }

}
