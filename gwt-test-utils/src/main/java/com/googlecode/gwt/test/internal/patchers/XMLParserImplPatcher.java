package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.*;
import com.google.gwt.xml.client.impl.XMLParserImpl;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.internal.utils.GwtXMLParser;
import com.googlecode.gwt.test.internal.utils.JsoProperties;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.internal.utils.PropertyContainer;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@PatchClass(XMLParserImpl.class)
class XMLParserImplPatcher {

    private static final String XML_ATTR_VALUE = "XML_ATTR_VALUE";

    @PatchMethod
    static JavaScriptObject appendChild(JavaScriptObject jsObject, JavaScriptObject newChildJs) {
        Node n = jsObject.cast();
        Node newChildNode = newChildJs.cast();
        return n.appendChild(newChildNode);
    }

    @PatchMethod
    static JavaScriptObject createCDATASection(JavaScriptObject jsObject, String data) {
        Document document = jsObject.cast();

        Text text = JsoUtils.newNode(com.google.gwt.xml.client.Node.CDATA_SECTION_NODE).cast();
        JavaScriptObjects.setProperty(text, JsoProperties.NODE_OWNER_DOCUMENT, document);

        text.setData(data);

        return text;
    }

    @PatchMethod
    static JavaScriptObject createDocumentImpl(XMLParserImpl xmlParserImpl) {
        return JsoUtils.newNode(com.google.gwt.xml.client.Node.DOCUMENT_NODE);
    }

    @PatchMethod
    static JavaScriptObject createElement(JavaScriptObject jsObject, String tagName) {
        Document document = jsObject.cast();
        return JsoUtils.newXmlElement(document, tagName);
    }

    @PatchMethod
    static JavaScriptObject createTextNode(JavaScriptObject jsObject, String text) {
        Document document = jsObject.cast();
        return document.createTextNode(text);
    }

    @PatchMethod
    static String getAttribute(JavaScriptObject o, String name) {
        // Attribute return by XML node can be null
        PropertyContainer properties = JsoUtils.getDomProperties(o.<Element>cast());

        // special treatement for 'class' attribute, which is automatically stored
        // in the PropertyContainer with 'className' key

        if ("class".equals(name)) {
            name = "className";
        }
        return properties.getObject(name);
    }

    @PatchMethod
    static JavaScriptObject getAttributeNode(JavaScriptObject o, String name) {
        String value = getAttribute(o, name);

        // create the JavaScriptObject which will simulate an google.xml.Attr
        Node attrJSO = JsoUtils.newNode(com.google.gwt.xml.client.Node.ATTRIBUTE_NODE).cast();
        JavaScriptObjects.setProperty(attrJSO, JsoProperties.XML_ATTR_NAME, name);
        JavaScriptObjects.setProperty(attrJSO, XML_ATTR_VALUE, value);
        JavaScriptObjects.setProperty(attrJSO, JsoProperties.NODE_NAMESPACE_URI, getNamespaceURI(o));

        return attrJSO;
    }

    @PatchMethod
    static JavaScriptObject getAttributes(JavaScriptObject t) {
        Set<String> attrSet = JavaScriptObjects.getObject(t, JsoProperties.XML_ATTR_SET);

        List<Node> list = new ArrayList<>();

        for (String attrName : attrSet) {
            Node attrNode = getAttributeNode(t, attrName).cast();
            list.add(attrNode);
        }

        return JsoUtils.newNodeList(list);
    }

    @PatchMethod
    static JavaScriptObject getChildNodes(JavaScriptObject t) {
        Node n = t.cast();
        return n.getChildNodes();
    }

    @PatchMethod
    static String getData(JavaScriptObject o) {
        Text text = o.cast();
        return text.getData();
    }

    @PatchMethod
    static JavaScriptObject getDocumentElement(JavaScriptObject o) {
        Document document = o.cast();
        return document.getFirstChild();
    }

    @PatchMethod
    static JavaScriptObject getElementByIdImpl(XMLParserImpl xmlParserImpl,
                                               JavaScriptObject jsoDocument, String id) {
        Document document = jsoDocument.cast();
        return document.getElementById(id);
    }

