package com.googlecode.gwt.test.internal.patchers;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;

import com.google.gwt.user.client.ui.PopupPanel;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.utils.JavassistUtils;

@PatchClass(PopupPanel.class)
class PopupPanelPatcher {

   @InitMethod
   static void initClass(CtClass c) throws CannotCompileException {
      CtConstructor cons = JavassistUtils.findConstructor(c);

      cons.insertAfter("getElement().getStyle().setProperty(\"visibility\", \"hidden\");");
   }

}
