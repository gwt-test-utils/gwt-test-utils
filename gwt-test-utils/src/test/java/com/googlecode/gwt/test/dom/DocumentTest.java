package com.googlecode.gwt.test.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Text;
import com.googlecode.gwt.test.GwtTestTest;

public class DocumentTest extends GwtTestTest {

   private Document d;

   @Test
   public void checkToString() {
      // Arrange
      d.getBody().appendChild(d.createAnchorElement());
      d.getBody().appendChild(d.createAreaElement());

      // Act
      String toString = Document.get().toString();

      // Assert
      assertEquals("<html><body><a></a><area></area></body></html>", toString);

   }

   @Test
   public void createElement() {
      // Act & Assert
      assertEquals("a", d.createAnchorElement().getTagName());
      assertEquals("area", d.createAreaElement().getTagName());
      assertEquals("base", d.createBaseElement().getTagName());
      assertEquals("body", d.createElement("body").getTagName());
      assertEquals("br", d.createBRElement().getTagName());
      assertEquals("div", d.createDivElement().getTagName());
      assertEquals("dl", d.createDLElement().getTagName());
      assertEquals("fieldset", d.createFieldSetElement().getTagName());
      assertEquals("form", d.createFormElement().getTagName());
      assertEquals("frame", d.createFrameElement().getTagName());
      assertEquals("frameset", d.createFrameSetElement().getTagName());
      assertEquals("head", d.createHeadElement().getTagName());
      assertEquals("h1", d.createHElement(1).getTagName());
      assertEquals("h2", d.createHElement(2).getTagName());
      assertEquals("h3", d.createHElement(3).getTagName());
      assertEquals("h4", d.createHElement(4).getTagName());
      assertEquals("h5", d.createHElement(5).getTagName());
      assertEquals("h6", d.createHElement(6).getTagName());
      assertEquals("hr", d.createHRElement().getTagName());
      assertEquals("iframe", d.createIFrameElement().getTagName());
      assertEquals("img", d.createImageElement().getTagName());
      assertEquals("input", d.createElement("input").getTagName());
      assertEquals("label", d.createLabelElement().getTagName());
      assertEquals("legend", d.createLegendElement().getTagName());
      assertEquals("li", d.createLIElement().getTagName());
      assertEquals("link", d.createLinkElement().getTagName());
      assertEquals("map", d.createMapElement().getTagName());
      assertEquals("meta", d.createMetaElement().getTagName());
      assertEquals("ins", d.createInsElement().getTagName());
      assertEquals("del", d.createDelElement().getTagName());
      assertEquals("object", d.createObjectElement().getTagName());
      assertEquals("ol", d.createOLElement().getTagName());
      assertEquals("optgroup", d.createOptGroupElement().getTagName());
      assertEquals("option", d.createOptionElement().getTagName());
      assertEquals("p", d.createPElement().getTagName());
      assertEquals("param", d.createParamElement().getTagName());
      assertEquals("pre", d.createPreElement().getTagName());
      assertEquals("q", d.createQElement().getTagName());
      assertEquals("blockquote", d.createBlockQuoteElement().getTagName());
      assertEquals("script", d.createScriptElement().getTagName());
      assertEquals("select", d.createSelectElement().getTagName());
      assertEquals("span", d.createSpanElement().getTagName());
      assertEquals("style", d.createStyleElement().getTagName());
      assertEquals("caption", d.createCaptionElement().getTagName());
      assertEquals("td", d.createTDElement().getTagName());
      assertEquals("th", d.createTHElement().getTagName());
      assertEquals("col", d.createColElement().getTagName());
      assertEquals("colgroup", d.createColGroupElement().getTagName());
      assertEquals("table", d.createTableElement().getTagName());
      assertEquals("tbody", d.createTBodyElement().getTagName());
      assertEquals("tfoot", d.createTFootElement().getTagName());
      assertEquals("thead", d.createTHeadElement().getTagName());
      assertEquals("textarea", d.createTextAreaElement().getTagName());
      assertEquals("title", d.createTitleElement().getTagName());
      assertEquals("ul", d.createULElement().getTagName());
   }

   @Test
   public void createImageInputElement() {
      // Act
      InputElement e = d.createImageInputElement();

      // Assert
      assertEquals("image", e.getType());
   }

   @Test
   public void createPushButtonElement() {
      // Act
      ButtonElement e = d.createPushButtonElement();

      // Assert
      assertEquals("button", e.getTagName());
      assertEquals("button", e.getType());
   }

   @Test
   public void createRadioInputElement() {
      // Act
      InputElement e = d.createRadioInputElement("test");

      // Assert
      assertEquals("RADIO", e.getType());
      assertEquals("test", e.getName());
   }

