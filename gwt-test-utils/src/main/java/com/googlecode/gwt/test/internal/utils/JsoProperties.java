package com.googlecode.gwt.test.internal.utils;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * List of {@link JavaScriptObject} properties. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public abstract class JsoProperties {

    public static final String ELEMENT_CLIENT_HEIGHT = "ELEMENT_CLIENT_HEIGHT";
    public static final String ELEMENT_CLIENT_WIDTH = "ELEMENT_CLIENT_WIDTH";

    // EVENT PROPERTIES
    public static final String EVENT_BUTTON = "EVENT_button";
    public static final String EVENT_IS_STOPPED = "EVENT_isStopped";
    public static final String EVENT_KEY_ALT = "EVENT_altKey";
    public static final String EVENT_KEY_CTRL = "EVENT_ctrlKey";
    public static final String EVENT_KEY_META = "EVENT_metaKey";
    public static final String EVENT_KEY_SHIFT = "EVENT_shiftKey";
    public static final String EVENT_KEYCODE = "EVENT_keyCode";
    public static final String EVENT_MOUSE_CLIENTX = "EVENT_clientX";
    public static final String EVENT_MOUSE_CLIENTY = "EVENT_clientY";
    public static final String EVENT_MOUSE_SCREENX = "EVENT_screenX";
    public static final String EVENT_MOUSE_SCREENY = "EVENT_screenY";
    public static final String EVENT_PREVENTDEFAULT = "EVENT_preventDefault";
    public static final String EVENT_RELATEDTARGET = "EVENT_relatedTarget";
    public static final String EVENT_TARGET = "EVENT_target";
    public static final String EVENT_TYPE = "EVENT_type";

    public static final String ID = "id";

    /**
     * The name of the internal {@link PropertyContainer} which is add in {@link JavaScriptObject}
     * class during class rewrite process
     */
    public static final String JSO_PROPERTIES = "properties";

    public static final String NODE_NAMESPACE_URI = "namespaceURI";

    public static final String NODE_OWNER_DOCUMENT = "NODE_OWNER_DOCUMENT";

    public static final String SELECTION_END = "SELECTION_END";
    public static final String SELECTION_START = "SELECTION_START";

    public static final String XML_ATTR_NAME = "XML_ATTR_NAME";
    public static final String XML_ATTR_SET = "XML_ATTR_SET";

    private JsoProperties() {

    }
}
