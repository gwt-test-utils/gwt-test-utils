package com.googlecode.gwt.test;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PushButton;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PushButtonTest extends GwtTestTest {

    private boolean clicked;

    @Test
    public void click() {
        // Arrange
        clicked = false;

        final PushButton b = new PushButton("Up", "Down");

        b.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                clicked = true;
            }
        });

        // Pre-Assert
        assertEquals("Up", b.getText());

        // Act
        Browser.click(b);

        // Assert
        assertTrue("PushButton onClick was not triggered", clicked);
        assertEquals("Up", b.getText());
    }

}
