package com.googlecode.gwt.test.internal.patchers;

import java.util.HashSet;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

import com.googlecode.gwt.test.internal.BrowserSimulatorImpl;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;

@PatchClass(target = "com.google.gwt.user.cellview.client.HasDataPresenter")
class HasDataPresenterPatcher {

   private static final Set<String> methodsToPatch = new HashSet<String>() {

      private static final long serialVersionUID = -7540404830435187143L;

      {
         add("clearKeyboardSelectedRowValue");
         add("redraw");
         add("setKeyboardSelectedRow");
         add("setRowCount");
         add("setRowData");
         add("setSelectionModel");
         add("setVisibleRange");
         add("updateCachedData");
      }
   };

   @InitMethod
   static void initClass(CtClass c) throws CannotCompileException {
      // every method which calls HasDataPresenter.ensurePendingState() should call
      // BrowserSimulatorImpl.get().fireLoopEnd() at the end, so finally and deferred
      // commands would be executed just after the method itself
      for (CtMethod m : c.getMethods()) {
         if (methodsToPatch.contains(m.getName())) {
            m.insertAfter(BrowserSimulatorImpl.class.getName() + ".get().fireLoopEnd();");
         }
      }
   }

}
