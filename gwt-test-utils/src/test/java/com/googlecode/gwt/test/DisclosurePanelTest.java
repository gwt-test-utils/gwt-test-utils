package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.DisclosurePanel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DisclosurePanelTest extends GwtTestTest {

    @Test
    public void style() {
        // Arrange
        DisclosurePanel dp = new DisclosurePanel();
        // Pre-Assert
        assertEquals("gwt-DisclosurePanel gwt-DisclosurePanel-closed", dp.getStyleName());

        // Act
        dp.setOpen(true);

        // Assert
        assertEquals("gwt-DisclosurePanel gwt-DisclosurePanel-open", dp.getStyleName());
    }

    @Test
    public void title() {
        // Arrange
        DisclosurePanel dp = new DisclosurePanel();

        // Act
        dp.setTitle("title");

        // Assert
        assertEquals("title", dp.getTitle());
    }

    @Test
    public void visible() {
        // Arrange
        DisclosurePanel dp = new DisclosurePanel();
        // Pre-Assert
        assertEquals(true, dp.isVisible());

        // Act
        dp.setVisible(false);

        // Assert
        assertEquals(false, dp.isVisible());
    }

}
