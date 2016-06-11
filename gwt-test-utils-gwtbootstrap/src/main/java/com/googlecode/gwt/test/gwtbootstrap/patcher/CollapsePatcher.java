package com.googlecode.gwt.test.gwtbootstrap.patcher;

import com.github.gwtbootstrap.client.ui.constants.VisibilityChange;
import com.github.gwtbootstrap.client.ui.event.HiddenEvent;
import com.github.gwtbootstrap.client.ui.event.HideEvent;
import com.github.gwtbootstrap.client.ui.event.ShowEvent;
import com.github.gwtbootstrap.client.ui.event.ShownEvent;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Patcher for Collapse (bootstrap 2.1.1).
 *
 * @author Kenichiro Tanaka
 */
@PatchClass(target = "com.github.gwtbootstrap.client.ui.Collapse")
class CollapsePatcher {
    private static Map<Object, Boolean> shownObject = new HashMap<Object, Boolean>();

    @PatchMethod
    static void changeVisibility(Object collapse, Element e, String c) {
        if (!shownObject.containsKey(collapse)) {
            shownObject.put(collapse, isToggle(collapse));
        }

        if (VisibilityChange.SHOW.get().equals(c) && !shownObject.get(collapse)) {
            show(collapse);
        } else if (VisibilityChange.HIDE.get().equals(c) && shownObject.get(collapse)) {
            hide(collapse);
        } else if (VisibilityChange.TOGGLE.get().equals(c)) {
            if (shownObject.get(collapse)) {
                hide(collapse);
            } else {
                show(collapse);
            }
        }
    }

    @PatchMethod
    static void changeVisibility(String target, String c) {
    }

    @PatchMethod
    static void configure(Object collapse, Element e, String parent, boolean toggle) {
        shownObject.put(collapse, toggle);
    }

    @PatchMethod
    static void configure(String selector, String parent, boolean toggle) {
        // TODO:
    }

    @PatchMethod
    static void removeDataIfExists(Object collapse, Element e) {
    }

    @PatchMethod
    static void setHandlerFunctions(Object collapse, Element e) {
    }

    private static Widget getWidget(Object collapse) {
        return GwtReflectionUtils.<Widget>callPrivateMethod(collapse, "getWidget");
    }

    private static void hide(Object collapse) {
        getWidget(collapse).fireEvent(new HideEvent());
        shownObject.put(collapse, false);
        getWidget(collapse).fireEvent(new HiddenEvent());
    }

    private static boolean isToggle(Object collapse) {
        return GwtReflectionUtils.<Boolean>callPrivateMethod(collapse, "isToggle");
    }

    private static void show(Object collapse) {
        getWidget(collapse).fireEvent(new ShowEvent());
        shownObject.put(collapse, true);
        getWidget(collapse).fireEvent(new ShownEvent());
    }
}
