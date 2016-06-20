package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.PopupPanel;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class PopupPanelTest extends GwtTestTest {

    @Test
    public void autoHideEnabled() {
        // Arrange
        PopupPanel popupPanel = new PopupPanel(true);
        // Pre-Assert
        assertTrue(popupPanel.isAutoHideEnabled());

        // Act
        popupPanel.setAutoHideEnabled(false);

        // Assert
        assertFalse(popupPanel.isAutoHideEnabled());
    }

    @Test
    public void center() {
        // Arrange
        PopupPanel popup = new PopupPanel();
        popup.setAnimationEnabled(true);

        // Act
        popup.center();

        // Assert
        assertEquals(0, popup.getOffsetHeight());
        assertEquals(0, popup.getOffsetWidth());
    }

    @Test
    public void show() {
        // Arrange
        PopupPanel popup = new PopupPanel();
        // Pre-Assert
        assertThat(popup.isVisible()).isTrue();
        assertThat(popup.isShowing()).isFalse();

        // Act
        popup.show();

        // Assert
        assertTrue(popup.isShowing());
    }

    @Test
    public void showGlass() {
        // Arrange
        PopupPanel popup = new PopupPanel();
        popup.setGlassEnabled(true);
        // Pre-Assert
        assertThat(popup.isShowing()).isFalse();

        // Act
        popup.show();

        // Assert
        assertThat(popup.isShowing()).isTrue();
    }

    @Test
    public void visible() {
        // Arrange
        PopupPanel popup = new PopupPanel();
        // Pre-Assert
        assertThat(popup.isVisible()).isTrue();

        // Act
        popup.setVisible(false);

        // Assert
        assertThat(popup.isVisible()).isFalse();
        assertEquals("hidden", popup.getElement().getStyle().getProperty("visibility"));
    }

}
