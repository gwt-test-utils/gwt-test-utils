package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.JsArrayBoolean;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(JsArrayBoolean.class)
class JsArrayBooleanPatcher {

    @PatchMethod
    static boolean get(JsArrayBoolean jsArray, int index) {
        return JsArrayHelper.get(jsArray, index, JsArrayHelper.getBooleanConverter());
    }

    @PatchMethod
    static String join(JsArrayBoolean jsArray, String separator) {
        return JsArrayHelper.join(jsArray, separator, JsArrayHelper.getBooleanConverter());
    }

    @PatchMethod
    static int length(JsArrayBoolean jsArray) {
        return JsArrayHelper.length(jsArray);
    }

    @PatchMethod
    static void push(JsArrayBoolean jsArray, boolean value) {
        JsArrayHelper.push(jsArray, value);
    }

    @PatchMethod
    static void set(JsArrayBoolean jsArray, int index, boolean value) {
        JsArrayHelper.set(jsArray, index, value);
    }

    @PatchMethod
    static void setLength(JsArrayBoolean jsArray, int newLength) {
        JsArrayHelper.setLength(jsArray, newLength);
    }

    @PatchMethod
    static boolean shift(JsArrayBoolean jsArray) {
        return JsArrayHelper.shift(jsArray, JsArrayHelper.getBooleanConverter());
    }

    @PatchMethod
    static void unshift(JsArrayBoolean jsArray, boolean value) {
        JsArrayHelper.unshift(jsArray, value);
    }

}
