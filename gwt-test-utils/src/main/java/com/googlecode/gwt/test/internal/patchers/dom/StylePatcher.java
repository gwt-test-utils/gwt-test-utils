package com.googlecode.gwt.test.internal.patchers.dom;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.googlecode.gwt.test.internal.utils.GwtStyleUtils;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(Style.class)
class StylePatcher {

   // map initialized with default style values
   private static final Map<String, String> DEFAULT_STYLE_VALUES = new HashMap<String, String>() {

      private static final long serialVersionUID = 1L;

      {
         put("whiteSpace", "nowrap");
      }
   };

   private static final String STYLE_BORDER_BOTTOM_WIDTH = "border-bottom-width";
   private static final String STYLE_BORDER_LEFT_WIDTH = "border-left-width";
   private static final String STYLE_BORDER_RIGHT_WIDTH = "border-right-width";
   private static final String STYLE_BORDER_TOP_WIDTH = "border-top-width";
   private static final String STYLE_Z_INDEX = "zIndex";

   @PatchMethod
   static void clearBorderWidth(Style style) {
      Map<String, String> properties = GwtStyleUtils.getStyleProperties(style);
      properties.remove(STYLE_BORDER_BOTTOM_WIDTH);
      properties.remove(STYLE_BORDER_LEFT_WIDTH);
      properties.remove(STYLE_BORDER_RIGHT_WIDTH);
      properties.remove(STYLE_BORDER_TOP_WIDTH);
   }

   @PatchMethod
   static void clearFloat(Style style) {
      GwtStyleUtils.getStyleProperties(style).remove("float");
   }

   @PatchMethod
   static String getBorderWidth(Style style) {
      return getPropertyImpl(style, STYLE_BORDER_TOP_WIDTH);
   }

   @PatchMethod
   static String getPropertyImpl(Style style, String propertyName) {
      String value = GwtStyleUtils.getStyleProperties(style).get(propertyName);

      if (value == null) {
         String defaultValue = DEFAULT_STYLE_VALUES.get(propertyName);
         value = defaultValue != null ? defaultValue : "";
      }

      return value;
   }

   @PatchMethod
   static void setBorderWidth(Style style, double value, Unit unit) {
      double modulo = value % 1;
      String completeValue = modulo == 0 ? Integer.toString((int) value) + unit.getType()
               : Double.toString(value) + unit.getType();
      GwtStyleUtils.setProperty(style, STYLE_BORDER_BOTTOM_WIDTH, completeValue);
      GwtStyleUtils.setProperty(style, STYLE_BORDER_LEFT_WIDTH, completeValue);
      GwtStyleUtils.setProperty(style, STYLE_BORDER_RIGHT_WIDTH, completeValue);
      GwtStyleUtils.setProperty(style, STYLE_BORDER_TOP_WIDTH, completeValue);
   }

   @PatchMethod
   static void setFloat(Style style, Float value) {
      GwtStyleUtils.setProperty(style, "float", value.getCssName());
   }

   @PatchMethod
   static void setPropertyImpl(Style style, String propertyName, String propertyValue) {
      GwtStyleUtils.setProperty(style, propertyName, propertyValue);
   }

   @PatchMethod
   static String getZIndex(Style style) {
     return getPropertyImpl(style, STYLE_Z_INDEX);
   }
}
