package com.googlecode.gwt.test.xml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.xml.client.CDATASection;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;
import com.googlecode.gwt.test.GwtTestTest;

public class XMLDocumentTest extends GwtTestTest {

   @Test
   public void createCDATASection() {
      // Arrange
      Document document = XMLParser.createDocument();

      // Act
      CDATASection cdata = document.createCDATASection("my cdata Value");

      // Assert
      assertEquals("my cdata Value", cdata.getData());
      assertEquals(document.getDocumentElement(), cdata.getOwnerDocument().getDocumentElement());
   }

   @Test
   public void createElement() {
      // Arrange
      Document document = XMLParser.createDocument();

      // Act
      Element element = document.createElement("elem");

      // Assert
      assertEquals("elem", element.getTagName());
      assertEquals(document.getDocumentElement(), element.getOwnerDocument().getDocumentElement());
   }

   @Test
   public void createTextNode() {
      // Arrange
      Document document = XMLParser.createDocument();

      // Act
      Text text = document.createTextNode("my text");

      // Assert
      assertEquals("my text", text.getData());
      assertEquals(document.getDocumentElement(), text.getOwnerDocument().getDocumentElement());
   }

   @Test
   public void documentToString() {
      Document document = XMLParser.createDocument();
      Element e = document.createElement("ThisIsATest");
      e.appendChild(document.createTextNode("SomeTextNode"));
      document.appendChild(e);

      assertEquals("<ThisIsATest>SomeTextNode</ThisIsATest>", document.toString());
   }

   @Test
   public void elementToString() {
      Document document = XMLParser.createDocument();
      Element e = document.createElement("ThisIsATest");
      e.appendChild(document.createTextNode("SomeTextNode"));
      document.appendChild(e);

      assertEquals("<ThisIsATest>SomeTextNode</ThisIsATest>", e.toString());
   }

}
