package com.googlecode.gwt.test;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.googlecode.gwt.test.utils.events.Browser;
import com.googlecode.gwt.test.utils.events.EventBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        // Arrange
        panel = new FocusPanel();
        panel.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                test = true;
            }
        });

        // Act
        Browser.click(panel);

        // Assert
        Assert.assertTrue(test);
    }

    @Test
    public void click_WithChild() {
        // Arrange
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

        // Act
        Browser.click(panel);

        // Assert
        Assert.assertTrue(test);
        // click event should not be dispatched to the child widget
        Assert.assertEquals("focus panel's child widget", child.getText());
    }

    @Test
    public void touchCancel() {
        // Arrange
        panel.addTouchCancelHandler(new TouchCancelHandler() {

            public void onTouchCancel(TouchCancelEvent event) {
                test = true;
            }
        });

        // Act
        Browser.dispatchEvent(panel, EventBuilder.create(Event.ONTOUCHCANCEL).build());

        // Assert
        Assert.assertTrue(test);
    }

    @Test
    public void touchEnd() {
        // Arrange
        panel.addTouchEndHandler(new TouchEndHandler() {

            public void onTouchEnd(TouchEndEvent event) {
                test = true;
            }
        });

        // Act
        Browser.dispatchEvent(panel, EventBuilder.create(Event.ONTOUCHEND).build());

        // Assert
        Assert.assertTrue(test);
    }

    @Test
    public void touchMove() {
        // Arrange
        panel.addTouchMoveHandler(new TouchMoveHandler() {

            public void onTouchMove(TouchMoveEvent event) {
                test = true;

            }
        });

        // Act
        Browser.dispatchEvent(panel, EventBuilder.create(Event.ONTOUCHMOVE).build());

        // Assert
        Assert.assertTrue(test);
    }

    @Test
    public void touchStart() {
        // Arrange
        panel.addTouchStartHandler(new TouchStartHandler() {

            public void onTouchStart(TouchStartEvent event) {
                test = true;

            }
        });

        // Act
        Browser.dispatchEvent(panel, EventBuilder.create(Event.ONTOUCHSTART).build());

        // Assert
        Assert.assertTrue(test);
    }

}
