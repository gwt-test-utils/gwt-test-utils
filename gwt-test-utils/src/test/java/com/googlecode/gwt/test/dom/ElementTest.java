package com.googlecode.gwt.test.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Float;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.internal.utils.JsoUtils;

public class ElementTest extends GwtTestTest {

   private Element e;

   @Test
   public void attribute() {
      // Pre-Assert
      assertEquals("", e.getAttribute("input"));

      // Act
      e.setAttribute("input", "text");

      // Assert
      assertEquals("text", e.getAttribute("input"));
   }

   @Before
   public void beforeElementTest() {
      e = Document.get().createDivElement();
   }

   @Test
   public void cast_OK() {
      // Act
      DivElement casted = e.cast();

      // Assert
      assertNotNull(casted);
   }

   @Test
   public void checkHashCode() {
      // Arrange
      Map<Element, String> map = new HashMap<Element, String>();

      // Act
      map.put(e, "a string value");
      map.put(e, "this value should have overrided the first one");

      // Assert
      assertEquals("this value should have overrided the first one", map.get(e));
   }

   @Test
   public void checkToString() {
      // Arrange
      DivElement div = Document.get().createDivElement();
      div.setAttribute("someAttr", "myVal");
      div.getStyle().setBackgroundColor("black");
      div.getStyle().setFloat(Float.LEFT);
      div.getStyle().setProperty("backgroundColor", "white");
      div.setInnerHTML("<span>in span</span> out span");

      // Act
      String html = div.toString();

      // Assert
      assertEquals(
               "<div someattr=\"myVal\" style=\"float: left; background-color: white; \"><span>in span</span> out span</div>",
               html);

   }

   @Test
   public void className() {
      // Act 1
      e.setClassName("testClass");

      // Assert 1
      assertEquals("testClass", e.getClassName());
      assertEquals("testClass", e.getAttribute("class"));
      assertEquals("testClass", e.getAttribute("CLASS"));
      assertEquals("", e.getAttribute("className"));
      assertEquals("", e.getAttribute("CLASSNAME"));
      assertNull(e.getPropertyString("class"));
      assertNull(e.getPropertyString("CLASS"));
      assertEquals("testClass", e.getPropertyString("className"));
      assertNull(e.getPropertyString("CLASSNAME"));

      // Act 2
      e.addClassName("addon");

      // Assert 2
      assertEquals("testClass addon", e.getClassName());
      assertEquals("testClass addon", e.getAttribute("class"));

      // Act 3
      e.setAttribute("class", "override");

      // Assert 3
      assertEquals("override", e.getClassName());
      assertEquals("override", e.getAttribute("class"));
   }

   @Test
   public void clone_Deep() {
      // Arrange
      e.setTitle("title");
      e.setPropertyBoolean("bool", true);

      Element child = Document.get().createAnchorElement();
      child.setTitle("child");
      e.appendChild(child);

      // Act
      DivElement newNode = e.cloneNode(true).cast();

      // Assert
      assertEquals("title", newNode.getTitle());
      assertNull("Cloned element's parent should be null", newNode.getParentNode());
      assertEquals(true, newNode.getPropertyBoolean("bool"));
      assertEquals("Deep cloned element should have child nodes", 1,
               newNode.getChildNodes().getLength());
      assertTrue(child != newNode.getChildNodes().getItem(0));
      assertEquals(1, e.getChildNodes().getLength());
   }

   @Test
   public void clone_NotDeep() {
      // Arrange
      e.setTitle("title");
      e.setPropertyBoolean("bool", true);

      AnchorElement child = Document.get().createAnchorElement();
      child.setTitle("child");
      e.appendChild(child);

      // Act
      DivElement newNode = e.cloneNode(false).cast();

      // Assert
      assertEquals("title", newNode.getTitle());
      assertNull("Cloned element's parent should be null", newNode.getParentNode());
      assertEquals(true, newNode.getPropertyBoolean("bool"));
      assertEquals("Not deep cloned element should not have child nodes", 0,
               newNode.getChildNodes().getLength());
      assertEquals(1, e.getChildNodes().getLength());
   }

   @Test
   public void dir() {
      // Act
      e.setDir("dir");

      // Assert
      assertEquals("dir", e.getDir());
   }

