package com.googlecode.gwt.test.internal.utils;

import com.google.gwt.dom.client.*;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

/**
 * SAX handler for GWT DOM parsing. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
class GwtHtmlContentHandler implements ContentHandler {

    private Node currentNode;

    private final List<Node> nodes = new ArrayList<>();

    @Override
    public void characters(char[] ch, int start, int length) {

        String string = String.valueOf(ch, start, length).replaceAll("\\u00A0", " ");

        if (string.length() > 0) {
            Text text = Document.get().createTextNode(string);

            if (currentNode != null) {
                currentNode.appendChild(text);
            } else {
                // root text node
                nodes.add(text);
            }
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

    public NodeList<Node> getParsedNodes() {
        return JsoUtils.newNodeList(nodes);
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
    }

    public void startElement(String nameSpaceURI, String localName, String rawName,
                             Attributes attributes) throws SAXException {

        Element element = Document.get().createElement(localName);

        if (currentNode != null) {
            currentNode.appendChild(element);
        } else {
            // root node
            nodes.add(element);
        }
        currentNode = element;

        for (int index = 0; index < attributes.getLength(); index++) {
            String attrName = attributes.getLocalName(index);
            String attrValue = attributes.getValue(index);

            if ("style".equalsIgnoreCase(attrName)) {
                GwtStyleUtils.overrideStyle(element.getStyle(), attrValue);
            } else if ("class".equalsIgnoreCase(attrName)) {
                element.setClassName(attrValue);
            } else {
                element.setAttribute(attrName, attrValue);

            }
        }
    }

    public void startPrefixMapping(String prefix, String URI) throws SAXException {
    }
}
