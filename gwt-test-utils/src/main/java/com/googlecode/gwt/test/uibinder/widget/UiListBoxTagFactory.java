package com.googlecode.gwt.test.uibinder.widget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.googlecode.gwt.test.uibinder.UiBinderXmlUtils;
import com.googlecode.gwt.test.uibinder.UiObjectTag;
import com.googlecode.gwt.test.uibinder.UiObjectTagFactory;

import java.util.List;
import java.util.Map;

public class UiListBoxTagFactory implements UiObjectTagFactory<ListBox> {

    private static class UiListBoxTag extends UiObjectTag<ListBox> {

        @Override
        protected void appendElement(ListBox wrapped, Element element, String namespaceURI,
                                     List<IsWidget> childWidgets) {
            if (!ITEM_TAG.equals(element.getTagName())
                    || !UiBinderXmlUtils.CLIENTUI_NSURI.equals(namespaceURI)) {
                super.appendElement(wrapped, element, namespaceURI, childWidgets);
            } else {
                // handle <g:item> tag
                String item = element.getInnerHTML();
                String value = element.getAttribute("value");
                wrapped.addItem(item, value);
            }

        }

        @Override
        protected void finalizeObject(ListBox uiObject) {
            // nothing to do
        }

        @Override
        protected void initializeObject(ListBox wrapped, Map<String, Object> attributes, Object owner) {
            // nothing to do
        }

    }

    private static final String ITEM_TAG = "item";

    public UiObjectTag<ListBox> createUiObjectTag(Class<?> clazz, Map<String, Object> attributes) {

        if (!ListBox.class.isAssignableFrom(clazz)) {
            return null;
        }

        return new UiListBoxTag();
    }

}
