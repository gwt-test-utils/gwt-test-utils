package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimplePanelTest extends GwtTestTest {

    @Test
    public void add() {
        // Given
        SimplePanel panel = new SimplePanel();
        RootPanel.get().add(panel);
        assertThat(panel.isAttached()).isTrue();
        assertThat(panel.getWidget()).isNull();
        Button b1 = new Button();
        assertThat(b1.isAttached()).isFalse();
        assertThat(b1.getParent()).isNull();

        // When
        panel.add(b1);

        // Then
        assertThat(b1.isAttached()).isTrue();
        assertThat(b1.getParent()).isEqualTo(panel);
        assertThat(panel.getWidget()).isEqualTo(b1);
    }

    @Test
    public void remove() {
        // Given
        SimplePanel panel = new SimplePanel();
        Button b = new Button();
        panel.add(b);

        // When & Then
        assertThat(panel.remove(b)).isTrue();
    }

    @Test
    public void title() {
        // Given
        SimplePanel sp = new SimplePanel();
        // Preconditions
        assertThat(sp.getTitle()).isEqualTo("");

        // When
        sp.setTitle("title");

        // Then
        assertThat(sp.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Given
        SimplePanel sp = new SimplePanel();
        // Preconditions
        assertThat(sp.isVisible()).isEqualTo(true);

        // When
        sp.setVisible(false);

        // Then
        assertThat(sp.isVisible()).isEqualTo(false);
    }

}
