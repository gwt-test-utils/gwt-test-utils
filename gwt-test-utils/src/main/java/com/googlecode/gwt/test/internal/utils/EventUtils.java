package com.googlecode.gwt.test.internal.utils;

import com.google.gwt.user.client.Event;
import com.googlecode.gwt.test.exceptions.GwtTestDomException;

/**
 * Some {@link Event} utility methods. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public class EventUtils {

    public static int getEventTypeInt(String type) {
        if (type.equals("blur")) {
            return Event.ONBLUR;
        } else if (type.equals("change")) {
            return Event.ONCHANGE;
        } else if (type.equals("click")) {
            return Event.ONCLICK;
        } else if (type.equals("dblclick")) {
            return Event.ONDBLCLICK;
        } else if (type.equals("focus")) {
            return Event.ONFOCUS;
        } else if (type.equals("keydown")) {
            return Event.ONKEYDOWN;
        } else if (type.equals("keypress")) {
            return Event.ONKEYPRESS;
        } else if (type.equals("keyup")) {
            return Event.ONKEYUP;
        } else if (type.equals("load")) {
            return Event.ONLOAD;
        } else if (type.equals("losecapture")) {
            return Event.ONLOSECAPTURE;
        } else if (type.equals("mousedown")) {
            return Event.ONMOUSEDOWN;
        } else if (type.equals("mousemove")) {
            return Event.ONMOUSEMOVE;
        } else if (type.equals("mouseout")) {
            return Event.ONMOUSEOUT;
        } else if (type.equals("mouseover")) {
            return Event.ONMOUSEOVER;
        } else if (type.equals("mouseup")) {
            return Event.ONMOUSEUP;
        } else if (type.equals("scroll")) {
            return Event.ONSCROLL;
        } else if (type.equals("error")) {
            return Event.ONERROR;
        } else if (type.equals("mousewheel")) {
            return Event.ONMOUSEWHEEL;
        } else if (type.equals("contextmenu")) {
            return Event.ONCONTEXTMENU;
        } else if (type.equals("paste")) {
            return Event.ONPASTE;
        } else if (type.equals("touchstart")) {
            return Event.ONTOUCHSTART;
        } else if (type.equals("touchmove")) {
            return Event.ONTOUCHMOVE;
        } else if (type.equals("touchend")) {
            return Event.ONTOUCHEND;
        } else if (type.equals("touchcancel")) {
            return Event.ONTOUCHCANCEL;
        } else if (type.equals("gesturestart")) {
            return Event.ONGESTURESTART;
        } else if (type.equals("gesturechange")) {
            return Event.ONGESTURECHANGE;
        } else if (type.equals("gestureend")) {
            return Event.ONGESTUREEND;
        }

        throw new GwtTestDomException("Unable to convert DOM Event \"" + type + "\" to an integer");
    }

    public static String getEventTypeString(int eventTypeInt) {
        switch (eventTypeInt) {
            case Event.ONBLUR:
                return "blur";
            case Event.ONCHANGE:
                return "change";
            case Event.ONCLICK:
                return "click";
            case Event.ONDBLCLICK:
                return "dblclick";
            case Event.ONFOCUS:
                return "focus";
            case Event.ONKEYDOWN:
                return "keydown";
            case Event.ONKEYPRESS:
                return "keypress";
            case Event.ONKEYUP:
                return "keyup";
            case Event.ONLOAD:
                return "load";
            case Event.ONLOSECAPTURE:
                return "losecapture";
            case Event.ONMOUSEDOWN:
                return "mousedown";
            case Event.ONMOUSEMOVE:
                return "mousemove";
            case Event.ONMOUSEOUT:
                return "mouseout";
            case Event.ONMOUSEOVER:
                return "mouseover";
            case Event.ONMOUSEUP:
                return "mouseup";
            case Event.ONSCROLL:
                return "scroll";
            case Event.ONERROR:
                return "error";
            case Event.ONMOUSEWHEEL:
                return "mousewheel";
            case Event.ONCONTEXTMENU:
                return "contextmenu";
            case Event.ONPASTE:
                return "paste";
            case Event.ONTOUCHSTART:
                return "touchstart";
            case Event.ONTOUCHMOVE:
                return "touchmove";
            case Event.ONTOUCHEND:
                return "touchend";
            case Event.ONTOUCHCANCEL:
                return "touchcancel";
            case Event.ONGESTURESTART:
                return "gesturestart";
            case Event.ONGESTURECHANGE:
                return "gesturechange";
            case Event.ONGESTUREEND:
                return "gestureend";
            default:
                throw new GwtTestDomException("Cannot get the String type of event with code ["
                        + eventTypeInt + "]");
        }
    }

    private EventUtils() {

    }

}
