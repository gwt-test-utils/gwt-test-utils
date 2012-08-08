package com.googlecode.gwt.test.internal.patchers;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.internal.handlers.GwtTestGWTBridge;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;

@PatchClass(GWT.class)
class GwtPatcher {

   @InitMethod
   static void init(CtClass ctClass) throws CannotCompileException {

      CtConstructor staticInitializer = ctClass.makeClassInitializer();

      staticInitializer.insertAfter("setBridge(" + GwtTestGWTBridge.class.getName() + ".get());");
   }

}
