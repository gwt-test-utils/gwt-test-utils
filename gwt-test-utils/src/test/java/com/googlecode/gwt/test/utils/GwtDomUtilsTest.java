package com.googlecode.gwt.test.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.googlecode.gwt.test.GwtTestTest;

public class GwtDomUtilsTest extends GwtTestTest {

   @Test
   public void setClientHeight() {
      // Arrange
      Element e = Document.get().createAnchorElement();

      // Act
      GwtDomUtils.setClientHeight(e, 4);

      // Assert
      assertEquals(4, e.getClientHeight());
   }

   @Test
   public void setClientWidth() {
      // Arrange
      Element e = Document.get().createAnchorElement();

      // Act
      GwtDomUtils.setClientWidth(e, 4);

      // Assert
      assertEquals(4, e.getClientWidth());
   }

}
