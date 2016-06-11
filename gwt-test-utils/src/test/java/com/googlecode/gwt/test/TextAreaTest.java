package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.TextArea;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextAreaTest extends GwtTestTest {

    @Test
    public void getCursorPos() {
        // Arrange
        TextArea t = new TextArea();
        t.setText("myText");
        GwtReflectionUtils.setPrivateFieldValue(t, "attached", true);

        // Act
        t.setCursorPos(2);

        // Assert
        assertEquals(2, t.getCursorPos());
    }

    @Test
    public void getSelectionLength() {
        // Arrange
        TextArea t = new TextArea();
        t.setText("myText");
        GwtReflectionUtils.setPrivateFieldValue(t, "attached", true);

        // Act
        t.setSelectionRange(1, 3);

        // Assert
        assertEquals(3, t.getSelectionLength());
    }

    @Test
    public void name() {
        // Arrange
        TextArea t = new TextArea();
        // Pre-Assert
        assertEquals("", t.getName());

        // Act
        t.setName("name");

        // Assert
        assertEquals("name", t.getName());
    }

    @Test
    public void text() {
        // Arrange
        TextArea t = new TextArea();
        // Pre-Assert
        assertEquals("", t.getText());

        // Act
        t.setText("text");

        // Assert
        assertEquals("text", t.getText());
    }

    @Test
    public void title() {
        // Arrange
        TextArea t = new TextArea();
        // Pre-Assert
        assertEquals("", t.getTitle());

        // Act
        t.setTitle("title");

        // Assert
        assertEquals("title", t.getTitle());
    }

    @Test
    public void visible() {
        // Arrange
        TextArea t = new TextArea();
        // Pre-Assert
        assertEquals(true, t.isVisible());

        // Act
        t.setVisible(false);

        // Assert
        assertEquals(false, t.isVisible());
    }

    @Test
    public void visibleLines() {
        // Arrange
        TextArea t = new TextArea();

        // Act
        t.setVisibleLines(10);

        // Assert
        assertEquals(10, t.getVisibleLines());
    }

}
