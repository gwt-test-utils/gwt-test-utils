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
        panel.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                test = true;
            }
        });

        // When
        Browser.click(panel);

        // Then
        assertThat(test).isTrue();
    }

    @Test
    public void click_WithChild() {
        // Given
        panel.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                test = true;
            }
        });

        child.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                child.setText("clicked");

            }
        });

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
        panel.addTouchCancelHandler(new TouchCancelHandler() {

            public void onTouchCancel(TouchCancelEvent event) {
                test = true;
            }
        });

        // When
        Browser.dispatchEvent(panel, EventBuilder.create(Event.ONTOUCHCANCEL).build());

        // Then
        assertThat(test).isTrue();
    }

    @Test
    public void touchEnd() {
        // Given
        panel.addTouchEndHandler(new TouchEndHandler() {

            public void onTouchEnd(TouchEndEvent event) {
                test = true;
            }
        });

        // When
        Browser.dispatchEvent(panel, EventBuilder.create(Event.ONTOUCHEND).build());

        // Then
        assertThat(test).isTrue();
    }

    @Test
    public void touchMove() {
        // Given
        panel.addTouchMoveHandler(new TouchMoveHandler() {

            public void onTouchMove(TouchMoveEvent event) {
                test = true;

            }
        });

        // When
        Browser.dispatchEvent(panel, EventBuilder.create(Event.ONTOUCHMOVE).build());

        // Then
        assertThat(test).isTrue();
    }

    @Test
    public void touchStart() {
        // Given
        panel.addTouchStartHandler(new TouchStartHandler() {

            public void onTouchStart(TouchStartEvent event) {
                test = true;

            }
        });

        // When
        Browser.dispatchEvent(panel, EventBuilder.create(Event.ONTOUCHSTART).build());

        // Then
        assertThat(test).isTrue();
    }

}
