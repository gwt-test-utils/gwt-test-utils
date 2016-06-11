package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.lang.reflect.Constructor;

@PatchClass(SafeHtmlUtils.class)
public class SafeHtmlUtilsPatcher {

    @PatchMethod
    public static SafeHtml fromSafeConstant(String s) {
        // PatchMethod to avoid gwt-dev dependency.. See SafeHtmlHostedModeUtils
        Class<?> clazz;
        try {
            clazz = Class.forName("com.google.gwt.safehtml.shared.SafeHtmlString");
            Constructor<?> cons = clazz.getDeclaredConstructor(String.class);
            return (SafeHtml) GwtReflectionUtils.instantiateClass(cons, s);
        } catch (Exception e) {
            throw new GwtTestPatchException("Error while instanciate a SafeHtmlString instance", e);
        }

    }

}
