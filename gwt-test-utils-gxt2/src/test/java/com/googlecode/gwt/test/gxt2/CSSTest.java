package com.googlecode.gwt.test.gxt2;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.extjs.gxt.ui.client.util.CSS;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;

public class CSSTest extends GwtGxtTest {

   @Test
   public void addStyleSheet() {
      // Act
      CSS.addStyleSheet("myStyle", "http://test.com/style.css");

      // Assert
      Element e = Document.get().getElementById("myStyle");
      assertEquals("link", e.getTagName());
      assertEquals("http://test.com/style.css", e.getPropertyString("href"));
   }

   @Test
   public void setRules() {
      // Arrange
      com.google.gwt.user.client.Element e = Document.get().createElement("style").cast();

      // Act
      CSS.setRules(e, ".test {background-color:red}");

      // Assert
      assertEquals("<style type=\"text/css\">.test {background-color:red}</style>", e.toString());
   }

}
