package com.googlecode.gwt.test;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.ui.Label;
import org.junit.Test;

import static org.junit.Assert.*;

public class LabelTest extends GwtTestTest {

    @SuppressWarnings("deprecation")
    @Test
    public void direction() {
        // Arrange
        Label label = new Label();
        // Pre-Assert
        assertEquals(Direction.DEFAULT, label.getDirection());

        // Act
        label.setDirection(Direction.RTL);

        // Assert
        assertEquals(Direction.RTL, label.getDirection());
    }

    @Test
    public void getText_with_HTML() {
        // Arrange
        Label label = new Label("<a href='#'>link</a><br/>test&nbsp;test");

        String text = label.getText();

        // Assert
        assertEquals("<a href='#'>link</a><br/>test&nbsp;test", text);
    }

    @Test
    public void id() {
        // Arrange
        Label label = new Label();

        // Act
        label.getElement().setId("myId");

        // Assert
        assertEquals("myId", label.getElement().getAttribute("id"));
    }

    @Test
    public void text() {
        // Arrange
        Label label = new Label("foo");
        // Pre-Assert
        assertEquals("foo", label.getText());

        // Act
        label.setText("text");

        // Assert
        assertEquals("text", label.getText());
    }

    @Test
    public void title() {
        // Arrange
        Label label = new Label();

        // Act
        label.setTitle("title");

        // Assert
        assertEquals("title", label.getTitle());
    }

    @Test
    public void visible() {
        // Arrange
        Label label = new Label();
        // Pre-Assert
        assertEquals(true, label.isVisible());

        // Act
        label.setVisible(false);

        // Assert
        assertFalse(label.isVisible());
    }

    @Test
    public void wordWrap() {
        // Arrange
        Label label = new Label();
        // Pre-Assert
        assertFalse(label.getWordWrap());

        // Act
        label.setWordWrap(true);

        // Assert
        assertTrue(label.getWordWrap());
    }

    @Test
    public void wrap() {
        // Arrange
        // Element.setInnerHTML & Document.get().getElementById are supposed to
        // work
        Document.get().getBody().setInnerHTML("<div id=\"anId\"></div>");
        DivElement div = Document.get().getElementById("anId").cast();

        // Act
        Label label = Label.wrap(div);
        label.setText("My wrapped label !");

        // Assert
        assertEquals(div, label.getElement());
        assertEquals("My wrapped label !", div.getInnerText());
    }

}
