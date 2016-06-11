package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayMixed;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(JsArrayMixed.class)
class JsArrayMixedPatcher {

    @PatchMethod
    static boolean getBoolean(JsArrayMixed jsArray, int index) {
        return JsArrayHelper.get(jsArray, index, JsArrayHelper.getBooleanConverter());
    }

    @PatchMethod
    static double getNumber(JsArrayMixed jsArray, int index) {
        return JsArrayHelper.get(jsArray, index, JsArrayHelper.getNumberConverter());
    }

    @PatchMethod
    static JavaScriptObject getObject(JsArrayMixed jsArray, int index) {
        return JsArrayHelper.get(jsArray, index, JsArrayHelper.getObjectConverter());
    }

    @PatchMethod
    static String getString(JsArrayMixed jsArray, int index) {
        return JsArrayHelper.get(jsArray, index, JsArrayHelper.getStringConverter());
    }

    @PatchMethod
    static String join(JsArrayMixed jsArray, String separator) {
        return JsArrayHelper.join(jsArray, separator, JsArrayHelper.getMixedConverter());
    }

    @PatchMethod
    static int length(JsArrayMixed jsArray) {
        return JsArrayHelper.length(jsArray);
    }

    @PatchMethod
    static void push(JsArrayMixed jsArray, boolean value) {
        JsArrayHelper.push(jsArray, value);
    }

    @PatchMethod
    static void push(JsArrayMixed jsArray, double value) {
        JsArrayHelper.push(jsArray, value);
    }

    @PatchMethod
    static void push(JsArrayMixed jsArray, JavaScriptObject value) {
        JsArrayHelper.push(jsArray, value);
    }

    @PatchMethod
    static void push(JsArrayMixed jsArray, String value) {
        JsArrayHelper.push(jsArray, value);
    }

    @PatchMethod
    static void set(JsArrayMixed jsArray, int index, boolean value) {
        JsArrayHelper.set(jsArray, index, value);
    }

    @PatchMethod
    static void set(JsArrayMixed jsArray, int index, double value) {
        JsArrayHelper.set(jsArray, index, value);
    }

    @PatchMethod
    static void set(JsArrayMixed jsArray, int index, JavaScriptObject value) {
        JsArrayHelper.set(jsArray, index, value);
    }

    @PatchMethod
    static void set(JsArrayMixed jsArray, int index, String value) {
        JsArrayHelper.set(jsArray, index, value);
    }

    @PatchMethod
    static void setLength(JsArrayMixed jsArray, int newLength) {
        JsArrayHelper.setLength(jsArray, newLength);
    }

    @PatchMethod
    static boolean shiftBoolean(JsArrayMixed jsArray) {
        return JsArrayHelper.shift(jsArray, JsArrayHelper.getBooleanConverter());
    }

    @PatchMethod
    static double shiftNumber(JsArrayMixed jsArray) {
        return JsArrayHelper.shift(jsArray, JsArrayHelper.getNumberConverter());
    }

    @PatchMethod
    static JavaScriptObject shiftObject(JsArrayMixed jsArray) {
        return JsArrayHelper.shift(jsArray, JsArrayHelper.getObjectConverter());
    }

    @PatchMethod
    static String shiftString(JsArrayMixed jsArray) {
        return JsArrayHelper.shift(jsArray, JsArrayHelper.getStringConverter());
    }

    @PatchMethod
    static void unshift(JsArrayMixed jsArray, boolean value) {
        JsArrayHelper.unshift(jsArray, value);
    }

    @PatchMethod
    static void unshift(JsArrayMixed jsArray, double value) {
        JsArrayHelper.unshift(jsArray, value);
    }

    @PatchMethod
    static void unshift(JsArrayMixed jsArray, JavaScriptObject value) {
        JsArrayHelper.unshift(jsArray, value);
    }

    @PatchMethod
    static void unshift(JsArrayMixed jsArray, String value) {
        JsArrayHelper.unshift(jsArray, value);
    }

}
