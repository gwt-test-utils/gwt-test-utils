package com.googlecode.gwt.test.uibinder.widget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.googlecode.gwt.test.uibinder.UiObjectTag;
import com.googlecode.gwt.test.uibinder.UiObjectTagFactory;

import java.util.List;
import java.util.Map;

/**
 * Handles &lt;g:HTMLPanel> tags.
 *
 * @author Gael Lazzari
 */
public class UiHTMLPanelTagFactory implements UiObjectTagFactory<HTMLPanel> {

    private static class UiHTMLPanelTag extends UiObjectTag<HTMLPanel> {

        @Override
        protected void appendElement(HTMLPanel wrapped, Element element, String namespaceURI,
                                     List<IsWidget> childWidgets) {

            getElement(wrapped).appendChild(element);
        }

        @Override
        protected void finalizeObject(HTMLPanel widget) {
            // nothing to do
        }

        @Override
        protected void initializeObject(HTMLPanel wrapped, Map<String, Object> attributes,
                                        Object owner) {
            // nothing to do
        }

        @Override
        protected HTMLPanel instanciate(Class<? extends HTMLPanel> clazz,
                                        Map<String, Object> attributes, Object owner) {

            if (clazz == HTMLPanel.class) {
                return new HTMLPanel("");
            }

            // use default instanciation system
            return super.instanciate(clazz, attributes, owner);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.uibinder.UiObjectTagFactory#createUiObjectTag (java.lang.Class,
     * java.util.Map)
     */
    public UiObjectTag<HTMLPanel> createUiObjectTag(Class<?> clazz, Map<String, Object> attributes) {

        if (HTMLPanel.class.isAssignableFrom(clazz)) {
            return new UiHTMLPanelTag();
        }

        return null;
    }

}
