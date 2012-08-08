package com.googlecode.gwt.test.gxt2.internal.patchers;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.js.JsArray;
import com.google.gwt.core.client.JavaScriptObject;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

@PatchClass(JsArray.class)
class JsArrayPatcher {

   private static final String INNER_LIST = "GXT_JS_ARRAY_INNER_LIST";

   @PatchMethod
   static void add(JsArray jsArray, boolean value) {
      getJsArrayJSO(jsArray).add(value);
   }

   @PatchMethod
   static void add(JsArray jsArray, byte value) {
      getJsArrayJSO(jsArray).add(value);
   }

   @PatchMethod
   static void add(JsArray jsArray, char value) {
      getJsArrayJSO(jsArray).add(value);
   }

   @PatchMethod
   static void add(JsArray jsArray, double value) {
      getJsArrayJSO(jsArray).add(value);
   }

   @PatchMethod
   static void add(JsArray jsArray, float value) {
      getJsArrayJSO(jsArray).add(value);
   }

   @PatchMethod
   static void add(JsArray jsArray, int value) {
      getJsArrayJSO(jsArray).add(value);
   }

   @PatchMethod
   static void add(JsArray jsArray, JavaScriptObject value) {
      getJsArrayJSO(jsArray).add(value);
   }

   @PatchMethod
   static void add(JsArray jsArray, short value) {
      getJsArrayJSO(jsArray).add(value);
   }

   @PatchMethod
   static void add(JsArray jsArray, String value) {
      getJsArrayJSO(jsArray).add(value);
   }

   @PatchMethod
   static void addObjectInternal(JsArray jsArray, Object value) {
      getJsArrayJSO(jsArray).add(value);
   }

   @PatchMethod
   static JavaScriptObject create(JsArray jsArray) {
      JavaScriptObject jsArrayJso = JavaScriptObject.createObject();
      JavaScriptObjects.setProperty(jsArrayJso, INNER_LIST, new ArrayList<Object>());

      return jsArrayJso;
   }

   @PatchMethod
   static Object get(JsArray jsArray, int index) {
      return getJsArrayJSO(jsArray).get(index);
   }

   @PatchMethod
   static boolean getBoolean(JsArray jsArray, int index) {
      List<Object> list = getJsArrayJSO(jsArray);

      if (list.size() <= index) {
         return false;
      }

      Object o = list.get(index);
      if (Boolean.TYPE.isInstance(o)) {
         return (Boolean) o;
      } else {
         return false;
      }
   }

   @PatchMethod
   static byte getByte(JsArray jsArray, int index) {
      List<Object> list = getJsArrayJSO(jsArray);

      if (list.size() <= index) {
         return 0;
      }

      Object o = list.get(index);
      if (Byte.TYPE.isInstance(o)) {
         return (Byte) o;
      } else {
         return 0;
      }
   }

   @PatchMethod
   static char getChar(JsArray jsArray, int index) {
      List<Object> list = getJsArrayJSO(jsArray);

      if (list.size() <= index) {
         return 0;
      }

      Object o = list.get(index);
      if (Character.TYPE.isInstance(o)) {
         return (Character) o;
      } else {
         return 0;
      }
   }

   @PatchMethod
   static double getDouble(JsArray jsArray, int index) {
      List<Object> list = getJsArrayJSO(jsArray);

      if (list.size() <= index) {
         return 0;
      }

      Object o = list.get(index);
      if (Double.TYPE.isInstance(o)) {
         return (Double) o;
      } else {
         return 0;
      }
   }

   @PatchMethod
   static float getFloat(JsArray jsArray, int index) {
      List<Object> list = getJsArrayJSO(jsArray);

      if (list.size() <= index) {
         return 0;
      }

      Object o = list.get(index);
      if (Float.TYPE.isInstance(o)) {
         return (Float) o;
      } else {
         return 0;
      }
   }

   @PatchMethod
   static int getInt(JsArray jsArray, int index) {
      List<Object> list = getJsArrayJSO(jsArray);

      if (list.size() <= index) {
         return 0;
      }

      Object o = list.get(index);
      if (Integer.TYPE.isInstance(o)) {
         return (Integer) o;
      } else {
         return 0;
      }
   }

   @PatchMethod
   static short getShort(JsArray jsArray, int index) {
      List<Object> list = getJsArrayJSO(jsArray);

      if (list.size() <= index) {
         return 0;
      }

      Object o = list.get(index);
      if (Short.TYPE.isInstance(o)) {
         return (Short) o;
      } else {
         return 0;
      }
   }

   @PatchMethod
   static String getString(JsArray jsArray, int index) {
      List<Object> list = getJsArrayJSO(jsArray);

      if (list.size() <= index) {
         return "";
      }

      Object o = list.get(index);
      if (String.class.isInstance(o)) {
         return (String) o;
      } else {
         return "";
      }
   }

   @PatchMethod
   static int size(JsArray jsArray) {
      return getJsArrayJSO(jsArray).size();
   }

   @SuppressWarnings("unchecked")
   private static List<Object> getJsArrayJSO(JsArray jsArray) {
      JavaScriptObject jsArrayJSO = GwtReflectionUtils.getPrivateFieldValue(jsArray, "jsArray");

      return (List<Object>) JavaScriptObjects.getObject(jsArrayJSO, INNER_LIST);
   }
}
