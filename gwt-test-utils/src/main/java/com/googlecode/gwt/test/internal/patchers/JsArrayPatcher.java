package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(JsArray.class)
class JsArrayPatcher {

    @PatchMethod
    static JavaScriptObject get(JsArray<JavaScriptObject> jsArray, int index) {
        return JsArrayHelper.get(jsArray, index, JsArrayHelper.getObjectConverter());
    }

    @PatchMethod
    static String join(JsArray<JavaScriptObject> jsArray, String separator) {
        return JsArrayHelper.join(jsArray, separator, JsArrayHelper.getObjectConverter());
    }

    @PatchMethod
    static int length(JsArray<JavaScriptObject> jsArray) {
        return JsArrayHelper.length(jsArray);
    }

    @PatchMethod
    static void push(JsArray<JavaScriptObject> jsArray, JavaScriptObject value) {
        JsArrayHelper.push(jsArray, value);
    }

    @PatchMethod
    static void set(JsArray<JavaScriptObject> jsArray, int index, JavaScriptObject value) {
        JsArrayHelper.set(jsArray, index, value);
    }

    @PatchMethod
    static void setLength(JsArray<JavaScriptObject> jsArray, int newLength) {
        JsArrayHelper.setLength(jsArray, newLength);
    }

    @PatchMethod
    static JavaScriptObject shift(JsArray<JavaScriptObject> jsArray) {
        return JsArrayHelper.shift(jsArray, JsArrayHelper.getObjectConverter());
    }

    @PatchMethod
    static void unshift(JsArray<JavaScriptObject> jsArray, JavaScriptObject value) {
        JsArrayHelper.unshift(jsArray, value);
    }

}
