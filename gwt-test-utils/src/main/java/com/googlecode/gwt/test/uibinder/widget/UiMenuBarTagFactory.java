package com.googlecode.gwt.test.uibinder.widget;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.gwt.test.uibinder.UiObjectTag;
import com.googlecode.gwt.test.uibinder.UiObjectTagFactory;

import java.util.Map;

/**
 * Handles &lt;g:MenuBar> tags.
 *
 * @author Gael Lazzari
 */
public class UiMenuBarTagFactory implements UiObjectTagFactory<MenuBar> {

    private static class UiMenuBarTag extends UiObjectTag<MenuBar> {

        @Override
        protected void addUIObject(MenuBar wrapped, UIObject uiObject) {
            if (uiObject instanceof MenuItem) {
                wrapped.addItem((MenuItem) uiObject);
            } else {
                super.addUIObject(wrapped, uiObject);
            }
        }

        @Override
        protected void finalizeObject(MenuBar widget) {
            // nothing to do
        }

        @Override
        protected void initializeObject(MenuBar wrapped, Map<String, Object> attributes, Object owner) {
            // nothing to do
        }

        @Override
        protected MenuBar instanciate(Class<? extends MenuBar> clazz, Map<String, Object> attributes,
                                      Object owner) {

            if (clazz != MenuBar.class) {
                // use default instanciation system
                return super.instanciate(clazz, attributes, owner);
            }

            String vertical = (String) attributes.get("vertical");
            boolean isVertical = vertical != null ? Boolean.valueOf(vertical) : false;

            return new MenuBar(isVertical);

        }

    }

    public UiObjectTag<MenuBar> createUiObjectTag(Class<?> clazz, Map<String, Object> attributes) {

        if (MenuBar.class.isAssignableFrom(clazz)) {
            return new UiMenuBarTag();
        }

        return null;

    }

}
