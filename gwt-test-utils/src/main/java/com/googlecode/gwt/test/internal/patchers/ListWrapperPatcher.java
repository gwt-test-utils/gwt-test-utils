package com.googlecode.gwt.test.internal.patchers;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import com.googlecode.gwt.test.internal.BrowserSimulatorImpl;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;

@PatchClass(target = "com.google.gwt.view.client.ListDataProvider$ListWrapper")
class ListWrapperPatcher {

   @InitMethod
   static void initClass(CtClass c) throws CannotCompileException, NotFoundException {
      CtMethod flushMethod = c.getDeclaredMethod("flush");
      flushMethod.insertAfter(BrowserSimulatorImpl.class.getName()
               + ".get().fireLoopEnd();");
   }

}
