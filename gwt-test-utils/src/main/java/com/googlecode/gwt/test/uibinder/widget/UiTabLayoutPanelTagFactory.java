package com.googlecode.gwt.test.uibinder.widget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.googlecode.gwt.test.exceptions.GwtTestUiBinderException;
import com.googlecode.gwt.test.uibinder.UiBinderXmlUtils;
import com.googlecode.gwt.test.uibinder.UiObjectTag;
import com.googlecode.gwt.test.uibinder.UiObjectTagFactory;

import java.util.List;
import java.util.Map;

/**
 * Handles &lt;g:TabLayoutPanel> tags.
 *
 * @author Gael Lazzari
 */
public class UiTabLayoutPanelTagFactory implements UiObjectTagFactory<TabLayoutPanel> {

    private static class UiTabLayoutPanelTag extends UiObjectTag<TabLayoutPanel> {

        @Override
        protected void appendElement(TabLayoutPanel wrapped, Element element, String namespaceURI,
                                     List<IsWidget> childWidgets) {

            if (!UiBinderXmlUtils.CLIENTUI_NSURI.equals(namespaceURI)) {
                super.appendElement(wrapped, element, namespaceURI, childWidgets);
            } else {
                handleTabLayoutPanelSpecifics(wrapped, element, childWidgets);
            }
        }

        @Override
        protected void finalizeObject(TabLayoutPanel widget) {
            // nothing to do
        }

        @Override
        protected void initializeObject(TabLayoutPanel wrapped, Map<String, Object> attributes,
                                        Object owner) {
            // nothing to do
        }

        @Override
        protected TabLayoutPanel instanciate(Class<? extends TabLayoutPanel> clazz,
                                             Map<String, Object> attributes, Object owner) {

            if (clazz == TabLayoutPanel.class) {
                String barHeight = (String) attributes.get("barHeight");

                if (barHeight == null) {
                    throw new GwtTestUiBinderException(
                            "Missing mandatory attribute 'barHeight' in a TabLayoutPanel declared in "
                                    + owner.getClass().getSimpleName() + ".ui.xml file");
                }

                String unit = (String) attributes.get("unit");
                Unit styleUnit = unit != null ? Unit.valueOf(unit) : Unit.PX;

                return new TabLayoutPanel(Double.valueOf(barHeight), styleUnit);
            }

            // use default instanciation system
            return super.instanciate(clazz, attributes, owner);
        }

        private void handleTabLayoutPanelSpecifics(TabLayoutPanel wrapped, Element element,
                                                   List<IsWidget> childWidgets) {

            if ("tab".equals(element.getTagName())) {
                NodeList<Element> headers = element.getElementsByTagName("header");
                if (headers.getLength() == 1 && childWidgets.size() == 1) {
                    // case of "header"
                    String header = headers.getItem(0).getInnerHTML();
                    wrapped.add(childWidgets.get(0), header);
                } else {
                    NodeList<Element> customHeaders = element.getElementsByTagName("customHeader");
                    if (customHeaders.getLength() == 1 && childWidgets.size() == 1) {
                        // case of "customHeader"
                        List<IsWidget> customHeaderChilds = UiBinderXmlUtils.getChildWidgets(customHeaders.getItem(0));
                        if (customHeaderChilds.size() == 1) {
                            wrapped.add(childWidgets.get(0), customHeaderChilds.get(0));
                        }
                    }
                }
            }
        }

    }

    public UiObjectTag<TabLayoutPanel> createUiObjectTag(Class<?> clazz,
                                                         Map<String, Object> attributes) {

        if (TabLayoutPanel.class.isAssignableFrom(clazz)) {
            return new UiTabLayoutPanelTag();
        }

        return null;
    }

}
