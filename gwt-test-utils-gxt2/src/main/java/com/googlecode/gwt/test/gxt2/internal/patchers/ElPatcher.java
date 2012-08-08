package com.googlecode.gwt.test.gxt2.internal.patchers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.extjs.gxt.ui.client.core.El;
import com.google.gwt.dom.client.Element;
import com.googlecode.gwt.test.internal.utils.GwtStyleUtils;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

@PatchClass(El.class)
class ElPatcher {

   private static Pattern UNIT_PATTERN = Pattern.compile("^(\\d+)[(px|em|%|en|ex|pt|in|cm|mm|pc)]{0,1}$");

   @PatchMethod
   static String addUnits(String v, String defaultUnit) {
      if (v == null)
         return "";

      v = v.replaceAll(" ", "");

      if ("undefined".equals(v))
         return "";

      Matcher m = UNIT_PATTERN.matcher(v);
      if (m.matches()) {
         String unit = (m.groupCount() == 3) ? m.group(2)
                  : (defaultUnit != null && !"".equals(defaultUnit)) ? defaultUnit : "px";

         return m.group(1) + unit;
      }

      return v;
   }

   @PatchMethod
   static El applyStyles(El el, String styles) {
      Element elem = getWrappedElement(el);
      LinkedHashMap<String, String> styleProperties = GwtStyleUtils.getStyleProperties(elem.getAttribute("style"));

      for (Map.Entry<String, String> entry : styleProperties.entrySet()) {
         elem.getStyle().setProperty(entry.getKey(), entry.getValue());
      }

      return el;
   }

   @PatchMethod
   static void disableTextSelectInternal(com.google.gwt.user.client.Element e, boolean disable) {

   }

   @PatchMethod
   static boolean isLeftorRight(El el, String s) {
      return s != null && (s.contains("Left") || s.contains("Right"));
   }

   @PatchMethod
   static El removeStyleName(El el, String styleName) {
      if (styleName != null) {
         Element elem = getWrappedElement(el);
         elem.removeClassName(styleName);
      }
      return el;
   }

   @PatchMethod
   static El repaint(El el) {
      return el;
   }

   @PatchMethod
   static El setFocus(El el, boolean focus) {
      Element dom = getWrappedElement(el);

      if (focus) {
         dom.focus();
      } else {
         dom.blur();
      }

      return el;
   }

   private static Element getWrappedElement(El el) {
      return GwtReflectionUtils.getPrivateFieldValue(el, "dom");
   }

}
