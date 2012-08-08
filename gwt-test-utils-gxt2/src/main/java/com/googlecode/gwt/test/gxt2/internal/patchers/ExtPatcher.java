package com.googlecode.gwt.test.gxt2.internal.patchers;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.impl.ComputedStyleImpl;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.TextMetrics;
import com.extjs.gxt.ui.client.widget.Layer;
import com.extjs.gxt.ui.client.widget.WindowManager;
import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(target = "com.extjs.gxt.ui.client.core.Ext")
class ExtPatcher {

   /**
    * Place where all necessary GXT internal objects are reset after the execution of a unit test
    */
   static {
      AfterTestCallbackManager.get().registerCallback(new AfterTestCallback() {

         public void afterTest() throws Throwable {
            GwtReflectionUtils.setStaticField(IconHelper.class, "el", null);
            GwtReflectionUtils.setStaticField(IconHelper.class, "cacheMap", null);

            GwtReflectionUtils.setStaticField(WindowManager.class, "instance", null);
            GwtReflectionUtils.setStaticField(TextMetrics.class, "instance", null);

            getStaticAndCallClear(Layer.class, "shadows");
            getStaticAndCallClear(Layer.class, "shims");

            ComputedStyleImpl computedStyle = GWT.create(ComputedStyleImpl.class);
            GwtReflectionUtils.setStaticField(El.class, "computedStyle", computedStyle);
         }

         private void getStaticAndCallClear(Class<?> clazz, String fieldName) {
            GwtReflectionUtils.callPrivateMethod(
                     GwtReflectionUtils.getStaticFieldValue(clazz, fieldName), "clear");
         }

      });
   }

   @PatchMethod
   static void loadDomHelper() {

   }

   @PatchMethod
   static void loadDomQuery() {

   }

   @PatchMethod
   static void loadExt() {

   }

   @PatchMethod
   static void loadFormat() {

   }

   @PatchMethod
   static void loadTemplate() {

   }

   @PatchMethod
   static void loadXTemplate() {

   }
}
