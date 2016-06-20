package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StackPanelTest extends GwtTestTest {

    private int index = -1;

    @Test
    public void click() {
        // Given
        index = -1;
        StackPanel panel = new StackPanel() {

            @Override
            public void showStack(int index) {
                StackPanelTest.this.index = index;
            }

            ;
        };

        panel.add(new Anchor());
        panel.add(new Anchor());

        // When
        Browser.click(panel, 1);

        // Then
        assertThat(index).isEqualTo(1);
    }

    @Test
    public void stackPanel() {
        // Given
        StackPanel panel = new StackPanel();

        // When
        panel.add(new Label("Foo"), "foo");
        Label label = new Label("Bar");
        panel.add(label, "bar");
        panel.add(new Label("Baz"), "baz");

        // Then
        assertThat(panel.getWidgetCount()).isEqualTo(3);
        assertThat(panel.getWidget(1)).isEqualTo(label);
        assertThat(panel.getWidgetIndex(label)).isEqualTo(1);
    }

    @Test
    public void title() {
        // Given
        StackPanel sp = new StackPanel();
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
        StackPanel sp = new StackPanel();
        // Preconditions
        assertThat(sp.isVisible()).isEqualTo(true);

        // When
        sp.setVisible(false);

        // Then
        assertThat(sp.isVisible()).isEqualTo(false);
    }

}
