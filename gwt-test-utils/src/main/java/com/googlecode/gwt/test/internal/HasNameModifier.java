package com.googlecode.gwt.test.internal;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

import com.google.gwt.user.client.ui.HasName;
import com.googlecode.gwt.test.finder.GwtFinder;

public class HasNameModifier implements JavaClassModifier {

   private final CtClass hasNameCtClass;
   private final CtClass[] setArgs;

   HasNameModifier() {
      hasNameCtClass = GwtClassPool.getCtClass(HasName.class);
      setArgs = new CtClass[]{GwtClassPool.getCtClass(String.class)};
   }

   public void modify(CtClass classToModify) throws Exception {

      if (classToModify.isInterface()) {
         return;
      }

      if (classToModify.subtypeOf(hasNameCtClass)) {
         instrumentSetName(classToModify);
      }
   }

   private void addTextField(CtClass c) throws CannotCompileException {
      CtField oldTextField = CtField.make("private String instrument_oldName;", c);
      c.addField(oldTextField);
   }

   private void instrumentSetName(CtClass c) throws CannotCompileException {
      // add behavior to HasHTML.setHTML method
      try {
         CtMethod setName = c.getDeclaredMethod("setName", setArgs);
         addTextField(c);
         setName.insertBefore("this.instrument_oldName = this.getName();");
         setName.insertAfter(GwtFinder.class.getName()
                  + ".onSetName(this, $1, this.instrument_oldName);");
      } catch (NotFoundException e) {
         // don't instrument method if not existing
      }
   }
}
