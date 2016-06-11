package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComplexPanelTest extends GwtTestTest {

    @Test
    public void add() {
        // Arrange
        ComplexPanel panel = new FlowPanel();
        RootPanel.get().add(panel);
        assertTrue(panel.isAttached());
        assertEquals(0, panel.getWidgetCount());
        Button b1 = new Button();
        assertFalse(b1.isAttached());
        assertNull(b1.getParent());

        // Act
        panel.add(b1);

        // Assert
        assertTrue(b1.isAttached());
        assertEquals(panel, b1.getParent());
        assertEquals(1, panel.getWidgetCount());
        assertEquals(b1, panel.getWidget(0));
        assertEquals(0, panel.getWidgetIndex(b1));
    }

    @Test
    public void count() {
        // Arrange
        ComplexPanel panel = new FlowPanel();
        panel.add(new Button());
        panel.add(new Button());

        // Act & Assert
        assertEquals(2, panel.getWidgetCount());
    }

    @Test
    public void remove() {
        // Arrange
        ComplexPanel panel = new FlowPanel();
        Button b = new Button();
        panel.add(b);
        // Pre-Assert
        assertEquals(1, panel.getWidgetCount());
        assertEquals(panel, b.getParent());

        // Act
        panel.remove(b);

        // Assert
        assertEquals(0, panel.getWidgetCount());
        assertNull(b.getParent());
    }

    @Test
    public void remove_ByIndex() {
        // Arrange
        ComplexPanel panel = new FlowPanel();
        Button b0 = new Button();
        panel.add(b0);
        Button b1 = new Button();
        panel.add(b1);

        // Act
        panel.remove(1);

        // Assert
        assertEquals(1, panel.getWidgetCount());
        assertEquals(b0, panel.getWidget(0));
        assertEquals(panel, b0.getParent());
        assertNull(b1.getParent());
    }

    @Test
    public void title() {
        // Arrange
        ComplexPanel panel = new FlowPanel();

        // Act
        panel.setTitle("title");

        // Assert
        assertEquals("title", panel.getTitle());
    }

    @Test
    public void visible() {
        // Arrange
        ComplexPanel panel = new FlowPanel();
        // Pre-Assert
        assertEquals(true, panel.isVisible());

        // Act
        panel.setVisible(false);

        // Assert
        assertEquals(false, panel.isVisible());
    }

}
