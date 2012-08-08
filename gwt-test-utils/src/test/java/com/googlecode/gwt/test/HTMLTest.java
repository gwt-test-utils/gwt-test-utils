package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.ui.HTML;

public class HTMLTest extends GwtTestTest {

   @Test
   public void checkToString() {
      // Arrange
      HTML html = new HTML("this is a <b>great</b> test.<br>Enjoy!");

      // Act
      String result = html.toString();

      // Assert
      assertEquals("<div class=\"gwt-HTML\">this is a <b>great</b> test.<br>Enjoy!</div>", result);
   }

   @Test
   public void getHTML() {
      // Arrange
      HTML html = new HTML("<a href='#'>link</a><br/>test&nbsp;test");

      // Act
      String result = html.getHTML();

      // Assert
      assertEquals("<a href=\"#\">link</a><br>test&nbsp;test", result);
   }

   @Test
   public void getText() {
      // Arrange
      HTML html = new HTML("<a href='#'>link</a><br/>test&nbsp;test&nbsp;");

      // Act
      String result = html.getText();

      // Assert
      assertEquals("linktest test ", result);

      // Act 2
      html.setText("override <b>not bold text</b>");

      // Assert 2
      assertEquals("override <b>not bold text</b>", html.getText());
   }

   @Test
   public void html_withAnchor() {
      // Arrange
      HTML widget = new HTML("<a href=\"foo\" target=\"bar\">baz</a>");

      // Act
      NodeList<Element> nodeList = widget.getElement().getElementsByTagName("a");

      // Assert
      assertEquals(1, nodeList.getLength());
      AnchorElement anchor = nodeList.getItem(0).cast();
      assertEquals("foo", anchor.getHref());
      assertEquals("bar", anchor.getTarget());
   }

   @Test
   public void html_withSpecialChars() {
      // Arrange
      HTML html = new HTML("<span>R&eacute;sidence&nbsp;:&nbsp;</span>");

      // Act
      String result = html.getHTML();

      // Assert
      assertEquals("<span>Résidence&nbsp;:&nbsp;</span>", result);
   }

}
