package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.PasswordTextBox;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PasswordTextBoxTest extends GwtTestTest {

    @Test
    public void name() {
        // Arrange
        PasswordTextBox ptb = new PasswordTextBox();
        // Pre-Assert
        assertEquals("", ptb.getName());

        // Act
        ptb.setName("name");

        // Assert
        assertEquals("name", ptb.getName());
    }

    @Test
    public void text() {
        // Arrange
        PasswordTextBox ptb = new PasswordTextBox();
        // Pre-Assert
        assertEquals("", ptb.getText());

        // Act
        ptb.setText("text");

        // Assert
        assertEquals("text", ptb.getText());
    }

    @Test
    public void title() {
        // Arrange
        PasswordTextBox ptb = new PasswordTextBox();
        // Pre-Assert
        assertEquals("", ptb.getTitle());

        // Act
        ptb.setTitle("title");

        // Assert
        assertEquals("title", ptb.getTitle());
    }

    @Test
    public void visible() {
        // Arrange
        PasswordTextBox ptb = new PasswordTextBox();
        // Pre-Assert
        assertEquals(true, ptb.isVisible());

        // Act
        ptb.setVisible(false);

        // Assert
        assertEquals(false, ptb.isVisible());
    }

}
