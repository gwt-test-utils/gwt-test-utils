package com.googlecode.gwt.test.utils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class which provides utilities on {@link Widget} classes.
 *
 * @author Gael Lazzari
 */
public class WidgetUtils {

    public static boolean assertListBoxDataMatch(ListBox listBox, String[] content) {
        int contentSize = content.length;
        if (contentSize != listBox.getItemCount()) {
            return false;
        }
        for (int i = 0; i < contentSize; i++) {
            if (!content[i].equals(listBox.getItemText(i))) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public static <T extends EventHandler> List<T> getHandlers(Widget widget, Type<T> eventType) {
        HandlerManager handlerManager = GwtReflectionUtils.getPrivateFieldValue(widget,
                "handlerManager");
        Object handlerRegistry = GwtReflectionUtils.getPrivateFieldValue(handlerManager, "eventBus");
        Map<GwtEvent.Type<?>, Map<Object, List<?>>> map = GwtReflectionUtils.getPrivateFieldValue(
                handlerRegistry, "map");

        Map<Object, List<?>> eventHandlerMap = map.get(eventType);

        List<T> result = new ArrayList<>();

        if (eventHandlerMap != null) {
            for (List<?> eventHandlerList : eventHandlerMap.values()) {
                result.addAll((List<T>) eventHandlerList);
            }
        }

        return result;
    }

    public static int getIndexInListBox(ListBox listBox, String regex) {
        int selectedIndex = -1;

        Pattern p = Pattern.compile(regex);

        int i = 0;
        String itemText;
        Matcher m;
        while (i < listBox.getItemCount() && selectedIndex == -1) {
            itemText = listBox.getItemText(i);
            m = p.matcher(itemText);
            if (m.matches() || regex.equals(itemText)) {
                selectedIndex = i;
            } else {
                i++;
            }
        }

        return selectedIndex;
    }

    public static String getListBoxContentToString(ListBox listBox) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listBox.getItemCount(); i++) {
            sb.append(listBox.getItemText(i)).append(" | ");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static List<MenuItem> getMenuItems(MenuBar menuBar) {
        return GwtReflectionUtils.getPrivateFieldValue(menuBar, "items");
    }

    public static List<MenuItem> getMenuItems(SuggestBox suggestBox) {
        SuggestionDisplay display = GwtReflectionUtils.getPrivateFieldValue(suggestBox, "display");
        MenuBar suggestionMenu = GwtReflectionUtils.getPrivateFieldValue(display, "suggestionMenu");
        return getMenuItems(suggestionMenu);
    }

    public static Widget getWidget(Element element) {
        while (element != null) {
            EventListener eventListener = DOM.getEventListener(element);
            if (eventListener != null && eventListener instanceof Widget) {
                return (Widget) eventListener;
            }
            element = element.getParentElement();

        }
        return null;
    }

    public static boolean hasStyle(UIObject object, String styleName) {
        return object.getStyleName().contains(styleName);
    }

    /**
     * Check if the current widget and its possible parents are visible. <strong>NOTE</strong> : if
     * the current widget is a Popup, it is the isShowing() flag which would be evaluate.
     *
     * @param object The widget to check.
     * @return True if the widget and its possible parents are visible, false otherwise.
     */
    public static boolean isWidgetVisible(UIObject object) {
        // FIXME : remove this hack which is required for googlecode main GWT project...
        if (object == null) {
            return false;
        } else if (object instanceof RootPanel) {
            return true;
        } else if (object instanceof PopupPanel) {
            PopupPanel popup = (PopupPanel) object;
            return popup.isShowing();
        }

        return GwtDomUtils.isVisible(object.getElement());
    }

    public static Boolean isShowingItems(SuggestBox suggestBox) {
        SuggestBox.DefaultSuggestionDisplay display = GwtReflectionUtils.getPrivateFieldValue(suggestBox, "display");
        PopupPanel suggestionPopup = GwtReflectionUtils.getPrivateFieldValue(display, "suggestionPopup");
        return suggestionPopup.isShowing();
    }

    /**
     * set a CheckBox value without firing any {@link ValueChangeEvent}.
     *
     * @param checkBox the targeted checkBox
     * @param newValue the new value, which could be retrieve through {@link CheckBox#getValue()}
     */
    public static void setCheckBoxValueSilent(CheckBox checkBox, boolean newValue) {
        InputElement inputElem = GwtReflectionUtils.getPrivateFieldValue(checkBox, "inputElem");
        inputElem.setChecked(newValue);
        inputElem.setDefaultChecked(newValue);
    }

    private WidgetUtils() {

    }

}
