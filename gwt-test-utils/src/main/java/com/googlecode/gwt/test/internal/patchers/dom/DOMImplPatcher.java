package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.googlecode.gwt.test.internal.utils.*;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.JavaScriptObjects;
import com.googlecode.gwt.test.utils.events.EventBuilder;

import java.util.ArrayList;
import java.util.List;

@PatchClass(target = "com.google.gwt.dom.client.DOMImpl")
class DOMImplPatcher {

    private static final String SCROLL_LEFT = "scrollLeft";
    private static final String TAB_INDEX = "tabIndex";

    @PatchMethod
    static void buttonClick(Object domImpl, ButtonElement button) {

        EventTarget relatedTarget = button.cast();
        Event onMouseOver = EventBuilder.create(Event.ONMOUSEOVER).setTarget(button).setRelatedTarget(
                relatedTarget).build();
        dispatchEvent(domImpl, button, onMouseOver);

        Event onMouseDown = EventBuilder.create(Event.ONMOUSEDOWN).setTarget(button).setRelatedTarget(
                relatedTarget).setButton(Event.BUTTON_LEFT).build();
        dispatchEvent(domImpl, button, onMouseDown);

        Event onMouseUp = EventBuilder.create(Event.ONMOUSEUP).setTarget(button).setRelatedTarget(
                relatedTarget).setButton(Event.BUTTON_LEFT).build();
        dispatchEvent(domImpl, button, onMouseUp);

        Event onClick = EventBuilder.create(Event.ONCLICK).setTarget(button).setRelatedTarget(
                relatedTarget).build();
        dispatchEvent(domImpl, button, onClick);
    }

    @PatchMethod
    static ButtonElement createButtonElement(Object domImpl, Document doc, String type) {
        ButtonElement e = doc.createElement("button").cast();

        e.setAttribute("type", type);
        return e;
    }

    @PatchMethod
    static InputElement createCheckInputElement(Object domImpl, Document doc) {
        InputElement e = createInputElement(doc, "checkbox", null);
        e.setValue("on");

        return e;
    }

    @PatchMethod
    static Element createElement(Object domImpl, Document doc, String tag) {
        return JsoUtils.newElement(tag, doc);
    }

    @PatchMethod
    static NativeEvent createHtmlEvent(Object domImpl, Document doc, String type, boolean canBubble,
                                       boolean cancelable) {

        int typeInt = EventUtils.getEventTypeInt(type);
        return EventBuilder.create(typeInt).setCanBubble(canBubble).build();
    }

    @PatchMethod
    static InputElement createInputElement(Object domImpl, Document doc, String type) {
        return createInputElement(doc, type, null);
    }

    @PatchMethod
    static InputElement createInputRadioElement(Object domImpl, Document doc, String name) {
        return createInputElement(doc, "RADIO", name);
    }

    @PatchMethod
    static NativeEvent createKeyCodeEvent(Object domImpl, Document document, String type,
                                          boolean ctrlKey, boolean altKey, boolean shiftKey, boolean metaKey, int keyCode) {

        int typeInt = EventUtils.getEventTypeInt(type);
        return EventBuilder.create(typeInt).setCtrlKey(ctrlKey).setAltKey(altKey).setShiftKey(
                shiftKey).setMetaKey(metaKey).setKeyCode(keyCode).build();

    }

    @PatchMethod
    static NativeEvent createKeyPressEvent(Object domImpl, Document document, boolean ctrlKey,
                                           boolean altKey, boolean shiftKey, boolean metaKey, int charCode) {

        return EventBuilder.create(Event.ONKEYPRESS).setCtrlKey(ctrlKey).setAltKey(altKey).setShiftKey(
                shiftKey).setMetaKey(metaKey).setKeyCode(charCode).build();
    }

    @PatchMethod
    static NativeEvent createMouseEvent(Object domImpl, Document doc, String type,
                                        boolean canBubble, boolean cancelable, int detail, int screenX, int screenY,
                                        int clientX, int clientY, boolean ctrlKey, boolean altKey, boolean shiftKey,
                                        boolean metaKey, int button, Element relatedTarget) {

        int typeInt = EventUtils.getEventTypeInt(type);

        return EventBuilder.create(typeInt).setCtrlKey(ctrlKey).setAltKey(altKey).setShiftKey(
                shiftKey).setMetaKey(metaKey).setButton(button).setTarget(relatedTarget).build();
    }

