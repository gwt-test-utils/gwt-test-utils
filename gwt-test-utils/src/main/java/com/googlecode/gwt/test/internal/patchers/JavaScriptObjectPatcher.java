package com.googlecode.gwt.test.internal.patchers;

import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dev.shell.JsValueGlue;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Text;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.internal.utils.GwtStyleUtils;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.internal.utils.PropertyContainer;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(JavaScriptObject.class)
class JavaScriptObjectPatcher {

   @PatchMethod
   static JavaScriptObject createArray() {
      return createObject();
   }

   @PatchMethod
   static JavaScriptObject createFunction() {
      return createObject();
   }

   @PatchMethod
   static JavaScriptObject createObject() {
      try {
         Class<?> clazz = Class.forName(JsValueGlue.JSO_IMPL_CLASS);
         return (JavaScriptObject) GwtReflectionUtils.instantiateClass(clazz);
      } catch (Exception e) {
         // should never happen
         throw new GwtTestPatchException("Error while instanciating JavaScriptObject :", e);
      }
   }

   @PatchMethod
   static String toString(JavaScriptObject jso) {
      short nodeType = jso.<Node> cast().getNodeType();

      switch (nodeType) {
         case Node.DOCUMENT_NODE:
            return documentToString(jso.<Document> cast());
         case Node.TEXT_NODE:
            Text text = jso.cast();
            return "'" + text.getData() + "'";
         case Node.ELEMENT_NODE:
            return elementToString(jso.<Element> cast());
         default:
            if (JsoUtils.isNodeList(jso)) {
               NodeList<?> nodeList = jso.cast();
               return JsoUtils.getChildNodeInnerList(nodeList).toString();
            } else if (GwtStyleUtils.isStyle(jso)) {
               Style style = jso.cast();
               return GwtStyleUtils.toString(style);
            } else {
               return jso.getClass().getSimpleName();
            }

      }

   }

   private static String documentToString(Document document) {
      StringBuilder html = new StringBuilder();
      NodeList<Node> childs = document.getChildNodes();

      for (int i = 0; i < childs.getLength(); i++) {
         Node child = childs.getItem(i);
         html.append(child.toString());
      }

      return html.toString();
   }

   private static String elementToString(Element elem) {
      String tagName = JsoUtils.isXmlElement(elem) ? elem.getTagName()
               : elem.getTagName().toLowerCase();

      // handle the particular case of <br> element
      if ("br".equals(tagName)) {
         return "<br>" + elem.getInnerText();
      }

      StringBuilder sb = new StringBuilder();
      sb.append("<").append(tagName).append(" ");

      PropertyContainer attrs = JsoUtils.getDomProperties(elem);
      for (Map.Entry<String, Object> entry : attrs.entrySet()) {
         // special treatment for "disabled" property, which should be a empty
         // string attribute if the DOM element is disabled
         if ("disabled".equals(entry.getKey())) {
            Boolean disabled = (Boolean) entry.getValue();
            if (disabled.booleanValue()) {
               sb.append(entry.getKey()).append("=\"\" ");
            }
         } else if ("className".equals(entry.getKey())) {
            // special treatment for "className", which is mapped with DOM
            // standard
            // property "class"
            sb.append("class=\"").append(entry.getValue()).append("\" ");
         } else if ("style".equals(entry.getKey())) {
            String style = elem.getStyle().toString();
            if (!"".equals(style)) {
               sb.append("style=\"").append(elem.getStyle().toString()).append("\" ");
            }
         } else {
            sb.append(entry.getKey()).append("=\"").append(entry.getValue()).append("\" ");
         }
      }
      // remove the last space character
      sb.replace(sb.length() - 1, sb.length(), "");

      sb.append(">").append(elem.getInnerHTML());
      sb.append("</").append(tagName).append(">");
      return sb.toString();
   }
}
