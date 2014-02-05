package com.googlecode.gwt.test.internal.patchers.dom;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.googlecode.gwt.test.internal.utils.GwtHtmlParser;
import com.googlecode.gwt.test.internal.utils.GwtStringUtils;
import com.googlecode.gwt.test.internal.utils.GwtStyleUtils;
import com.googlecode.gwt.test.internal.utils.JsoProperties;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.internal.utils.PropertyContainer;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

@PatchClass(Element.class)
class ElementPatcher {

   @PatchMethod
   static void blur(Element element) {

   }

   @PatchMethod
   static void focus(Element element) {

   }

   @PatchMethod
   static int getClientHeight(Element element) {
      return JavaScriptObjects.getInteger(element, JsoProperties.ELEMENT_CLIENT_HEIGHT);
   }

   @PatchMethod
   static int getClientWidth(Element element) {
      return JavaScriptObjects.getInteger(element, JsoProperties.ELEMENT_CLIENT_WIDTH);
   }

   @PatchMethod
   static NodeList<Element> getElementsByTagName(Element elem, String tagName) {
      return DocumentPatcher.getElementsByTagName(elem, tagName);
   }

   @PatchMethod
   static int getOffsetHeight(Element element) {
      return GwtStringUtils.parseInt(element.getStyle().getHeight(), 0);
   }

   @PatchMethod
   static int getOffsetLeft(Element element) {
      return GwtStringUtils.parseInt(element.getStyle().getLeft(), 0);
   }

   @PatchMethod
   static Element getOffsetParent(Element element) {
      if (element == null) {
         return null;
      }

      return element.getParentElement();
   }

   @PatchMethod
   static int getOffsetTop(Element element) {
      return GwtStringUtils.parseInt(element.getStyle().getTop(), 0);
   }

   @PatchMethod
   static int getOffsetWidth(Element element) {
      return GwtStringUtils.parseInt(element.getStyle().getWidth(), 0);
   }

   @PatchMethod
   static boolean getPropertyBoolean(Element element, String propertyName) {
      PropertyContainer properties = JsoUtils.getDomProperties(element);
      return properties.getBoolean(propertyName);
   }

   @PatchMethod
   static double getPropertyDouble(Element element, String propertyName) {
      PropertyContainer properties = JsoUtils.getDomProperties(element);
      return properties.getDouble(propertyName);
   }

   @PatchMethod
   static int getPropertyInt(Element element, String propertyName) {
      PropertyContainer properties = JsoUtils.getDomProperties(element);
      return properties.getInteger(propertyName);
   }

   @PatchMethod
   static JavaScriptObject getPropertyJSO(Element element, String propertyName) {
      return (JavaScriptObject) getPropertyObject(element, propertyName);
   }

   @PatchMethod
   static Object getPropertyObject(Element element, String propertyName) {
      if ("tagName".equals(propertyName)) {
         return element.getTagName().toUpperCase();
      } else if ("style".equals(propertyName)) {
         return element.getStyle();
      }

      PropertyContainer properties = JsoUtils.getDomProperties(element);

      return properties.getObject(propertyName);
   }

   @PatchMethod
   static String getPropertyString(Element element, String propertyName) {

      Object value = getPropertyObject(element, propertyName);

      // null (javascript undefined) is a possible value here if not a DOM
      // standard property
      if (value == null && JsoUtils.isStandardDOMProperty(propertyName)) {
         return "";
      } else if (value == null) {
         return null;
      } else {
         return value.toString();
      }

   }

   @PatchMethod
   static Style getStyle(Element element) {
      // mark the style as being modified
      PropertyContainer properties = JsoUtils.getDomProperties(element);
      properties.put("style", "");

      return GwtStyleUtils.getStyle(element);
   }

   @PatchMethod
   static void removeAttribute(Element element, String name) {
      PropertyContainer properties = JsoUtils.getDomProperties(element);
      String propertyName = DOMImplPatcher.getDOMPropertyName(name);
      properties.remove(propertyName);
   }

   @PatchMethod
   static void setAttribute(Element element, String attributeName, String value) {
      if (JsoProperties.ID.equals(attributeName)) {
         JsoUtils.onSetId(element, value, getPropertyString(element, JsoProperties.ID));
      }

      PropertyContainer properties = JsoUtils.getDomProperties(element);

      String propertyName = DOMImplPatcher.getDOMPropertyName(attributeName);

      properties.put(propertyName, value);
   }

   @PatchMethod
   static void setInnerHTML(Element element, String html) {
      // clear old childs
      List<Node> innerList = JsoUtils.getChildNodeInnerList(element);
      innerList.clear();

      // parse new childs
      NodeList<Node> nodes = GwtHtmlParser.parse(html);

      // append new childs
      for (int i = 0; i < nodes.getLength(); i++) {
         element.appendChild(nodes.getItem(i));
      }
   }

   @PatchMethod
   static void setPropertyBoolean(Element element, String name, boolean value) {
      setPropertyObject(element, name, value);
   }

   @PatchMethod
   static void setPropertyDouble(Element element, String name, double value) {
      setPropertyObject(element, name, value);
   }

   @PatchMethod
   static void setPropertyInt(Element element, String name, int value) {
      setPropertyObject(element, name, value);
   }

   @PatchMethod
   static void setPropertyJSO(Element element, String name, JavaScriptObject value) {
      setPropertyObject(element, name, value);
   }

   @PatchMethod
   static void setPropertyObject(Element element, String name, Object value) {

      if (JsoProperties.ID.equals(name)) {
         JsoUtils.onSetId(element, value.toString(), getPropertyString(element, JsoProperties.ID));
      }

      PropertyContainer properties = JsoUtils.getDomProperties(element);

      if ("style".equals(value)) {
         GwtStyleUtils.overrideStyle(element.getStyle(), value.toString());
         // add an empty style to preserve the insert order of DOM attribute in
         // the
         // wrapped LinkedHashMap
         properties.put(name, "");
      } else {
         properties.put(name, value);
      }
   }

   @PatchMethod
   static void setPropertyString(Element element, String name, String value) {
      setPropertyObject(element, name, value);
   }

   @PatchMethod
   static int toInt32(double val) {
      return Double.valueOf(val).intValue();
   }

}
