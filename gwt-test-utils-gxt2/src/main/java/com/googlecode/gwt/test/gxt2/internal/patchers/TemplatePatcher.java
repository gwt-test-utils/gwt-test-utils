package com.googlecode.gwt.test.gxt2.internal.patchers;

import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.core.Template;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

@SuppressWarnings("unchecked")
@PatchClass(Template.class)
class TemplatePatcher {

   private static final String JS_ARRAY_INNER_LIST = "GXT_JS_ARRAY_INNER_LIST";

   @PatchMethod
   static Element appendInternal(final JavaScriptObject t, final Element el,
            final JavaScriptObject values) {

      el.setInnerHTML(applyInternal(t, values));
      return el;
   }

   @PatchMethod
   static String applyInternal(final JavaScriptObject t, final JavaScriptObject values) {
      Element template = t.cast();
      String templHtml = template.getInnerHTML();

      List<Object> array = (List<Object>) JavaScriptObjects.getObject(values, JS_ARRAY_INNER_LIST);

      if (array != null) {
         for (int i = 0; i < array.size(); i++) {
            Object val = array.get(i);
            templHtml = templHtml.replaceAll("\\{" + i + "\\}", val.toString());
         }
      } else {
         for (Map.Entry<String, Object> entry : JavaScriptObjects.entrySet(values)) {
            templHtml = templHtml.replaceAll("\\{" + entry.getKey() + "\\}",
                     entry.getValue().toString());
         }
      }

      return templHtml;
   }

   @PatchMethod
   static void compile(final Template tem) {
   }

   @PatchMethod
   static JavaScriptObject create(final String html) {
      Element template = Document.get().createDivElement().cast();
      template.setInnerHTML(html);
      return template;
   }

   @PatchMethod
   static String getHtml(final JavaScriptObject t) {
      Element template = t.cast();
      return template.getInnerHTML();
   }

   @PatchMethod
   static Element insertInternal(final String method, final JavaScriptObject t, final Element el,
            final JavaScriptObject values) {

      el.setInnerHTML(applyInternal(t, values));
      return el;
   }

}
