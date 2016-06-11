package com.googlecode.gwt.test.uibinder.widget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.test.exceptions.GwtTestUiBinderException;
import com.googlecode.gwt.test.uibinder.UiBinderXmlUtils;
import com.googlecode.gwt.test.uibinder.UiObjectTag;
import com.googlecode.gwt.test.uibinder.UiObjectTagFactory;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

public class UiDisclosurePanelTagFactory implements UiObjectTagFactory<DisclosurePanel> {

    private static class UiDisclosurePanelTag extends UiObjectTag<DisclosurePanel> {

        private static ClientBundle DEFAULT_IMAGES = GwtReflectionUtils.getStaticFieldValue(
                DisclosurePanel.class, "DEFAULT_IMAGES");

        @Override
        protected void appendElement(DisclosurePanel wrapped, Element element, String namespaceURI,
                                     List<IsWidget> childWidgets) {

            if ("header".equals(element.getTagName())
                    && UiBinderXmlUtils.CLIENTUI_NSURI.equals(namespaceURI)) {
                Widget headerWidget = getTextHeaderWidget(element, wrapped);
                wrapped.setHeader(headerWidget);
            } else if ("customHeader".equals(element.getTagName())
                    && UiBinderXmlUtils.CLIENTUI_NSURI.equals(namespaceURI)) {
                Widget headerWidget = getCustomHeaderWidget(childWidgets);
                wrapped.setHeader(headerWidget);
            } else {
                // delegate to the default implementation, which should throw an
                // "unexpected element" exception
                super.appendElement(wrapped, element, namespaceURI, childWidgets);
            }
        }

        @Override
        protected void finalizeObject(DisclosurePanel uiObject) {
            // nothing to do
        }

        @Override
        protected void initializeObject(DisclosurePanel wrapped, Map<String, Object> attributes,
                                        Object owner) {
            // nothing to do
        }

        private Widget getCustomHeaderWidget(List<IsWidget> childWidgets) {
            switch (childWidgets.size()) {
                case 0:
                    throw new GwtTestUiBinderException(
                            "Error while setting a customHeader to a UiBinder DisclosurePanel : no widget added");
                case 1:
                    return childWidgets.get(0).asWidget();
                default:
                    throw new GwtTestUiBinderException(
                            "Error while setting a customHeader to a UiBinder DisclosurePanel : too many widgets ("
                                    + childWidgets.size() + ")");
            }
        }

        @SuppressWarnings("unchecked")
        private Widget getTextHeaderWidget(Element textHeaderElement, DisclosurePanel parent) {
            try {
                Class<Widget> disclosurePanelClass = (Class<Widget>) Class.forName("com.google.gwt.user.client.ui.DisclosurePanel$DefaultHeader");

                ImageResource disclosurePanelOpen = GwtReflectionUtils.callPrivateMethod(
                        DEFAULT_IMAGES, "disclosurePanelOpen");
                ImageResource disclosurePanelClosed = GwtReflectionUtils.callPrivateMethod(
                        DEFAULT_IMAGES, "disclosurePanelClosed");
                String text = textHeaderElement.getInnerText();

                Constructor<Widget> ctr = disclosurePanelClass.getDeclaredConstructor(
                        DisclosurePanel.class, ImageResource.class, ImageResource.class, String.class);

                return GwtReflectionUtils.instantiateClass(ctr, parent, disclosurePanelOpen,
                        disclosurePanelClosed, text);
            } catch (Exception e) {
                // should never happen
                throw new GwtTestUiBinderException(
                        "Error while instanciating DisclosurePanel.DefaultHeader to handle <header> tag",
                        e);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.uibinder.UiObjectTagFactory#createUiObjectTag(java .lang.Class,
     * java.util.Map)
     */
    public UiObjectTag<DisclosurePanel> createUiObjectTag(Class<?> clazz,
                                                          Map<String, Object> attributes) {
        if (!DisclosurePanel.class.isAssignableFrom(clazz)) {
            return null;
        }

        return new UiDisclosurePanelTag();
    }

}
