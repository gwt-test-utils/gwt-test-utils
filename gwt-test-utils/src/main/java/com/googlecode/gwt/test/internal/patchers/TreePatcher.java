package com.googlecode.gwt.test.internal.patchers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(Tree.class)
class TreePatcher {

   @PatchMethod
   static boolean shouldTreeDelegateFocusToElement(Element elem) {
      List<Class<?>> focusElementClasses = getFocusElementClasses();
      int i = 0;
      boolean shouldDelegate = false;

      while (i < focusElementClasses.size() && !shouldDelegate) {
         Class<?> focusElementClass = focusElementClasses.get(i++);
         shouldDelegate = focusElementClass.isInstance(elem);
      }

      return shouldDelegate;
   }

   @PatchMethod
   static void showImage(Tree tree, TreeItem treeItem, AbstractImagePrototype proto) {

   }

   private static List<Class<?>> getFocusElementClasses() {
      List<Class<?>> list = new ArrayList<Class<?>>();
      list.add(SelectElement.class);
      list.add(InputElement.class);
      list.add(TextAreaElement.class);
      list.add(OptionElement.class);
      list.add(ButtonElement.class);
      list.add(LabelElement.class);

      return list;
   }

}
