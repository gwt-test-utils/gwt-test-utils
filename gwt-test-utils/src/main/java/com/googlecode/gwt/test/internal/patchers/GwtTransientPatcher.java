package com.googlecode.gwt.test.internal.patchers;

import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;
import javassist.CtClass;

import java.lang.reflect.Modifier;

@PatchClass(target = "com.google.common.collect.GwtTransient")
public class GwtTransientPatcher {

    @InitMethod
    static void init(CtClass ctClass) {
        // set GwtTransient class public
        ctClass.setModifiers(ctClass.getModifiers() + Modifier.PUBLIC);
    }

}