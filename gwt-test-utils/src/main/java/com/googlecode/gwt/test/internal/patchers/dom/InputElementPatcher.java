package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.dom.client.InputElement;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(InputElement.class)
class InputElementPatcher {

    @PatchMethod
    static void click(InputElement inputElement) {

    }

    @PatchMethod
    static void select(InputElement inputElement) {

    }

}
