package com.googlecode.gwt.test.internal.patchers;

import java.lang.reflect.Modifier;

import javassist.CtClass;

import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;

@PatchClass(target = "com.google.common.collect.GwtTransient")
public class GwtTransientPatcher {

   @InitMethod
   static void init(CtClass ctClass) {
      // set GwtTransient class public
      ctClass.setModifiers(ctClass.getModifiers() + Modifier.PUBLIC);
   }

}