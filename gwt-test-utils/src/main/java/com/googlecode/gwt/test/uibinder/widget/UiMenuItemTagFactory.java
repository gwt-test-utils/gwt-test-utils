package com.googlecode.gwt.test.uibinder.widget;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.googlecode.gwt.test.uibinder.UiObjectTag;
import com.googlecode.gwt.test.uibinder.UiObjectTagFactory;

import java.util.Map;

/**
 * Handles &lt;g:MenuItem> tags.
 *
 * @author Gael Lazzari
 */
public class UiMenuItemTagFactory implements UiObjectTagFactory<MenuItem> {

    private static class UiMenuItemTag extends UiObjectTag<MenuItem> {

        @Override
        protected void addWidget(MenuItem wrapped, IsWidget isWidget) {
            if (isWidget instanceof MenuBar) {
                wrapped.setSubMenu((MenuBar) isWidget);
            } else {
                super.addWidget(wrapped, isWidget);
            }
        }

        @Override
        protected void finalizeObject(MenuItem uiObject) {
            // nothing to do
        }

        @Override
        protected void initializeObject(MenuItem wrapped, Map<String, Object> attributes, Object owner) {
            // nothing to do
        }

        @Override
        protected MenuItem instanciate(Class<? extends MenuItem> clazz,
                                       Map<String, Object> attributes, Object owner) {

            if (clazz == MenuItem.class) {
                return new MenuItem(SafeHtmlUtils.fromSafeConstant(""));
            }
            return super.instanciate(clazz, attributes, owner);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.uibinder.UiObjectTagFactory#createUiObjectTag( java.lang .Class,
     * java.util.Map)
     */
    public UiObjectTag<MenuItem> createUiObjectTag(Class<?> clazz, Map<String, Object> attributes) {

        if (MenuItem.class.isAssignableFrom(clazz)) {
            return new UiMenuItemTag();
        }

        return null;
    }

}
