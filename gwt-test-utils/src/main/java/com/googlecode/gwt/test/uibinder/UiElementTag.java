package com.googlecode.gwt.test.uibinder;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Text;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.gwt.test.exceptions.ReflectionException;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles all HTML element tags declared in a .ui.xml file.
 *
 * @author Gael Lazzari
 */
class UiElementTag implements UiTag<Element> {

    static final String UIBINDER_CHILD_WIDGETS_LIST = "UIBINDER_CHILD_WIDGETS_LIST";
    static final String UIBINDER_XML_NAMESPACE = "UIBINDER_XML_NAMESPACE";

    private static final String UIBINDER_CHILD_UIOBJECT_LIST = "UIBINDER_CHILD_UIOBJECT_LIST";

    private final UiTag<?> parentTag;
    private final Element wrapped;

    UiElementTag(String nsURI, String tagName, Map<String, Object> attributes, UiTag<?> parentTag,
                 Object owner) {
        this.wrapped = JsoUtils.newElement(tagName, Document.get());
        this.parentTag = parentTag;

        JavaScriptObjects.setProperty(wrapped, UIBINDER_XML_NAMESPACE, nsURI);

        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            String attrName = entry.getKey();
            String attrValue = entry.getValue().toString().trim();

            if ("ui:field".equals(attrName)) {
                try {
                    GwtReflectionUtils.setPrivateFieldValue(owner, attrValue, this.wrapped);
                } catch (ReflectionException e) {
                    // ui:field has no corresponding @UiField declared : just ignore
                    // it
                }
            } else if ("class".equalsIgnoreCase(attrName)) {
                this.wrapped.setAttribute("class", UiBinderXmlUtils.getEffectiveStyleName(attrValue));
            } else {
                this.wrapped.setAttribute(attrName, attrValue);
            }
        }
    }

    public final void addElement(Element element) {
        appendElement(wrapped, element);

    }

    public final void addUiObject(UIObject uiObject) {
        appendUiObject(wrapped, uiObject);

    }

    public void addWidget(IsWidget widget) {
        addWidget(this.wrapped, widget);

    }

    public final void appendText(String data) {
        if (!"".equals(data.trim())) {
            appendText(this.wrapped, data);
        }
    }

    public Element endTag() {
        return this.wrapped;
    }

    public UiTag<?> getParentTag() {
        return parentTag;
    }

    protected void addWidget(Element wrapped, IsWidget isWidget) {
        List<IsWidget> childWidgets = JavaScriptObjects.getObject(wrapped,
                UIBINDER_CHILD_WIDGETS_LIST);

        if (childWidgets == null) {
            childWidgets = new ArrayList<>();
            JavaScriptObjects.setProperty(wrapped, UIBINDER_CHILD_WIDGETS_LIST, childWidgets);
        }

        childWidgets.add(isWidget);
        appendElement(wrapped, isWidget.asWidget().getElement());
    }

    protected void appendElement(Element wrapped, Element child) {
        wrapped.appendChild(child);
    }

    protected void appendText(Element wrapped, String data) {
        Text text = JsoUtils.newText(data, wrapped.getOwnerDocument());
        wrapped.appendChild(text);
    }

    protected void appendUiObject(Element wrapped2, UIObject uiObject) {
        List<UIObject> childObjects = JavaScriptObjects.getObject(wrapped,
                UIBINDER_CHILD_UIOBJECT_LIST);

        if (childObjects == null) {
            childObjects = new ArrayList<>();
            JavaScriptObjects.setProperty(wrapped, UIBINDER_CHILD_UIOBJECT_LIST, childObjects);
        }

        childObjects.add(uiObject);
        appendElement(wrapped, uiObject.getElement());

    }

}
