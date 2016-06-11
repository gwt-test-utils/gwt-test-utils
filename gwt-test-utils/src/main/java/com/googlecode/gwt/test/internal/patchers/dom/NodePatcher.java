package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.*;
import com.googlecode.gwt.test.exceptions.GwtTestDomException;
import com.googlecode.gwt.test.internal.utils.JsoProperties;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

import java.util.List;

@PatchClass(Node.class)
class NodePatcher {

    @PatchMethod
    static Node appendChild(Node parent, Node newChild) {
        return insertAtIndex(parent, newChild, -1);
    }

    @PatchMethod
    static Node cloneNode(Node node, boolean deep) {
        return JsoUtils.cloneJso(node, deep).cast();
    }

    @PatchMethod
    static NodeList<Node> getChildNodes(Node node) {
        return JsoUtils.getChildNodes(node);
    }

    @PatchMethod
    static Node getFirstChild(Node node) {
        List<Node> list = JsoUtils.getChildNodeInnerList(node);

        if (list.size() == 0) {
            return null;
        }

        return list.get(0);
    }

    @PatchMethod
    static Node getLastChild(Node node) {
        List<Node> list = JsoUtils.getChildNodeInnerList(node);

        if (list.size() == 0) {
            return null;
        }

        return list.get(list.size() - 1);
    }

    @PatchMethod
    static Node getNextSibling(Node node) {
        Node parent = node.getParentNode();
        if (parent == null) {
            return null;
        }

        List<Node> list = JsoUtils.getChildNodeInnerList(parent);

        for (int i = 0; i < list.size(); i++) {
            Node current = list.get(i);
            if (current.equals(node) && i < list.size() - 1) {
                return list.get(i + 1);
            }
        }

        return null;
    }

    @PatchMethod
    static String getNodeName(Node node) {
        switch (node.getNodeType()) {
            case Node.DOCUMENT_NODE:
                return "#document";
            case Node.ELEMENT_NODE:
                Element e = node.cast();
                return e.getTagName();
            case Node.TEXT_NODE:
                return "#text";
            case com.google.gwt.xml.client.Node.ATTRIBUTE_NODE:
                return JavaScriptObjects.getString(node, JsoProperties.XML_ATTR_NAME);
            default:
                throw new GwtTestDomException(
                        "Invalid Node type (not a Document / Element / Text / Attribute) : "
                                + node.getNodeType());
        }
    }

    @PatchMethod
    static short getNodeType(Node node) {
        return JsoUtils.getNodeType(node);
    }

    @PatchMethod
    static String getNodeValue(Node node) {
        switch (node.getNodeType()) {
            case Node.DOCUMENT_NODE:
                return null;
            case Node.ELEMENT_NODE:
                return null;
            case Node.TEXT_NODE:
                Text text = node.cast();
                return text.getData();
            default:
                throw new GwtTestDomException("Invalid Node type (not a Document / Element / Text : "
                        + node.getNodeType());
        }
    }

    @PatchMethod
    static Document getOwnerDocument(Node node) {
        return JavaScriptObjects.getObject(node, JsoProperties.NODE_OWNER_DOCUMENT);
    }

    @PatchMethod
    static Node getPreviousSibling(Node node) {
        Node parent = node.getParentNode();
        if (parent == null) {
            return null;
        }

        List<Node> list = JsoUtils.getChildNodeInnerList(parent);

        for (int i = 0; i < list.size(); i++) {
            Node current = list.get(i);
            if (current.equals(node) && i > 0) {
                return list.get(i - 1);
            }
        }

        return null;
    }

    @PatchMethod
    static boolean hasChildNodes(Node node) {
        return JsoUtils.getChildNodeInnerList(node).size() > 0;
    }

    static Node insertAtIndex(Node parent, Node newChild, int index) {
        List<Node> list = JsoUtils.getChildNodeInnerList(parent);

        // First, remove from old parent
        Node oldParent = newChild.getParentNode();
        if (oldParent != null) {
            oldParent.removeChild(newChild);
        }

        // Then, add
        if (index == -1 || index >= list.size()) {
            list.add(newChild);
        } else {
            list.add(index, newChild);
        }

        // Manage getParentNode()
        JsoUtils.setParentNode(newChild, parent);

        return newChild;
    }

    @PatchMethod
    static Node insertBefore(Node parent, Node newChild, Node refChild) {
        List<Node> list = JsoUtils.getChildNodeInnerList(parent);

        // get the index of refChild
        int index = -1;
        if (refChild != null) {
            int i = 0;
            while (index == -1 && i < list.size()) {
                if (list.get(i).equals(refChild)) {
                    index = i;
                }
                i++;
            }
        }

        // Then insert by index
        return insertAtIndex(parent, newChild, index);
    }

    @PatchMethod
    static boolean is(JavaScriptObject object) {
        if (object == null) {
            return false;
        }

        short nodeType = object.<Node>cast().getNodeType();
        return nodeType > 0;
    }

    @PatchMethod
    static Node removeAllChildren(Node node) {
        List<Node> list = JsoUtils.getChildNodeInnerList(node);
        list.clear();
        return node;
    }

    @PatchMethod
    static Node removeChild(Node oldParent, Node oldChild) {
        List<Node> list = JsoUtils.getChildNodeInnerList(oldParent);

        return (list.remove(oldChild)) ? oldChild : null;
    }

    @PatchMethod
    static Node replaceChild(Node parent, Node newChild, Node oldChild) {

        if (oldChild == null) {
            return null;
        }
        List<Node> list = JsoUtils.getChildNodeInnerList(parent);

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(oldChild)) {
                list.add(i, newChild);
                list.remove(oldChild);
                return oldChild;
            }
        }

        return null;
    }

    @PatchMethod
    static void setNodeValue(Node node, String nodeValue) {
        switch (node.getNodeType()) {
            case Node.DOCUMENT_NODE:
                // nothing to do
                break;
            case Node.ELEMENT_NODE:
                // nothing to do
                break;
            case Node.TEXT_NODE:
                Text text = node.cast();
                text.setData(nodeValue);
                break;
        }
    }

}
