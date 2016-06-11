package com.googlecode.gwt.test.utils.events;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.gwt.test.internal.utils.JsoProperties;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

/**
 * Builder for complex {@link Event}.
 *
 * @author Gael Lazzari
 */
public class EventBuilder {

    /**
     * Create a builder to configure an {@link Event} of the specified type.
     *
     * @param eventType The type of the browser event to build.
     * @return The builder which can be used to configure the event to build.
     */
    public static EventBuilder create(int eventType) {
        EventBuilder builder = new EventBuilder();

        JavaScriptObjects.setProperty(builder.event, JsoProperties.EVENT_TYPE, eventType);

        return builder.setCanBubble(true);
    }

    private final Event event;

    private EventBuilder() {
        event = JavaScriptObject.createObject().cast();
    }

    /**
     * Return the configured {@link Event}.
     *
     * @return The event which has been configured by the current builder.
     */
    public Event build() {
        return event;
    }

    public EventBuilder setAltKey(boolean altKey) {
        JavaScriptObjects.setProperty(event, JsoProperties.EVENT_KEY_ALT, altKey);
        return this;
    }

    public EventBuilder setButton(int button) {
        JavaScriptObjects.setProperty(event, JsoProperties.EVENT_BUTTON, button);
        return this;
    }

    public EventBuilder setCanBubble(boolean canBubble) {
        JavaScriptObjects.setProperty(event, JsoProperties.EVENT_IS_STOPPED, !canBubble);
        return this;
    }

    public EventBuilder setCtrlKey(boolean ctrlKey) {
        JavaScriptObjects.setProperty(event, JsoProperties.EVENT_KEY_CTRL, ctrlKey);
        return this;
    }

    public EventBuilder setKeyCode(int keyCode) {
        JavaScriptObjects.setProperty(event, JsoProperties.EVENT_KEYCODE, keyCode);
        return this;
    }

    public EventBuilder setMetaKey(boolean metaKey) {
        JavaScriptObjects.setProperty(event, JsoProperties.EVENT_KEY_META, metaKey);
        return this;
    }

    public EventBuilder setMouseScreenX(int screenX) {
        JavaScriptObjects.setProperty(event, JsoProperties.EVENT_MOUSE_SCREENX, screenX);
        return this;
    }

    public EventBuilder setMouseScreenY(int screenY) {
        JavaScriptObjects.setProperty(event, JsoProperties.EVENT_MOUSE_SCREENY, screenY);
        return this;
    }

    public EventBuilder setMouseX(int x) {
        JavaScriptObjects.setProperty(event, JsoProperties.EVENT_MOUSE_CLIENTX, x);
        return this;
    }

    public EventBuilder setMouseY(int y) {
        JavaScriptObjects.setProperty(event, JsoProperties.EVENT_MOUSE_CLIENTY, y);
        return this;
    }

    public EventBuilder setRelatedTarget(EventTarget target) {
        JavaScriptObjects.setProperty(event, JsoProperties.EVENT_RELATEDTARGET, target);
        return this;
    }

    public EventBuilder setShiftKey(boolean shiftKey) {
        JavaScriptObjects.setProperty(event, JsoProperties.EVENT_KEY_SHIFT, shiftKey);
        return this;
    }

    public EventBuilder setTarget(Element target) {
        JavaScriptObjects.setProperty(event, JsoProperties.EVENT_TARGET, target);
        return this;
    }

    public EventBuilder setTarget(UIObject target) {
        JavaScriptObjects.setProperty(event, JsoProperties.EVENT_TARGET, target.getElement());
        return this;
    }

}