    @PatchMethod
    static JavaScriptObject getElementsByTagNameImpl(XMLParserImpl xmlParserImpl,
                                                     JavaScriptObject o, String tagName) {
        Node node = o.cast();
        NodeList<Element> nodeList;

        switch (node.getNodeType()) {
            case Node.DOCUMENT_NODE:
                Document document = node.cast();
                nodeList = document.getElementsByTagName(tagName);
                break;
            case Node.ELEMENT_NODE:
                Element element = node.cast();
                nodeList = element.getElementsByTagName(tagName);
                break;
            default:
                nodeList = JsoUtils.newNodeList();
                break;
        }

        return nodeList;
    }

    @PatchMethod
    static int getLength(JavaScriptObject o) {
        NodeList<Node> nodeList = o.cast();
        return nodeList.getLength();
    }

    @PatchMethod
    static String getName(JavaScriptObject o) {
        return JavaScriptObjects.getString(o, JsoProperties.XML_ATTR_NAME);
    }

    @PatchMethod
    static JavaScriptObject getNamedItem(JavaScriptObject t, String name) {
        NodeList<Node> attrs = t.cast();

        for (int i = 0; i < attrs.getLength(); i++) {
            Node n = attrs.getItem(i);
            if (name.equals(getName(n))) {
                return n;
            }
        }

        return null;
    }

    @PatchMethod
    static String getNamespaceURI(JavaScriptObject o) {
        return JavaScriptObjects.getString(o, JsoProperties.NODE_NAMESPACE_URI);
    }

    @PatchMethod
    static JavaScriptObject getNextSibling(JavaScriptObject o) {
        Node n = o.cast();
        return n.getNextSibling();
    }

    @PatchMethod
    static String getNodeName(JavaScriptObject o) {
        try {
            Node node = o.cast();
            return node.getNodeName();
        } catch (ClassCastException e) {
            // TODO: remove this when cast() will be fine
            return "";
        }
    }

    @PatchMethod
    static short getNodeType(JavaScriptObject jsObject) {
        Node node = jsObject.cast();
        return node.getNodeType();
    }

    @PatchMethod
    static String getNodeValue(JavaScriptObject o) {
        Node n = o.cast();
        switch (n.getNodeType()) {
            case com.google.gwt.xml.client.Node.ATTRIBUTE_NODE:
                return JavaScriptObjects.getString(n, XML_ATTR_VALUE);
            case Node.ELEMENT_NODE:
                Element e = n.cast();
                return e.getInnerText();
            default:
                return n.getNodeValue();
        }
    }

    @PatchMethod
    static Document getOwnerDocument(JavaScriptObject o) {
        Node n = o.cast();
        return n.getOwnerDocument();
    }

    @PatchMethod
    static JavaScriptObject getPreviousSibling(JavaScriptObject o) {
        Node n = o.cast();
        return n.getPreviousSibling();
    }

    @PatchMethod
    static String getTagName(JavaScriptObject o) {
        Element e = o.cast();
        return e.getTagName();
    }

    @PatchMethod
    static String getValue(JavaScriptObject o) {
        return JavaScriptObjects.getString(o, XML_ATTR_VALUE);
    }

    @PatchMethod
    static boolean hasChildNodes(JavaScriptObject jsObject) {
        Node n = jsObject.cast();
        return n.hasChildNodes();
    }

    @PatchMethod
    static JavaScriptObject item(JavaScriptObject t, int index) {
        NodeList<Node> nodeList = t.cast();
        return nodeList.getItem(index);
    }

    @PatchMethod
    static JavaScriptObject parseImpl(XMLParserImpl xmlParserImpl, String contents) {

        try {
            return GwtXMLParser.parse(contents);
        } catch (Exception e) {
            throw new GwtTestPatchException("Error while parsing XML", e);
        }
    }

    @PatchMethod
    static JavaScriptObject removeChild(JavaScriptObject jsObject, JavaScriptObject oldChildJs) {
        Node node = jsObject.cast();
        Node oldChildNode = oldChildJs.cast();

        return node.removeChild(oldChildNode);
    }

    @PatchMethod
    static void setAttribute(JavaScriptObject o, String name, String value) {
        PropertyContainer properties = JsoUtils.getDomProperties(o.<Element>cast());
        properties.put(name, value);
    }

    @PatchMethod
    static void setNodeValue(JavaScriptObject jsObject, String nodeValue) {
        Node n = jsObject.cast();
        switch (n.getNodeType()) {
            case Node.TEXT_NODE:
                Text text = n.cast();
                text.setData(nodeValue);
                break;
            case Node.ELEMENT_NODE:
                Element element = n.cast();
                element.setInnerText(nodeValue);
                break;
            case Node.DOCUMENT_NODE:
                // nothing to do
                break;
        }
    }

}
