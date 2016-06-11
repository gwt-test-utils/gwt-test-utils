package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.JsArrayNumber;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(JsArrayNumber.class)
class JsArrayNumberPatcher {

    @PatchMethod
    static double get(JsArrayNumber jsArray, int index) {
        return JsArrayHelper.get(jsArray, index, JsArrayHelper.getNumberConverter());
    }

    @PatchMethod
    static String join(JsArrayNumber jsArray, String separator) {
        return JsArrayHelper.join(jsArray, separator, JsArrayHelper.getNumberConverter());
    }

    @PatchMethod
    static int length(JsArrayNumber jsArray) {
        return JsArrayHelper.length(jsArray);
    }

    @PatchMethod
    static void push(JsArrayNumber jsArray, double value) {
        JsArrayHelper.push(jsArray, value);
    }

    @PatchMethod
    static void set(JsArrayNumber jsArray, int index, double value) {
        JsArrayHelper.set(jsArray, index, value);
    }

    @PatchMethod
    static void setLength(JsArrayNumber jsArray, int newLength) {
        JsArrayHelper.setLength(jsArray, newLength);
    }

    @PatchMethod
    static double shift(JsArrayNumber jsArray) {
        return JsArrayHelper.shift(jsArray, JsArrayHelper.getNumberConverter());
    }

    @PatchMethod
    static void unshift(JsArrayNumber jsArray, double value) {
        JsArrayHelper.unshift(jsArray, value);
    }

}