   @Test
   public void domImplementation() {
      // Arrange
      e.setAttribute("test", "testAttr");

      // Assert getAttribute() is case insensitive
      assertEquals("testAttr", e.getAttribute("test"));
      assertEquals("testAttr", e.getAttribute("Test"));

      // Assert hasAttribute is case insensitive
      assertTrue(e.hasAttribute("teST"));

      // Assert removeAttribute is case insensitve
      e.removeAttribute("tEst");
      assertEquals("", e.getAttribute("test"));
      assertFalse(e.hasAttribute("teST"));

      // Assert "non standard" DOM properties returns 'undefined' for String,
      // Object and JSO
      assertNull(e.getPropertyString("test"));
      assertFalse(e.getPropertyBoolean("test"));
      assertEquals(0, e.getPropertyInt("test"));
      assertEquals(new Double(0.0), (Double) e.getPropertyDouble("test"));
      assertNull(e.getPropertyObject("test"));
      assertNull(e.getPropertyJSO("test"));

      // Assert "standard" DOM properties returns "" for String
      assertEquals("", e.getPropertyString("className"));
      assertNull(e.getPropertyString("classnamE"));

      e.setPropertyString("className", "testClass");
      assertEquals("testClass", e.getPropertyString("className"));
      // Special case "class" and "className"
      assertNull(e.getPropertyString("class"));
      assertEquals("testClass", e.getAttribute("class"));
      assertEquals("", e.getAttribute("CLASSNAME"));
      assertNull(e.getPropertyString("CLASSNAME"));

      // Assert on Style JSO
      assertEquals("", e.getAttribute("style")); // prints ""
      assertEquals("", e.getPropertyString("style"));
   }

   @Test
   public void getElementByTagName() {
      // Arrange
      AnchorElement ae0 = Document.get().createAnchorElement();
      AnchorElement ae1 = Document.get().createAnchorElement();
      ButtonElement be = Document.get().createPushButtonElement();
      e.appendChild(ae0);
      e.appendChild(ae1);
      e.appendChild(be);

      // Act
      NodeList<Element> anchorList = e.getElementsByTagName("a");
      NodeList<Element> buttonList = e.getElementsByTagName("button");
      NodeList<Element> allList = e.getElementsByTagName("*");

      // Assert
      assertEquals(2, anchorList.getLength());
      assertEquals(ae0, anchorList.getItem(0));
      assertEquals(ae1, anchorList.getItem(1));

      assertEquals(1, buttonList.getLength());
      assertEquals(be, buttonList.getItem(0));

      assertEquals(3, allList.getLength());
      assertEquals(ae0, allList.getItem(0));
      assertEquals(ae1, allList.getItem(1));
      assertEquals(be, allList.getItem(2));
   }

   @Test
   public void getFirstChildElement() {
      // Pre-Assert
      assertNull(e.getFirstChildElement());

      // Arrange
      Node node = Document.get().createTextNode("test");
      ButtonElement be0 = Document.get().createPushButtonElement();
      ButtonElement be1 = Document.get().createPushButtonElement();
      e.appendChild(node);
      e.appendChild(be0);
      e.appendChild(be1);

      // Act & Assert
      assertEquals(be0, e.getFirstChildElement());
   }

   @Test
   public void getNextSiblingElement() {
      // Pre-Assert
      assertNull(e.getNextSiblingElement());

      // Arrange
      ButtonElement be0 = Document.get().createPushButtonElement();
      ButtonElement be1 = Document.get().createPushButtonElement();
      e.appendChild(be0);
      e.appendChild(JsoUtils.newText("test1", Document.get()));
      e.appendChild(be1);
      e.appendChild(JsoUtils.newText("test2", Document.get()));

      // Act & Assert
      assertEquals(be1, be0.getNextSiblingElement());
      assertNull(be1.getNextSiblingElement());
   }

   @Test
   public void getOffset() {
      // Arrange
      Element parent = Document.get().createElement("a");
      parent.appendChild(e);

      // Act & Assert
      assertEquals(0, e.getOffsetHeight());
      assertEquals(0, e.getOffsetLeft());
      assertEquals(0, e.getOffsetTop());
      assertEquals(0, e.getOffsetWidth());
      assertEquals(parent, e.getOffsetParent());
   }

   @Test
   public void getParentElement() {
      // Arrange
      Element otherParent = Document.get().createDivElement();
      Element child = Document.get().createBaseElement();
      e.appendChild(child);

      // Act and assert
      assertEquals(e, child.getParentElement());

      // Act 2
      otherParent.appendChild(child);

      // Assert 2
      assertFalse(
               "Child nodes list should be empty since the only child has been attached to another parent node",
               e.hasChildNodes());
   }

   @Test
   public void hasAttribute() {
      // Arrange
      e.setAttribute("myAttr", "value");

      // Act & Assert
      assertTrue(e.hasAttribute("myAttr"));
   }

   @Test
   public void id() {
      // Act 1
      e.setId("myId");

      // Assert 1
      assertEquals("myId", e.getId());
      assertEquals("myId", e.getAttribute("id"));

      // Act 2
      e.setAttribute("id", "updatedId");

      // Assert 2
      assertEquals("updatedId", e.getId());
      assertEquals("updatedId", e.getAttribute("id"));
   }

