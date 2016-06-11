package com.googlecode.gwt.test.uibinder;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base interface for a XML tag in a .ui.xml file. An UiTag should wrap an object which correspond
 * to a ui element declaration.
 *
 * @param <T> The type of the wrapped object.
 * @author Gael Lazzari
 */
public interface UiTag<T> {

    /**
     * Appends a DOM child element to this UiBinder tag.
     *
     * @param element The element to append
     */
    void addElement(Element element);

    /**
     * Adds a child UIObject which isn't a {@link Widget} to this UiBinder tag.
     */
    void addUiObject(UIObject uiObject);

    /**
     * Adds a child widget to this UiBinder tag.
     *
     * @param widget The widget to add
     */
    void addWidget(IsWidget widget);

    /**
     * Append text to this UiBinder tag.
     *
     * @param text The text to append
     */
    void appendText(String text);

    /**
     * Callback method called when the UiBinder tag is closed, so implementation could apply some
     * custom configuration if necessary.
     *
     * @return The UiBinder tag's wrapped object (Widget, Resource, DOM element...)
     */
    T endTag();

    /**
     * Get the parent UiBinder tag.
     *
     * @return The parent UiBinder tag
     */
    UiTag<?> getParentTag();

}
