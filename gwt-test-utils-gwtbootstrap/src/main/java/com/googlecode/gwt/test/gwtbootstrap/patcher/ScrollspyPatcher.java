package com.googlecode.gwt.test.gwtbootstrap.patcher;

import com.github.gwtbootstrap.client.ui.Scrollspy;
import com.google.gwt.dom.client.Element;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

/**
 * Patcher for {@link Scrollspy}.
 * <p>
 * This patcher do nothing only for avoiding {@link UnsatisfiedLinkError}.
 *
 * @author Kenichiro Tanaka
 */
@PatchClass(Scrollspy.class)
class ScrollspyPatcher {

    @PatchMethod
    static void jsConfigure(Scrollspy spy, Element e, String target, int offset) {
    }

    @PatchMethod
    static void refresh(Scrollspy spy, Element e) {
    }
}
