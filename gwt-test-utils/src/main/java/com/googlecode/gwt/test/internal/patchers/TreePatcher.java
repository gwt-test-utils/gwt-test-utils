package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

import java.util.ArrayList;
import java.util.List;

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
        List<Class<?>> list = new ArrayList<>();
        list.add(SelectElement.class);
        list.add(InputElement.class);
        list.add(TextAreaElement.class);
        list.add(OptionElement.class);
        list.add(ButtonElement.class);
        list.add(LabelElement.class);

        return list;
    }

}