    @PatchMethod
    static void cssClearOpacity(Object domImpl, Style style) {
        style.setProperty("opacity", "");
    }

    @PatchMethod
    static void cssSetOpacity(Object domImpl, Style style, double value) {
        double modulo = value % 1;
        String stringValue = modulo == 0 ? String.valueOf((int) value) : String.valueOf(value);
        style.setProperty("opacity", stringValue);
    }

    @PatchMethod
    static void dispatchEvent(Object domImpl, Element target, NativeEvent evt) {
        EventListener listener = DOM.getEventListener(target);
        if (listener != null && evt instanceof Event) {
            listener.onBrowserEvent((Event) evt);
        }

        // dispatch to parent if needed
        boolean propagationStopped = JavaScriptObjects.getBoolean(evt, JsoProperties.EVENT_IS_STOPPED);
        if (target.getParentElement() != null && propagationStopped) {
            target.getParentElement().dispatchEvent(evt);
        }
    }

    @PatchMethod
    static boolean eventGetAltKey(Object domImpl, NativeEvent evt) {
        return JavaScriptObjects.getBoolean(evt, JsoProperties.EVENT_KEY_ALT);
    }

    @PatchMethod
    static int eventGetButton(Object domImpl, NativeEvent evt) {
        return JavaScriptObjects.getInteger(evt, JsoProperties.EVENT_BUTTON);
    }

    @PatchMethod
    static int eventGetCharCode(Object domImpl, NativeEvent evt) {
        // FIXME : wrong : GetCharCode = ASCII, evt.getKeyCode =
        // http://www.cambiaresearch.com/articles/15/javascript-char-codes-key-codes
        return evt.getKeyCode();
    }

    @PatchMethod
    static int eventGetClientX(Object domImpl, NativeEvent evt) {
        return JavaScriptObjects.getInteger(evt, JsoProperties.EVENT_MOUSE_CLIENTX);
    }

    @PatchMethod
    static int eventGetClientY(Object domImpl, NativeEvent evt) {
        return JavaScriptObjects.getInteger(evt, JsoProperties.EVENT_MOUSE_CLIENTY);
    }

    @PatchMethod
    static boolean eventGetCtrlKey(Object domImpl, NativeEvent evt) {
        return JavaScriptObjects.getBoolean(evt, JsoProperties.EVENT_KEY_CTRL);
    }

    @PatchMethod
    static int eventGetKeyCode(Object domImpl, NativeEvent evt) {
        return JavaScriptObjects.getInteger(evt, JsoProperties.EVENT_KEYCODE);
    }

    @PatchMethod
    static boolean eventGetMetaKey(Object domImpl, NativeEvent evt) {
        return JavaScriptObjects.getBoolean(evt, JsoProperties.EVENT_KEY_META);
    }

    @PatchMethod
    static EventTarget eventGetRelatedTarget(Object domImpl, NativeEvent nativeEvent) {

        JavaScriptObject relatedTargetJSO = JavaScriptObjects.getObject(nativeEvent,
                JsoProperties.EVENT_RELATEDTARGET);

        if (relatedTargetJSO == null) {
            return null;
        }

        return relatedTargetJSO.cast();
    }

    @PatchMethod
    static int eventGetScreenX(Object domImpl, NativeEvent evt) {
        return JavaScriptObjects.getInteger(evt, JsoProperties.EVENT_MOUSE_SCREENX);
    }

    @PatchMethod
    static int eventGetScreenY(Object domImpl, NativeEvent evt) {
        return JavaScriptObjects.getInteger(evt, JsoProperties.EVENT_MOUSE_SCREENY);
    }

    @PatchMethod
    static boolean eventGetShiftKey(Object domImpl, NativeEvent evt) {
        return JavaScriptObjects.getBoolean(evt, JsoProperties.EVENT_KEY_SHIFT);
    }