   @Test
   public void createResetButtonElement() {
      // Act
      ButtonElement e = d.createResetButtonElement();

      // Assert
      assertEquals("button", e.getTagName());
      assertEquals("reset", e.getType());
   }

   @Test
   public void createSubmitButtonElement() {
      // Act
      ButtonElement e = d.createSubmitButtonElement();

      // Assert
      assertEquals("button", e.getTagName());
      assertEquals("submit", e.getType());
   }

   @Test
   public void createTextNode() {
      // Act
      String data = "myData";
      Text text = d.createTextNode(data);

      // Assert
      assertEquals(Node.TEXT_NODE, text.getNodeType());
      assertEquals(data, text.getData());
   }

   @Test
   public void getBodyOffsetLeft() {
      // Act
      int result = d.getBodyOffsetLeft();

      // Assert
      assertEquals(0, result);
   }

   @Test
   public void getBodyOffsetTop() {
      // Act
      int result = d.getBodyOffsetTop();

      // Assert
      assertEquals(0, result);
   }

   @Test
   public void getDocumentElement() {
      // Act
      Element e = d.getDocumentElement();

      // Assert
      assertEquals("HTML", e.getTagName());
      assertEquals("HTML", e.getNodeName());
      assertEquals(Node.ELEMENT_NODE, e.getNodeType());
      assertEquals(d.getChild(0), e);
   }

   @Test
   public void getDomain() {
      // Act & Assert
      assertNull(d.getDomain());
   }

   @Test
   public void getElementById_InBody() {
      // Arrange
      AnchorElement a = Document.get().createAnchorElement();
      a.setId("myId");
      DivElement div = Document.get().createDivElement();
      div.appendChild(a);
      d.getBody().appendChild(div);

      // Act
      Element result = d.getElementById("myId");

      // Assert
      assertEquals(a, result);
   }

   @Test
   public void getElementById_NotFound() {
      // Arrange
      AnchorElement a1 = Document.get().createAnchorElement();
      AnchorElement a2 = Document.get().createAnchorElement();
      AnchorElement a3 = Document.get().createAnchorElement();
      DivElement d1 = Document.get().createDivElement();
      d.appendChild(a1);
      d.appendChild(a1);
      d.appendChild(a2);
      a2.appendChild(a3);
      d.appendChild(d1);

      // Act
      Element result = d.getElementById("myId");

      // Assert
      assertNull(result);
   }

   @Test
   public void getElementByIdFound() {
      // Arrange
      AnchorElement a1 = Document.get().createAnchorElement();
      AnchorElement a2 = Document.get().createAnchorElement();
      AnchorElement a3 = Document.get().createAnchorElement();
      a3.setId("myId");
      DivElement d1 = Document.get().createDivElement();
      d.appendChild(a1);
      d.appendChild(a1);
      d.appendChild(a2);
      a2.appendChild(a3);
      d.appendChild(d1);

      // Act
      Element result = d.getElementById("myId");

      // Assert
      assertEquals(a3, result);
   }

   @Test
   public void getElementByTagName() {
      // Arrange
      AnchorElement a1 = Document.get().createAnchorElement();
      AnchorElement a2 = Document.get().createAnchorElement();
      AnchorElement a3 = Document.get().createAnchorElement();
      DivElement d1 = Document.get().createDivElement();
      d.appendChild(a1);
      d.appendChild(a1);
      d.appendChild(a2);
      a2.appendChild(a3);
      d.appendChild(d1);

      // Act
      NodeList<Element> nodes = d.getElementsByTagName("a");

      // Assert
      assertEquals(3, nodes.getLength());
      assertEquals(a1, nodes.getItem(0));
      assertEquals(a2, nodes.getItem(1));
      assertEquals(a3, nodes.getItem(2));
   }

   @Test
   public void getReferrer() {
      assertEquals("", d.getReferrer());
   }

   @Before
   public void initDocument() {
      d = Document.get();
   }

   @Test
   public void isCSS1Compat() {
      // Act
      boolean result = d.isCSS1Compat();

      // Assert
      assertEquals(false, result);
   }

   @Test
   public void scrollLeft() {
      // Pre-Assert
      assertEquals(0, d.getScrollLeft());

      // Act
      d.setScrollLeft(3);

      // Assert
      assertEquals(3, d.getScrollLeft());
   }

   @Test
   public void scrollTop() {
      // Pre-Assert
      assertEquals(0, d.getScrollTop());

      // Act
      d.setScrollTop(3);

      // Assert
      assertEquals(3, d.getScrollTop());
   }

}
