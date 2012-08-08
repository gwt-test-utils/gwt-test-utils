package com.googlecode.gwt.test.internal.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.dom.client.BRElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Text;
import com.googlecode.gwt.test.GwtTestTest;

public class GwtHtmlParserTest extends GwtTestTest {

   @Test
   public void parse() throws Exception {
      // Arrange
      String html = "<div id=\"parent0\"></div><div id=\"parent1\"><div id=\"child0\"><span class=\"spanClass\" >test</span></div><BR><DIV id=\"child2\" style=\"color:red; font-style:italic; font-weight:bold; font-family:Arial\"></div>";

      // Act
      NodeList<Node> nodes = GwtHtmlParser.parse(html);

      // Assert
      assertEquals(2, nodes.getLength());
      DivElement parent0 = (DivElement) nodes.getItem(0);
      assertEquals("parent0", parent0.getId());
      assertEquals(0, parent0.getChildCount());

      DivElement parent1 = (DivElement) nodes.getItem(1);
      assertEquals("parent1", parent1.getId());
      assertEquals(3, parent1.getChildCount());

      DivElement child0 = (DivElement) parent1.getChild(0);
      assertEquals("child0", child0.getId());
      assertEquals(1, child0.getChildCount());

      SpanElement span = (SpanElement) child0.getChild(0);
      assertEquals("", span.getId());
      assertEquals("spanClass", span.getClassName());
      assertEquals(1, span.getChildCount());
      assertEquals(Node.TEXT_NODE, span.getChild(0).getNodeType());
      Text text = span.getChild(0).cast();
      assertEquals("test", text.getData());
      assertEquals("test", span.getInnerText());

      BRElement br = (BRElement) parent1.getChild(1);
      assertEquals("", br.getId());

      DivElement child2 = (DivElement) parent1.getChild(2);
      assertEquals("child2", child2.getId());
      assertEquals(0, child2.getChildCount());
      Style style = child2.getStyle();
      assertEquals("red", style.getColor());
   }
}
