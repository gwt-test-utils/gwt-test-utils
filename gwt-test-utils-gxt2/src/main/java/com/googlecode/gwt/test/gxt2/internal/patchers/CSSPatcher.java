package com.googlecode.gwt.test.gxt2.internal.patchers;

import com.extjs.gxt.ui.client.util.CSS;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Text;
import com.google.gwt.user.client.Element;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(CSS.class)
class CSSPatcher {

   /**
    * Adds a rules string in a <code>&ltdstyle&gt&ltd/style&gt</code> element.
    * 
    * @param style the <code>&ltdstyle&gt&ltd/style&gt</code> element
    * @param cssStr the rules string
    */
   @PatchMethod
   static void setRules(Element style, String cssStr) {
      style.setAttribute("type", "text/css");

      while (style.hasChildNodes()) {
         style.removeChild(style.getChild(0));
      }

      Text text = Document.get().createTextNode(cssStr);
      style.appendChild(text);
   }

}
