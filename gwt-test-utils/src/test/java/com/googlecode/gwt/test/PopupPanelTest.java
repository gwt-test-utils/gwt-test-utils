package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.PopupPanel;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PopupPanelTest extends GwtTestTest {

    @Test
    public void autoHideEnabled() {
        // Given
        PopupPanel popupPanel = new PopupPanel(true);
        // Preconditions
        assertThat(popupPanel.isAutoHideEnabled()).isTrue();

        // When
        popupPanel.setAutoHideEnabled(false);

        // Then
        assertThat(popupPanel.isAutoHideEnabled()).isFalse();
    }

    @Test
    public void center() {
        // Given
        PopupPanel popup = new PopupPanel();
        popup.setAnimationEnabled(true);

        // When
        popup.center();

        // Then
        assertThat(popup.getOffsetHeight()).isEqualTo(0);
        assertThat(popup.getOffsetWidth()).isEqualTo(0);
    }

    @Test
    public void show() {
        // Given
        PopupPanel popup = new PopupPanel();
        // Preconditions
        assertThat(popup.isVisible()).isTrue();
        assertThat(popup.isShowing()).isFalse();

        // When
        popup.show();

        // Then
        assertThat(popup.isShowing()).isTrue();
    }

    @Test
    public void showGlass() {
        // Given
        PopupPanel popup = new PopupPanel();
        popup.setGlassEnabled(true);
        // Preconditions
        assertThat(popup.isShowing()).isFalse();

        // When
        popup.show();

        // Then
        assertThat(popup.isShowing()).isTrue();
    }

    @Test
    public void visible() {
        // Given
        PopupPanel popup = new PopupPanel();
        // Preconditions
        assertThat(popup.isVisible()).isTrue();

        // When
        popup.setVisible(false);

        // Then
        assertThat(popup.isVisible()).isFalse();
        assertThat(popup.getElement().getStyle().getProperty("visibility")).isEqualTo("hidden");
    }

}
