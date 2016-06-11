package com.googlecode.gwt.test.gwtbootstrap.patcher;

import com.github.gwtbootstrap.client.ui.base.AlertBase;
import com.github.gwtbootstrap.client.ui.event.ClosedEvent;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

/**
 * Patcher for {@link AlertBase}
 *
 * @author Kenichiro Tanaka
 * @author Gael Lazzari
 */
@PatchClass(AlertBase.class)
class AlertBasePatcher {

    @PatchMethod
    static void close(AlertBase alertBase, Element e) {
        CloseEvent.<AlertBase>fire(alertBase, alertBase);
        ClosedEvent.<AlertBase>fire(alertBase, alertBase);
    }

    @PatchMethod
    static void configure(AlertBase alertBase, Element e) {
    }

    @PatchMethod
    static void setHandlerFunctions(AlertBase alertBase, Element e) {
    }
}
