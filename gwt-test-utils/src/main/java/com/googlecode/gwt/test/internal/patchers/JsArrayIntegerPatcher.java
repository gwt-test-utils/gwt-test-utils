package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.JsArrayInteger;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(JsArrayInteger.class)
class JsArrayIntegerPatcher {

    @PatchMethod
    static int get(JsArrayInteger jsArray, int index) {
        return JsArrayHelper.get(jsArray, index, JsArrayHelper.getIntegerConverter());
    }

    @PatchMethod
    static String join(JsArrayInteger jsArray, String separator) {
        return JsArrayHelper.join(jsArray, separator, JsArrayHelper.getIntegerConverter());
    }

    @PatchMethod
    static int length(JsArrayInteger jsArray) {
        return JsArrayHelper.length(jsArray);
    }

    @PatchMethod
    static void push(JsArrayInteger jsArray, int value) {
        JsArrayHelper.push(jsArray, value);
    }

    @PatchMethod
    static void set(JsArrayInteger jsArray, int index, int value) {
        JsArrayHelper.set(jsArray, index, value);
    }

    @PatchMethod
    static void setLength(JsArrayInteger jsArray, int newLength) {
        JsArrayHelper.setLength(jsArray, newLength);
    }

    @PatchMethod
    static int shift(JsArrayInteger jsArray) {
        return JsArrayHelper.shift(jsArray, JsArrayHelper.getIntegerConverter());
    }

    @PatchMethod
    static void unshift(JsArrayInteger jsArray, int value) {
        JsArrayHelper.unshift(jsArray, value);
    }

}
