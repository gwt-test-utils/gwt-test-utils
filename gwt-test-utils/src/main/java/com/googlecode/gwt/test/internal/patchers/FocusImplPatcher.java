package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(FocusImpl.class)
class FocusImplPatcher {

    @PatchMethod
    static void blur(FocusImpl focusImpl, Element element) {

    }

    @PatchMethod
    static void focus(FocusImpl focusImpl, Element element) {

    }

}
