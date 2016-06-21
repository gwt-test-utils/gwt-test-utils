package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ComplexPanelTest extends GwtTestTest {

    @Test
    public void add() {
        // Given
        ComplexPanel panel = new FlowPanel();
        RootPanel.get().add(panel);
        assertThat(panel.isAttached()).isTrue();
        assertThat(panel.getWidgetCount()).isEqualTo(0);
        Button b1 = new Button();
        assertThat(b1.isAttached()).isFalse();
        assertThat(b1.getParent()).isNull();

        // When
        panel.add(b1);

        // Then
        assertThat(b1.isAttached()).isTrue();
        assertThat(b1.getParent()).isEqualTo(panel);
        assertThat(panel.getWidgetCount()).isEqualTo(1);
        assertThat(panel.getWidget(0)).isEqualTo(b1);
        assertThat(panel.getWidgetIndex(b1)).isEqualTo(0);
    }

    @Test
    public void count() {
        // Given
        ComplexPanel panel = new FlowPanel();
        panel.add(new Button());
        panel.add(new Button());

        // When & Then
        assertThat(panel.getWidgetCount()).isEqualTo(2);
    }

    @Test
    public void remove() {
        // Given
        ComplexPanel panel = new FlowPanel();
        Button b = new Button();
        panel.add(b);
        // Preconditions
        assertThat(panel.getWidgetCount()).isEqualTo(1);
        assertThat(b.getParent()).isEqualTo(panel);

        // When
        panel.remove(b);

        // Then
        assertThat(panel.getWidgetCount()).isEqualTo(0);
        assertThat(b.getParent()).isNull();
    }

    @Test
    public void remove_ByIndex() {
        // Given
        ComplexPanel panel = new FlowPanel();
        Button b0 = new Button();
        panel.add(b0);
        Button b1 = new Button();
        panel.add(b1);

        // When
        panel.remove(1);

        // Then
        assertThat(panel.getWidgetCount()).isEqualTo(1);
        assertThat(panel.getWidget(0)).isEqualTo(b0);
        assertThat(b0.getParent()).isEqualTo(panel);
        assertThat(b1.getParent()).isNull();
    }

    @Test
    public void title() {
        // Given
        ComplexPanel panel = new FlowPanel();

        // When
        panel.setTitle("title");

        // Then
        assertThat(panel.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Given
        ComplexPanel panel = new FlowPanel();
        // Preconditions
        assertThat(panel.isVisible()).isEqualTo(true);

        // When
        panel.setVisible(false);

        // Then
        assertThat(panel.isVisible()).isEqualTo(false);
    }

}
