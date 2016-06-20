package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RootPanelTest extends GwtTestTest {

    @Test
    public void add() {
        // Given
        Label label = new Label();
        assertThat(label.isAttached()).isFalse();

        // When
        RootPanel.get().add(label);

        // Then
        assertThat(RootPanel.get().getWidget(0)).isEqualTo(label);
        assertThat(label.isAttached()).isTrue();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void init() {
        // Preconditions
        assertThat(RootPanel.get().getWidgetCount()).isEqualTo(0);

        // When
        RootPanel.get().getWidget(0);
    }

}
