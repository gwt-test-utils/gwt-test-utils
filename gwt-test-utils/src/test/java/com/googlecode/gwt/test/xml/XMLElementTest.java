package com.googlecode.gwt.test.xml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;
import com.googlecode.gwt.test.GwtTestTest;

public class XMLElementTest extends GwtTestTest {

   @Test
   public void setAttribute() {
      // Arrange
      Document document = XMLParser.createDocument();
      Element element = document.createElement("elem");

      // Act
      element.setAttribute("myAttr", "myValue");

      // Assert
      assertEquals("myValue", element.getAttribute("myAttr"));
   }

}
