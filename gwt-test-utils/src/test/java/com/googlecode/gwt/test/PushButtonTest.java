package com.googlecode.gwt.test;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PushButton;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PushButtonTest extends GwtTestTest {

    private boolean clicked;

    @Test
    public void click() {
        // Given
        clicked = false;

        final PushButton b = new PushButton("Up", "Down");

        b.addClickHandler(event -> clicked = true);

        // Preconditions
        assertThat(b.getText()).isEqualTo("Up");

        // When
        Browser.click(b);

        // Then
        assertThat(clicked).as("PushButton onClick was not triggered").isTrue();
        assertThat(b.getText()).isEqualTo("Up");
    }

}
