package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.dom.client.StyleInjector;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(StyleInjector.class)
class StyleInjectorPatcher {

    @PatchMethod
    static void inject(String css, boolean immediate) {

    }

    @PatchMethod
    static void injectAtEnd(String css, boolean immediate) {

    }

    @PatchMethod
    static void injectAtStart(String css, boolean immediate) {

    }

}
