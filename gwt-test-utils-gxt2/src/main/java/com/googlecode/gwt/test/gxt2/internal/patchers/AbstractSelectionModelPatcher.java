package com.googlecode.gwt.test.gxt2.internal.patchers;

import java.lang.reflect.Method;

import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.selection.AbstractSelectionModel;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(AbstractSelectionModel.class)
class AbstractSelectionModelPatcher {

   @SuppressWarnings("unchecked")
   @PatchMethod
   static ContainerEvent<Container<Component>, Component> createContainerEvent(
            AbstractSelectionModel<Container<Component>, Component> absm,
            Container<Component> container) {

      for (Method m : Container.class.getDeclaredMethods()) {
         if ("createContainerEvent".equals(m.getName())) {
            return (ContainerEvent<Container<Component>, Component>) GwtReflectionUtils.callPrivateMethod(
                     container, m, (Component) null);
         }
      }
      return null;
   }

}
