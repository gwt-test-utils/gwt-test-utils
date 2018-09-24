package com.googlecode.gwt.test.internal.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.test.finder.GwtFinder;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.JavaScriptObjects;
import com.googlecode.gwt.test.utils.WidgetUtils;

import java.util.*;

/**
 * Some {@link JavaScriptObject} utility methods. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public class JsoUtils {

    private static final Set<String> DOM_PROPERTIES = new HashSet<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
         /*
          * 
          * Parse HTML standard attributes here : http://www.w3.org/TR/html4/index/attributes.html
          * 
          * With this jQuery script :
          * 
          * <script language="Javascript"> var array = new Array();
          * $('td[title=Name]').each(function() { var text = $(this).text().trim(); if
          * (jQuery.inArray(text, array) == -1) { array.push(text); } });
          * 
          * var java = '<p>'; var length = array.length;
          * 
          * for (var i=0; i < length; i++) { java += 'add("' + array[i] + '");<br/>'; }
          * 
          * java += '</p>';
          * 
          * $('table').parent().html(java); </script>
          */
            add("abbr");
            add("accept-charset");
            add("accept");
            add("accesskey");
            add("action");
            add("align");
            add("alink");
            add("alt");
            add("archive");
            add("axis");
            add("background");
            add("bgcolor");
            add("border");
            add("cellpadding");
            add("cellspacing");
            add("char");
            add("charoff");
            add("charset");
            add("checked");
            add("cite");
            add("class");
            add("classid");
            add("clear");
            add("code");
            add("codebase");
            add("codetype");
            add("color");
            add("cols");
            add("colspan");
            add("compact");
            add("content");
            add("coords");
            add("data");
            add("datetime");
            add("declare");
            add("defer");
            add("dir");
            add("disabled");
            add("enctype");
            add("face");
            add("for");
            add("frame");
            add("frameborder");
            add("headers");
            add("height");
            add("href");
            add("hreflang");
            add("hspace");
            add("http-equiv");
            add("id");
            add("ismap");
            add("label");
            add("lang");
            add("language");
            add("link");
            add("longdesc");
            add("marginheight");
            add("marginwidth");
            add("maxlength");
            add("media");
            add("method");
            add("multiple");
            add("name");
            add("nohref");
            add("noresize");
            add("noshade");
            add("nowrap");
            add("object");
            add("onblur");
            add("onchange");
            add("onclick");
            add("ondblclick");
            add("onfocus");
            add("onkeydown");
            add("onkeypress");
            add("onkeyup");
            add("onload");
            add("onmousedown");
            add("onmousemove");
            add("onmouseout");
            add("onmouseover");
            add("onmouseup");
            add("onreset");
            add("onselect");
            add("onsubmit");
            add("onunload");
            add("profile");
            add("prompt");
            add("readonly");
            add("rel");
            add("rev");
            add("rows");
            add("rowspan");
            add("rules");
            add("scheme");
            add("scope");
            add("scrolling");
            add("selected");
            add("shape");
            add("size");
            add("span");
            add("src");
            add("standby");
            add("start");
            add("style");
            add("summary");
            add("tabindex");
            add("target");
            add("text");
            add("title");
            add("type");
            add("usemap");
            add("valign");
            add("value");
            add("valuetype");
            add("version");
            add("vlink");
            add("vspace");
            add("width");
        }
    };

    private static final String ELEM_PROPERTIES = "ELEM_PROPERTIES";
    private static final String IS_XML_ELEMENT = "IS_XML_ELEMENT";
    private static final String NODE_LIST_FIELD = "childNodes";
    private static final String NODE_LIST_INNER_LIST = "NODE_LIST_INNER_LIST";
    private static final String NODE_NAME = "nodeName";
    private static final String NODE_TYPE_FIELD = "nodeType";
    private static final String PARENT_NODE_FIELD = "parentNode";
    private static final String TAG_NAME = "tagName";

    public static JavaScriptObject cloneJso(JavaScriptObject oldJso, boolean deep) {
        JavaScriptObject newJso = JavaScriptObject.createObject();

        if (Node.is(oldJso)) {
            short nodeType = oldJso.<Node>cast().getNodeType();
            JavaScriptObjects.setProperty(newJso, NODE_TYPE_FIELD, nodeType);
        }

        for (Map.Entry<String, Object> entry : JavaScriptObjects.entrySet(oldJso)) {

            if (PARENT_NODE_FIELD.equals(entry.getKey())) {
                // Nothing to do : new cloned node does not have any parent
            } else if (NODE_TYPE_FIELD.equals(entry.getKey())) {
                // ignore it since it has to be handled at the very beginning of
                // cloning
            } else if (JsoProperties.NODE_OWNER_DOCUMENT.equals(entry.getKey())) {
                JavaScriptObjects.setProperty(newJso, JsoProperties.NODE_OWNER_DOCUMENT,
                        JavaScriptObjects.getObject(oldJso, JsoProperties.NODE_OWNER_DOCUMENT));
            } else if (GwtStyleUtils.STYLE_OBJECT_FIELD.equals(entry.getKey())) {
                Style newStyle = GwtStyleUtils.getStyle(newJso.<Element>cast());
                GwtStyleUtils.cloneStyle(newStyle, (Style) entry.getValue());
            } else if (NODE_LIST_FIELD.equals(entry.getKey())) {
                Node newNode = newJso.cast();
                Node oldNode = oldJso.cast();
                cloneChildNodes(newNode, oldNode, deep);
            } else if (ELEM_PROPERTIES.equals(entry.getKey())) {
                PropertyContainer newPc = getDomProperties(newJso.<Element>cast());
                PropertyContainer oldPc = getDomProperties(oldJso.<Element>cast());

                for (Map.Entry<String, Object> entry2 : oldPc.entrySet()) {
                    newPc.put(entry2.getKey(), entry2.getValue());
                }
            } else if (JavaScriptObject.class.isInstance(entry.getValue())) {
                JavaScriptObject oldChildJso = (JavaScriptObject) entry.getValue();
                JavaScriptObject newChildJso = cloneJso(oldChildJso, deep);
                JavaScriptObjects.setProperty(newJso, entry.getKey(), newChildJso);
            } else {
                // copy the property, which should be a String or a primitive type
                // (or
                // corresponding wrapper object)
                JavaScriptObjects.setProperty(newJso, entry.getKey(), entry.getValue());
            }
        }

        return newJso;

    }

    @SuppressWarnings("unchecked")
    public static List<Node> getChildNodeInnerList(Node node) {
        NodeList<Node> nodeList = getChildNodes(node);
        return (List<Node>) JavaScriptObjects.getObject(nodeList, NODE_LIST_INNER_LIST);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> List<T> getChildNodeInnerList(NodeList<T> nodeList) {
        assert isNodeList(nodeList) : "not a NodeList";

        return (List<T>) JavaScriptObjects.getObject(nodeList, NODE_LIST_INNER_LIST);
    }

    public static NodeList<Node> getChildNodes(Node node) {
        assert Node.is(node) : "not a Node";
        NodeList<Node> nodeList = JavaScriptObjects.getObject(node, NODE_LIST_FIELD);

        if (nodeList == null) {
            nodeList = newNodeList();
            JavaScriptObjects.setProperty(node, NODE_LIST_FIELD, nodeList);
        }

        return nodeList;
    }

    public static PropertyContainer getDomProperties(Element element) {
        assert Node.is(element) : "not a Node";

        PropertyContainer pc = JavaScriptObjects.getObject(element, ELEM_PROPERTIES);

        if (pc == null) {
            // a propertyContainer with a LinkedHashMap to record the order of DOM
            // properties
            pc = PropertyContainer.newInstance(new LinkedHashMap<String, Object>());
            JavaScriptObjects.setProperty(element, ELEM_PROPERTIES, pc);
        }

        return pc;
    }

    public static short getNodeType(JavaScriptObject jso) {
        return JavaScriptObjects.hasProperty(jso, NODE_TYPE_FIELD) ? JavaScriptObjects.getShort(jso,
                NODE_TYPE_FIELD) : -1;
    }

    public static String getTagName(Element element) {
        assert Element.is(element) : "not an Element";

        return JavaScriptObjects.getString(element, TAG_NAME);
    }

    public static boolean isNodeList(JavaScriptObject jso) {
        return JavaScriptObjects.hasProperty(jso, NODE_LIST_INNER_LIST);
    }

    public static boolean isStandardDOMProperty(String propertyName) {
        return "className".equals(propertyName) || !"class".equals(propertyName)
                && DOM_PROPERTIES.contains(propertyName);
    }

    public static boolean isXmlElement(JavaScriptObject jso) {
        return JavaScriptObjects.hasProperty(jso, IS_XML_ELEMENT);
    }

    public static Document newDocument() {
        Document document = newNode(Node.DOCUMENT_NODE).cast();
        JavaScriptObjects.setProperty(document, JsoProperties.NODE_OWNER_DOCUMENT, document);
        return document;

    }

    public static Element newElement(String tag, Document ownerDocument) {
        Element elem = newNode(Node.ELEMENT_NODE).cast();
        JavaScriptObjects.setProperty(elem, TAG_NAME, tag);
        JavaScriptObjects.setProperty(elem, JsoProperties.NODE_OWNER_DOCUMENT, ownerDocument);

        if (tag.equalsIgnoreCase("html")) {
            JavaScriptObjects.setProperty(elem, NODE_NAME, "HTML");
        }

        return elem;
    }

    public static Node newNode(int nodeType) {
        Node newNode = JavaScriptObject.createObject().cast();
        JavaScriptObjects.setProperty(newNode, NODE_TYPE_FIELD, (short) nodeType);
        return newNode;
    }

    public static <T extends Node> NodeList<T> newNodeList() {
        return newNodeList(new ArrayList<>());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Node> NodeList<T> newNodeList(List<T> innerList) {
        NodeList<T> nodeList = JavaScriptObject.createObject().cast();
        JavaScriptObjects.setProperty(nodeList, NODE_LIST_INNER_LIST, innerList);
        return nodeList;
    }

    public static Text newText(String data, Document ownerDocument) {
        Text text = newNode(Node.TEXT_NODE).cast();
        JavaScriptObjects.setProperty(text, JsoProperties.NODE_OWNER_DOCUMENT, ownerDocument);
        text.setData(data);

        return text;
    }

    public static Element newXmlElement(Document document, String tagName) {
        Element xmlElement = document.createElement(tagName);
        JavaScriptObjects.setProperty(xmlElement, IS_XML_ELEMENT, true);

        return xmlElement;
    }

    public static void onSetHTML(JavaScriptObject jso, String newHTML, String oldHTML) {
        Widget w = WidgetUtils.getWidget(jso.<Element>cast());

        if (w != null) {
            GwtReflectionUtils.callStaticMethod(GwtFinder.class, "onSetHTML", w, newHTML, oldHTML);
        }

    }

    public static void onSetId(JavaScriptObject jso, String newId, String oldId) {
        Widget w = WidgetUtils.getWidget(jso.<Element>cast());

        if (w != null) {
            GwtReflectionUtils.callStaticMethod(GwtFinder.class, "onSetId", w, newId, oldId);
        }
    }

    public static void onSetText(JavaScriptObject jso, String newText, String oldText) {
        Widget w = WidgetUtils.getWidget(jso.<Element>cast());

        if (w != null) {
            GwtReflectionUtils.callStaticMethod(GwtFinder.class, "onSetText", w, newText, oldText);
        }
    }

    public static String serialize(JavaScriptObject jso) {
        if (Node.is(jso)) {
            return jso.toString();
        }

        StringBuilder sb = new StringBuilder();
        // FIXME : provide support for JavaScriptObject arrays
        sb.append("{ ");

        for (Map.Entry<String, Object> entry : JavaScriptObjects.entrySet(jso)) {
            sb.append("\"").append(entry.getKey()).append("\": ");
            sb.append(entry.getValue()).append(", ");
        }

        sb.replace(sb.length() - 2, sb.length(), "");
        sb.append(" }");

        return sb.toString();

    }

    public static void setParentNode(Node child, Node parent) {
        JavaScriptObjects.setProperty(child, PARENT_NODE_FIELD, parent);
    }

    private static void cloneChildNodes(Node newNode, Node oldNode, boolean deep) {

        List<Node> childs = getChildNodeInnerList(oldNode);
        if (deep) {
            // copy all child nodes
            for (Node child : childs) {
                newNode.appendChild(child.cloneNode(deep));
            }
        } else {
            // only copy the TextNode if exists
            for (Node child : childs) {
                if (Node.TEXT_NODE == child.getNodeType()) {
                    newNode.appendChild(Document.get().createTextNode(child.getNodeValue()));
                    break;
                }
            }
        }
    }

}
