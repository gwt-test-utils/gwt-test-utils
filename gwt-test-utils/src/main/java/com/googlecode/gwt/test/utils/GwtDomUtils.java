package com.googlecode.gwt.test.utils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.gwt.test.internal.utils.JsoProperties;

/**
 * Classe which provides utilies on GWT DOM stuff.
 *
 * @author Gael Lazzari
 */
public class GwtDomUtils {

    /**
     * Gets all of the element's style names, as a space-separated list.
     *
     * @param elem the element whose style is to be retrieved
     * @return the objects's space-separated style names
     */
    public static String getStyleName(Element element) {
        return element.getPropertyString("className");
    }

    /**
     * Gets the element's primary style name.
     *
     * @param elem the element whose primary style name is to be retrieved
     * @return the element's primary style name
     */
    public static String getStylePrimaryName(Element elem) {
        // copied from protected static UiObject method
        String fullClassName = getStyleName(elem);

        // The primary style name is always the first token of the full CSS class
        // name. There can be no leading whitespace in the class name, so it's not
        // necessary to trim() it.
        int spaceIdx = fullClassName.indexOf(' ');
        if (spaceIdx >= 0) {
            return fullClassName.substring(0, spaceIdx);
        }
        return fullClassName;
    }

    public static boolean hasStyle(Element element, String styleName) {
        return getStyleName(element).contains(styleName);
    }

    public static boolean isVisible(Element element) {
        if (!UIObject.isVisible(element)) {
            return false;
        } else if (element.getParentElement() != null) {
            return isVisible(element.getParentElement());
        } else {
            return true;
        }
    }

    /**
     * Manually set a DOM element height.
     *
     * @param element The targeted element.
     * @param height  The height value.
     */
    public static void setClientHeight(Element element, int height) {
        JavaScriptObjects.setProperty(element, JsoProperties.ELEMENT_CLIENT_HEIGHT, height);
    }

    /**
     * Manually set a DOM element width.
     *
     * @param element The targeted element.
     * @param width   The width value.
     */
    public static void setClientWidth(Element element, int width) {
        JavaScriptObjects.setProperty(element, JsoProperties.ELEMENT_CLIENT_WIDTH, width);
    }

}