   @Test
   public void innerHTML() {
      // Act
      e.setInnerHTML("<h1>test</h1>");

      // Assert
      assertEquals("<h1>test</h1>", e.getInnerHTML());
      assertEquals(1, e.getChildCount());
      HeadingElement h1 = e.getChild(0).cast();
      assertEquals("H1", h1.getTagName());
      assertEquals("test", h1.getInnerText());
   }

   @Test
   public void innerText() {
      // Act
      e.setInnerText("myText");

      // Assert
      assertEquals("myText", e.getInnerText());
   }

   @Test
   public void isOrHasChild() {
      // Arrange
      AnchorElement child = Document.get().createAnchorElement();
      e.appendChild(child);
      AnchorElement notAChild = Document.get().createAnchorElement();

      // Act & Assert
      assertTrue(e.isOrHasChild(e));
      assertTrue(e.isOrHasChild(child));
      assertFalse(e.isOrHasChild(notAChild));
   }

   @Test
   public void lang() {
      // Act
      e.setLang("myLang");

      // Assert
      assertEquals("myLang", e.getLang());
   }

   @Test
   public void propertyBoolean_False() {
      // Act
      e.setPropertyBoolean("prop", false);

      // Assert
      assertFalse(e.getPropertyBoolean("prop"));
   }

   @Test
   public void propertyBoolean_True() {
      // Pre-Assert
      assertFalse(e.getPropertyBoolean("prop"));
      // Act
      e.setPropertyBoolean("prop", true);

      // Assert
      assertTrue(e.getPropertyBoolean("prop"));
   }

   @Test
   public void propertyDouble() {
      // Pre-Assert
      assertEquals(new Double(0), (Double) e.getPropertyDouble("prop"));

      // Act
      e.setPropertyDouble("prop", 23);

      // Assert
      assertEquals(new Double(23), (Double) e.getPropertyDouble("prop"));
   }

   @Test
   public void propertyInt() {
      // Pre-Assert
      assertEquals(0, e.getPropertyInt("prop"));

      // Act
      e.setPropertyInt("prop", 2);

      // Assert
      assertEquals(2, e.getPropertyInt("prop"));
   }

   @Test
   public void propertyString() {
      // Pre-Assert
      assertNull(e.getPropertyString("prop"));

      // Act
      e.setPropertyString("prop", "test");

      // Assert
      assertEquals("test", e.getPropertyString("prop"));
   }

   @Test
   public void removeAttribute() {
      // Arrange
      e.setAttribute("test", "value");

      // Act
      e.removeAttribute("Test");

      // Assert
      assertEquals("Removed attribute should return emptyString", "", e.getAttribute("test"));
   }

   @Test
   public void scrollLeft() {
      // Pre-Assert
      assertEquals(0, e.getScrollLeft());

      // Act
      e.setScrollLeft(3);

      // Assert
      assertEquals(3, e.getScrollLeft());
   }

   @Test
   public void scrollTop() {
      // Pre-Assert
      assertEquals(0, e.getScrollTop());

      // Act
      e.setScrollTop(3);
      assertEquals(3, e.getScrollTop());
   }

   @Test
   public void style() {
      // Act
      e.getStyle().setProperty("test", "value");

      // Assert
      assertEquals("value", e.getStyle().getProperty("test"));
   }

   @Test
   public void tagName() {
      // Act & Assert
      assertEquals("div", e.getTagName());
      assertEquals("", e.getAttribute("tagName"));
      assertEquals("", e.getAttribute("TAGNAME"));
      assertEquals("DIV", e.getPropertyString("tagName"));
      assertNull(e.getPropertyString("TAGNAME"));
   }

   @Test
   public void title() {
      // Pre-Assert
      assertEquals("", e.getTitle());
      assertEquals("", e.getAttribute("title"));
      assertEquals("", e.getAttribute("titLe"));
      assertEquals("", e.getPropertyString("title"));
      assertNull(e.getPropertyString("titLe"));
      assertNull(e.getPropertyObject("title"));
      assertNull(e.getPropertyObject("titLe"));

      // Act
      e.setTitle("MyTitle");

      // Assert
      assertEquals("MyTitle", e.getTitle());
      assertEquals("MyTitle", e.getAttribute("title"));
      assertEquals("MyTitle", e.getAttribute("titLe"));
      assertEquals("MyTitle", e.getPropertyString("title"));
      assertNull(e.getPropertyString("titLe"));
      assertEquals("MyTitle", e.getPropertyObject("title"));
      assertNull(e.getPropertyObject("titLe"));
   }

}
