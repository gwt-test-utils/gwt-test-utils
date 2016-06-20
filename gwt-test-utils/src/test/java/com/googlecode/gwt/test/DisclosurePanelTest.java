package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.DisclosurePanel;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DisclosurePanelTest extends GwtTestTest {

    @Test
    public void style() {
        // Given
        DisclosurePanel dp = new DisclosurePanel();
        // Preconditions
        assertThat(dp.getStyleName()).isEqualTo("gwt-DisclosurePanel gwt-DisclosurePanel-closed");

        // When
        dp.setOpen(true);

        // Then
        assertThat(dp.getStyleName()).isEqualTo("gwt-DisclosurePanel gwt-DisclosurePanel-open");
    }

    @Test
    public void title() {
        // Given
        DisclosurePanel dp = new DisclosurePanel();

        // When
        dp.setTitle("title");

        // Then
        assertThat(dp.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Given
        DisclosurePanel dp = new DisclosurePanel();
        // Preconditions
        assertThat(dp.isVisible()).isEqualTo(true);

        // When
        dp.setVisible(false);

        // Then
        assertThat(dp.isVisible()).isEqualTo(false);
    }

}
