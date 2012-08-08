package com.googlecode.gwt.test.utils;

import com.google.gwt.dom.client.Element;
import com.googlecode.gwt.test.internal.utils.JsoProperties;

/**
 * Classe which provides utilies on GWT DOM stuff.
 * 
 * @author Gael Lazzari
 * 
 */
public class GwtDomUtils {

   /**
    * Manually set a DOM element height.
    * 
    * @param element The targeted element.
    * @param height The height value.
    */
   public static void setClientHeight(Element element, int height) {
      JavaScriptObjects.setProperty(element, JsoProperties.ELEMENT_CLIENT_HEIGHT, height);
   }

   /**
    * Manually set a DOM element width.
    * 
    * @param element The targeted element.
    * @param width The width value.
    */
   public static void setClientWidth(Element element, int width) {
      JavaScriptObjects.setProperty(element, JsoProperties.ELEMENT_CLIENT_WIDTH, width);
   }

}