    @PatchMethod
    static EventTarget eventGetTarget(Object domImpl, NativeEvent nativeEvent) {
        Element target = JavaScriptObjects.getObject(nativeEvent, JsoProperties.EVENT_TARGET);
        return target.cast();
    }

    @PatchMethod
    static String eventGetType(Object domImpl, NativeEvent nativeEvent) {
        int eventType = JavaScriptObjects.getInteger(nativeEvent, JsoProperties.EVENT_TYPE);
        return EventUtils.getEventTypeString(eventType);
    }

    @PatchMethod
    static void eventPreventDefault(Object domImpl, NativeEvent evt) {
        JavaScriptObjects.setProperty(evt, JsoProperties.EVENT_PREVENTDEFAULT, true);
    }

    @PatchMethod
    static void eventStopPropagation(Object domImpl, NativeEvent evt) {
        JavaScriptObjects.setProperty(evt, JsoProperties.EVENT_IS_STOPPED, true);
    }

    @PatchMethod
    static int getAbsoluteLeft(Object domImpl, Element elem) {
        return 0;
    }

    @PatchMethod
    static int getAbsoluteTop(Object domImpl, Element elem) {
        return 0;
    }

    @PatchMethod
    static String getAttribute(Object domImpl, Element elem, String name) {
        if ("style".equals(name)) {
            return elem.getStyle().toString();
        }

        PropertyContainer properties = JsoUtils.getDomProperties(elem);

        String propertyName = getDOMPropertyName(name);

        return properties.getString(propertyName);

    }

    @PatchMethod
    static int getBodyOffsetLeft(Object domImpl, Document doc) {
        return 0;
    }

    @PatchMethod
    static int getBodyOffsetTop(Object domImpl, Document doc) {
        return 0;
    }

    static String getDOMPropertyName(String propertyNameCaseInsensitive) {
        propertyNameCaseInsensitive = propertyNameCaseInsensitive.toLowerCase();
        if ("class".equals(propertyNameCaseInsensitive)) {
            return "className";
        }

        return propertyNameCaseInsensitive;

    }

    @PatchMethod
    static Element getFirstChildElement(Object domImpl, Element elem) {
        NodeList<Node> nodeList = elem.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.getItem(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                return node.cast();
            }
        }

