package com.googlecode.gwt.test.internal.patchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

import com.google.gwt.core.client.JsArrayInteger;
import com.googlecode.gwt.test.internal.BrowserSimulatorImpl;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

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

   @PatchMethod
   static void sortJsArrayInteger(JsArrayInteger array) {
      List<Integer> list = new ArrayList<Integer>(array.length());
      for (int i = 0; i < array.length(); i++) {
         list.add(array.get(i));
      }
      Collections.sort(list);
      for (int i = 0; i < array.length(); i++) {
         array.set(i, list.get(i));
      }
   }

}
