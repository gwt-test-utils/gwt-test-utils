package com.googlecode.gwt.test;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ToggleButtonTest extends GwtTestTest {

    private boolean clicked;

    @Test
    public void click() {
        // Given
        final ToggleButton toggleButton = new ToggleButton("Up", "Down");

        // needs to be attached
        RootPanel.get().add(toggleButton);

        clicked = false;

        toggleButton.addClickHandler(event -> clicked = true);

        // Preconditions
        assertThat(toggleButton.isDown()).as("ToggleButton should not be toggled by default").isFalse();
        assertThat(toggleButton.getText()).isEqualTo("Up");

        // When
        Browser.click(toggleButton);

        // Then
        assertThat(clicked).as("ToggleButton onClick was not triggered").isTrue();
        assertThat(toggleButton.isDown()).as("ToggleButton should be toggled after being clicked once").isTrue();
        assertThat(toggleButton.getText()).isEqualTo("Down");

        // When 2
        Browser.click(toggleButton);
        assertThat(toggleButton.isDown()).as("ToggleButton should not be toggled after being clicked twice").isFalse();
        assertThat(toggleButton.getText()).isEqualTo("Up");
    }

}
