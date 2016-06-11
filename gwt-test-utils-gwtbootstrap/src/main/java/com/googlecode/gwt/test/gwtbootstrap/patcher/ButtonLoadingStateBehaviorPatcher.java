package com.googlecode.gwt.test.gwtbootstrap.patcher;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Button.LoadingStateBehavior;
import com.google.gwt.dom.client.Element;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

/**
 * Patcher for {@link LoadingStateBehavior}.
 *
 * @author Kenichiro Tanaka
 */
@PatchClass(Button.LoadingStateBehavior.class)
class ButtonLoadingStateBehaviorPatcher {

    @PatchMethod
    static void setLoadingBehavior(Button.LoadingStateBehavior lsb, Element e, String behavior) {
        // Nothing to do??
    }
}
