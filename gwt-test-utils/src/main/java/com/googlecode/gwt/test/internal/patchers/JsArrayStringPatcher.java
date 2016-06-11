package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.JsArrayString;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(JsArrayString.class)
class JsArrayStringPatcher {

    @PatchMethod
    static String get(JsArrayString jsArray, int index) {
        return JsArrayHelper.get(jsArray, index, JsArrayHelper.getStringConverter());
    }

    @PatchMethod
    static String join(JsArrayString jsArray, String separator) {
        return JsArrayHelper.join(jsArray, separator, JsArrayHelper.getStringConverter());
    }

    @PatchMethod
    static int length(JsArrayString jsArray) {
        return JsArrayHelper.length(jsArray);
    }

    @PatchMethod
    static void push(JsArrayString jsArray, String value) {
        JsArrayHelper.push(jsArray, value);
    }

    @PatchMethod
    static void set(JsArrayString jsArray, int index, String value) {
        JsArrayHelper.set(jsArray, index, value);
    }

    @PatchMethod
    static void setLength(JsArrayString jsArray, int newLength) {
        JsArrayHelper.setLength(jsArray, newLength);
    }

    @PatchMethod
    static String shift(JsArrayString jsArray) {
        return JsArrayHelper.shift(jsArray, JsArrayHelper.getStringConverter());
    }

    @PatchMethod
    static void unshift(JsArrayString jsArray, String value) {
        JsArrayHelper.unshift(jsArray, value);
    }

}
