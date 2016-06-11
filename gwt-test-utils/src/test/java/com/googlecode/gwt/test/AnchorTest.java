package com.googlecode.gwt.test;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.user.client.ui.Anchor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnchorTest extends GwtTestTest {

    @Test
    public void absoluteLeft() {
        // Arrange
        Anchor a = new Anchor();

        // Assert
        assertEquals(0, a.getAbsoluteLeft());
    }

    @Test
    public void absoluteTop() {
        // Arrange
        Anchor a = new Anchor();

        // Assert
        assertEquals(0, a.getAbsoluteTop());
    }

    @Test
    public void href() {
        // Arrange
        Anchor a = new Anchor("toto", "href");
        // Pre-Assert
        assertEquals("toto", a.getText());
        assertEquals("href", a.getHref());

        // Act
        a.setHref("new");

        // Assert
        assertEquals("new", a.getHref());
    }

    @Test
    public void html() {
        // Arrange
        Anchor a = new Anchor("<h1>foo</h1>", true);
        assertEquals("<h1>foo</h1>", a.getHTML());

        // Act
        a.setHTML("<h1>test</h1>");

        // Assert
        assertEquals("<h1>test</h1>", a.getHTML());
        assertEquals(1, a.getElement().getChildCount());
        HeadingElement h1 = a.getElement().getChild(0).cast();
        assertEquals("H1", h1.getTagName());
        assertEquals("test", h1.getInnerText());
    }

    @Test
    public void name() {
        // Arrange
        Anchor a = new Anchor();

        // Act
        a.setName("toto");

        // Assert
        assertEquals("toto", a.getName());
    }

    @Test
    public void tabIndex() {
        // Arrange
        Anchor a = new Anchor();

        // Act
        a.setTabIndex(1);

        // Assert
        assertEquals(1, a.getTabIndex());
    }

    @Test
    public void tagName() {
        // Arrange
        Anchor a = new Anchor();

        // Assert
        assertEquals("a", a.getElement().getTagName());
    }

    @Test
    public void target() {
        // Arrange
        Anchor a = new Anchor();

        // Act
        a.setTarget("myTarget");

        // Assert
        assertEquals("myTarget", a.getTarget());
    }

    @Test
    public void text() {
        // Arrange
        Anchor a = new Anchor("foo");
        assertEquals("foo", a.getText());

        // Act
        a.setText("toto");

        // Assert
        assertEquals("toto", a.getText());
    }

    @Test
    public void title() {
        // Arrange
        Anchor a = new Anchor();

        // Act
        a.setTitle("title");

        // Assert
        assertEquals("title", a.getTitle());
    }

    @Test
    public void visible() {
        // Arrange
        Anchor a = new Anchor();
        assertEquals(true, a.isVisible());

        // Act
        a.setVisible(false);

        // Assert
        assertEquals(false, a.isVisible());
    }

}
