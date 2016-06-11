package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplePanelTest extends GwtTestTest {

    @Test
    public void add() {
        // Arrange
        SimplePanel panel = new SimplePanel();
        RootPanel.get().add(panel);
        assertTrue(panel.isAttached());
        assertNull(panel.getWidget());
        Button b1 = new Button();
        assertFalse(b1.isAttached());
        assertNull(b1.getParent());

        // Act
        panel.add(b1);

        // Assert
        assertTrue(b1.isAttached());
        assertEquals(panel, b1.getParent());
        assertEquals(b1, panel.getWidget());
    }

    @Test
    public void remove() {
        // Arrange
        SimplePanel panel = new SimplePanel();
        Button b = new Button();
        panel.add(b);

        // Act & Assert
        assertTrue(panel.remove(b));
    }

    @Test
    public void title() {
        // Arrange
        SimplePanel sp = new SimplePanel();
        // Pre-Assert
        assertEquals("", sp.getTitle());

        // Act
        sp.setTitle("title");

        // Assert
        assertEquals("title", sp.getTitle());
    }

    @Test
    public void visible() {
        // Arrange
        SimplePanel sp = new SimplePanel();
        // Pre-Assert
        assertEquals(true, sp.isVisible());

        // Act
        sp.setVisible(false);

        // Assert
        assertEquals(false, sp.isVisible());
    }

}