        return null;
    }

    @PatchMethod
    static String getInnerHTML(Object domImpl, Element elem) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elem.getChildNodes().getLength(); i++) {
            Node current = elem.getChildNodes().getItem(i);
            if (current.getNodeType() == Node.TEXT_NODE) {
                Text text = current.cast();
                sb.append(text.getData());
            } else {
                sb.append(current.toString());
            }
        }

        return sb.toString();
    }

    @PatchMethod
    static String getInnerText(Object domImpl, Element elem) {

        StringBuilder sb = new StringBuilder("");

        appendInnerTextRecursive(elem, sb);

        return sb.toString();
    }

    @PatchMethod
    static Element getNextSiblingElement(Object domImpl, Element elem) {
        Node parent = elem.getParentNode();
        if (parent == null) {
            return null;
        }

        NodeList<Node> list = parent.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            Node current = list.getItem(i);
            if (current.equals(elem) && i < list.getLength() - 1) {
                while (i < list.getLength() - 1) {
                    i++;
                    if (list.getItem(i).getNodeType() == Node.ELEMENT_NODE) {
                        return list.getItem(i).cast();
                    }
                }
            }
        }

        return null;
    }

    @PatchMethod
    static Element getParentElement(Object domImpl, Node elem) {
        Node parent = elem.getParentNode();

        if (parent == null || !(parent.getNodeType() == Node.ELEMENT_NODE)) {
            return null;
        }

        return parent.cast();
    }

    @PatchMethod
    static int getScrollLeft(Object domImpl, Element elem) {
        return JavaScriptObjects.getInteger(elem, SCROLL_LEFT);
    }

    @PatchMethod
    static String getStyleProperty(Object domImpl, Style style, String propertyName) {
        return GwtStyleUtils.getProperty(style, propertyName);
    }

    @PatchMethod
    static int getTabIndex(Object domImpl, Element elem) {
        return JavaScriptObjects.getInteger(elem, TAB_INDEX);
    }

    @PatchMethod
    static String getTagName(Object domImpl, Element elem) {
        return JsoUtils.getTagName(elem);
    }

    @PatchMethod
    static boolean hasAttribute(Object domImpl, Element elem, String name) {
        PropertyContainer properties = JsoUtils.getDomProperties(elem);

        String propertyName = getDOMPropertyName(name);

        return properties.contains(propertyName);
    }

    @PatchMethod
    static String imgGetSrc(Object domImpl, Element img) {
        return img.getAttribute("src");
    }

    @PatchMethod
    static void imgSetSrc(Object domImpl, Element img, String src) {
        img.setAttribute("src", src);
    }

    @PatchMethod
    static boolean isOrHasChild(final Object domImpl, final Node parent, Node child) {
        while (child != null) {
            if (parent.equals(child)) {
                return true;
            }
            child = child.getParentNode();
        }
        return false;
    }

    @PatchMethod
    static void scrollIntoView(Object domImpl, Element elem) {

    }

    @PatchMethod
    static void selectAdd(Object domImpl, SelectElement select, OptionElement option,
                          OptionElement before) {
        if (before == null) {
            select.appendChild(option);
        } else {
            select.insertBefore(option, before);
        }

        refreshSelect(select);
    }

    @PatchMethod
    static void selectClear(Object domImpl, SelectElement select) {
        clearChildNodes(select);
        select.setSelectedIndex(-1);
    }

    @PatchMethod
    static int selectGetLength(Object domImpl, SelectElement select) {
        return selectGetOptions(domImpl, select).getLength();
    }

    @PatchMethod
    static NodeList<OptionElement> selectGetOptions(Object domImpl, SelectElement select) {
        List<OptionElement> innerList = new ArrayList<>();
        for (int i = 0; i < select.getChildNodes().getLength(); i++) {
            Element e = select.getChildNodes().getItem(i).cast();
            if ("option".equals(e.getTagName())) {
                OptionElement option = e.cast();
                innerList.add(option);
            }
        }

        return JsoUtils.newNodeList(innerList);
    }

    @PatchMethod
    static void selectRemoveOption(Object domImpl, SelectElement select, int index) {
        List<Node> list = JsoUtils.getChildNodeInnerList(select);
        list.remove(index);
        refreshSelect(select);
    }

    @PatchMethod
    static void setInnerText(Object domImpl, Element elem, String text) {
        clearChildNodes(elem);
        elem.appendChild(JsoUtils.newText(text, elem.getOwnerDocument()));
    }

    @PatchMethod
    static void setScrollLeft(Object domImpl, Element elem, int left) {
        JavaScriptObjects.setProperty(elem, SCROLL_LEFT, left);
    }

    @PatchMethod
    static String toString(Object domImpl, Element elem) {
        return elem.toString();
    }

    private static void appendInnerTextRecursive(Element elem, StringBuilder sb) {
        NodeList<Node> list = elem.getChildNodes();

        for (int i = 0; i < elem.getChildNodes().getLength(); i++) {
            Node node = list.getItem(i);
            switch (node.getNodeType()) {
                case Node.TEXT_NODE:
                    Text text = node.cast();
                    String data = text.getData();
                    data = "&nbsp;".equals(data) ? " " : data;
                    sb.append(data);
                    break;
                case Node.ELEMENT_NODE:
                    Element childNode = node.cast();
                    appendInnerTextRecursive(childNode, sb);
                    break;
            }
        }
    }

    private static void clearChildNodes(Element elem) {
        List<Node> innerList = JsoUtils.getChildNodeInnerList(elem);
        innerList.clear();
    }

    private static InputElement createInputElement(Document doc, String type, String name) {
        InputElement e = doc.createElement("input").cast();

        e.setAttribute("type", type);

        if (name != null) {
            e.setAttribute("name", name);
        }

        return e;
    }

    private static void refreshSelect(SelectElement select) {
        SelectElementPatcher.refreshSelect(select);
    }

}
