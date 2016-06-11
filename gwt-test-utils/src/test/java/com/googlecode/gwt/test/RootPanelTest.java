package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import org.junit.Test;

import static org.junit.Assert.*;

public class RootPanelTest extends GwtTestTest {

    @Test
    public void add() {
        // Arrange
        Label label = new Label();
        assertFalse(label.isAttached());

        // Act
        RootPanel.get().add(label);

        // Assert
        assertEquals(label, RootPanel.get().getWidget(0));
        assertTrue(label.isAttached());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void init() {
        // Pre-Assert
        assertEquals(0, RootPanel.get().getWidgetCount());

        // Act
        RootPanel.get().getWidget(0);
    }

}
