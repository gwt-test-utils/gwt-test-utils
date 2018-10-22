package com.googlecode.gwt.test;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.gwt.test.utils.events.Browser;
import com.googlecode.gwt.test.utils.events.EventBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FocusPanelTest extends GwtTestTest {

    private Label child;
    private FocusPanel panel;
    private boolean test;

    @Before
    public void beforeFocusPanelTest() {
        child = new Label("focus panel's child widget");
        panel = new FocusPanel(child);
        test = false;
    }

    @Test
    public void click_EmptyPanel() {
        // Given
        panel = new FocusPanel();
        panel.addClickHandler(event -> test = true);

        // When
        Browser.click(panel);

        // Then
        assertThat(test).isTrue();
    }

    @Test
    public void click_WithChild() {
        // Given
        panel.addClickHandler(event -> test = true);

        child.addClickHandler(event -> child.setText("clicked"));

        // When
        Browser.click(panel);

        // Then
        assertThat(test).isTrue();
        // click event should not be dispatched to the child widget
        assertThat(child.getText()).isEqualTo("focus panel's child widget");
    }

    @Test
    public void touchCancel() {
        // Given
        panel.addTouchCancelHandler(event -> test = true);

        // When
        Browser.dispatchEvent(panel, EventBuilder.create(Event.ONTOUCHCANCEL).build());

        // Then
        assertThat(test).isTrue();
    }

    @Test
    public void touchEnd() {
        // Given
        panel.addTouchEndHandler(event -> test = true);

        // When
        Browser.dispatchEvent(panel, EventBuilder.create(Event.ONTOUCHEND).build());

        // Then
        assertThat(test).isTrue();
    }

    @Test
    public void touchMove() {
        // Given
        panel.addTouchMoveHandler(event -> test = true);

        // When
        Browser.dispatchEvent(panel, EventBuilder.create(Event.ONTOUCHMOVE).build());

        // Then
        assertThat(test).isTrue();
    }

    @Test
    public void touchStart() {
        // Given
        panel.addTouchStartHandler(event -> test = true);

        // When
        Browser.dispatchEvent(panel, EventBuilder.create(Event.ONTOUCHSTART).build());

        // Then
        assertThat(test).isTrue();
    }

}
