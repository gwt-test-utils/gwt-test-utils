package com.googlecode.gwt.test.gxt2.internal.patchers;

import com.extjs.gxt.ui.client.js.JsObject;
import com.google.gwt.core.client.JavaScriptObject;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

@PatchClass(JsObject.class)
class JsObjectPatcher {

   @PatchMethod
   static JavaScriptObject create(JsObject jso) {
      return JavaScriptObject.createObject();
   }

   @PatchMethod
   static Object get(JsObject jso, String name) {
      return JavaScriptObjects.getObject(jso.getJsObject(), name);
   }

   @PatchMethod
   static boolean getBoolean(JsObject jso, String name) {
      return JavaScriptObjects.getBoolean(jso.getJsObject(), name);
   }

   @PatchMethod
   static byte getByte(JsObject jso, String name) {
      return JavaScriptObjects.getByte(jso.getJsObject(), name);
   }

   @PatchMethod
   static char getChar(JsObject jso, String name) {
      return JavaScriptObjects.getChar(jso.getJsObject(), name);
   }

   @PatchMethod
   static double getDouble(JsObject jso, String name) {
      return JavaScriptObjects.getDouble(jso.getJsObject(), name);
   }

   @PatchMethod
   static float getFloat(JsObject jso, String name) {
      return JavaScriptObjects.getFloat(jso.getJsObject(), name);
   }

   @PatchMethod
   static void set(JsObject jso, String name, boolean value) {
      JavaScriptObjects.setProperty(jso.getJsObject(), name, value);
   }

   @PatchMethod
   static void set(JsObject jso, String name, byte value) {
      JavaScriptObjects.setProperty(jso.getJsObject(), name, value);
   }

   @PatchMethod
   static void set(JsObject jso, String name, char value) {
      JavaScriptObjects.setProperty(jso.getJsObject(), name, value);
   }

   @PatchMethod
   static void set(JsObject jso, String name, double value) {
      JavaScriptObjects.setProperty(jso.getJsObject(), name, value);
   }

   @PatchMethod
   static void set(JsObject jso, String name, float value) {
      JavaScriptObjects.setProperty(jso.getJsObject(), name, value);
   }

   @PatchMethod
   static void set(JsObject jso, String name, int value) {
      JavaScriptObjects.setProperty(jso.getJsObject(), name, value);
   }

   @PatchMethod
   static void set(JsObject jso, String name, JavaScriptObject value) {
      JavaScriptObjects.setProperty(jso.getJsObject(), name, value);
   }

   @PatchMethod
   static void set(JsObject jso, String name, short value) {
      JavaScriptObjects.setProperty(jso.getJsObject(), name, value);
   }

   @PatchMethod
   static void set(JsObject jso, String name, String value) {
      JavaScriptObjects.setProperty(jso.getJsObject(), name, value);
   }

   @PatchMethod
   static void setObjectInternal(JsObject jso, String name, Object value) {
      JavaScriptObjects.setProperty(jso.getJsObject(), name, value);
   }

}
