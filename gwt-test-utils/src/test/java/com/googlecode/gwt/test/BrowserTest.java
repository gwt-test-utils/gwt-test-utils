package com.googlecode.gwt.test;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.googlecode.gwt.test.utils.events.Browser;
import com.googlecode.gwt.test.utils.events.EventBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.gwt.test.assertions.GwtAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;
import static org.junit.Assert.assertTrue;

public class BrowserTest extends GwtTestTest {

    private Button b;
    private Cell clickedCell;
    private int keyDownCount;
    private int keyUpCount;
    private boolean onBlurTriggered;
    private boolean onChangeTriggered;
    private boolean onClickTriggered;
    private boolean onMouseDownTriggered;
    private String onErrorMessage;
    private FocusPanel panel;
    private boolean panelTested;
    private boolean tested;

    @Test
    public void addText_delete_SelectedText() {
        // Given
        TextBox textBox = new TextBox();
        // must be attached to use "addText"
        RootPanel.get().add(textBox);
        textBox.setText("toto titi");
        // select "titi"
        textBox.setSelectionRange(5, 4);
        // Preconditions
        assertThat("titi").isEqualTo(textBox.getSelectedText());

        // When
        Browser.addText(textBox, "tutu");

        // Then
        assertThat(textBox.getText()).isEqualTo("toto tutu");
        assertThat(textBox.getCursorPos()).isEqualTo(9);
    }

    @Test
    public void addText_DoesNot_Fire_ValueChangeEvent() {
        // Given
        TextBox textBox = new TextBox();
        // must be attached to use "addText"
        RootPanel.get().add(textBox);

        textBox.addValueChangeHandler(event -> fail("ValueChangeEvent should not be fired with Browser.addText(..)"));

        // When
        Browser.addText(textBox, "toto");

        // Then
        assertThat(textBox.getText()).isEqualTo("toto");
    }

    @Test
    public void addText_insertAtCursorPos() {
        // Given
        TextBox textBox = new TextBox();
        // must be attached to use "addText"
        RootPanel.get().add(textBox);

        textBox.setText("toto");
        // Preconditions
        assertThat(textBox.getCursorPos()).isEqualTo(4);

        // change the position
        textBox.setCursorPos(2);
        assertThat(textBox.getCursorPos()).isEqualTo(2);

        // When
        Browser.addText(textBox, "titi");

        // Then
        assertThat(textBox.getText()).isEqualTo("totitito");
        assertThat(textBox.getCursorPos()).isEqualTo(6);
    }

    @Before
    public void beforeBrowserTest() {
        panel = new FocusPanel();
        RootPanel.get().add(panel);
        b = new Button();
        panel.add(b);

        tested = false;
        panelTested = false;
        onChangeTriggered = false;
        onBlurTriggered = false;
        keyDownCount = 0;
        keyUpCount = 0;
    }

    @Test
    public void blur() {
        // Given
        b.addBlurHandler(event -> {
            tested = !tested;

            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(b.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isNull();
        });

        // When
        Browser.blur(b);

        // Then
        assertThat(tested).as("onBlur event was not triggered").isTrue();
    }

    @Test
    public void click_ComplexPanel() {
        // Given
        final Anchor a0 = new Anchor();
        a0.setText("a0");
        final Anchor a1 = new Anchor();
        a1.setText("a1");
        final StackPanel panel = new StackPanel() {

            @Override
            public void onBrowserEvent(com.google.gwt.user.client.Event event) {
                super.onBrowserEvent(event);

                if (DOM.eventGetType(event) == Event.ONCLICK) {
                    tested = !tested;
                    assertThat(event.getRelatedEventTarget()).isNull();
                    assertThat(event.getEventTarget()).isEqualTo(a1.getElement());
                }
            }
        };

        panel.add(a0);
        panel.insert(a1, 1);
        panel.showStack(1);

        // Pre-Assertions
        assertThat(a1).isVisible();
        assertThat(a0).isNotVisible();


        // When
        Browser.click(panel, 1);

        // Then
        assertThat(tested).isTrue();
        assertThat(panel.getSelectedIndex()).isEqualTo(1);
    }

    @Test
    public void click_firesNativePreviewHandler() {
        // Given
        Button b = new Button();
        FocusPanel focusPanel = new FocusPanel();
        focusPanel.add(b);
        RootPanel.get().add(focusPanel);

        final StringBuilder sb = new StringBuilder();

        Event.addNativePreviewHandler(event -> {
            Event nativeEvent = Event.as(event.getNativeEvent());
            int eventType = DOM.eventGetType(nativeEvent);
            if (eventType == Event.ONCLICK) {
                sb.append("click!");
            }
        });

        // When
        Browser.click(b);

        // Then
        assertThat(sb.toString()).isEqualTo("click!");
    }

    @Test
    public void click_Grid() {
        // Given
        final Grid g = new Grid(2, 2);
        final Anchor a = new Anchor();
        g.setWidget(1, 1, a);

        g.addClickHandler(event -> {
            clickedCell = g.getCellForEvent(event);

            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(a.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isNull();
        });

        // When
        Browser.click(g, 1, 1);

        // Then
        assertThat(clickedCell.getRowIndex()).isEqualTo(1);
        assertThat(clickedCell.getCellIndex()).isEqualTo(1);
    }

    @Test
    public void click_propagation() {
        // Given
        b.addClickHandler(event -> {
            tested = !tested;

            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(b.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isNull();
        });

        panel.addClickHandler(event -> {
            panelTested = !panelTested;
            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(b.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isNull();

        });

        // When
        Browser.click(b);

        // Then
        assertThat(tested).as("onClick event was not triggered").isTrue();
        assertThat(panelTested).as("onClick event was not triggered on target widget parents").isTrue();
    }

    @Test
    public void click_stopPropagation() {
        // Given
        b.addClickHandler(event -> {
            tested = !tested;
            // stop event propagation : parent clickHandler should not be
            // triggered
            event.stopPropagation();

            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(b.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isNull();
        });

        panel.addClickHandler(event -> fail("parent click handler should not be triggered when event.stopPropagation() is called"));

        // When
        Browser.click(b);

        // Then
        assertThat(tested).as("onClick event was not triggered").isTrue();
    }

    @Test
    public void click_SuggestBox() {
        // Given
        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        oracle.add("suggestion 1");
        oracle.add("suggestion 2");
        SuggestBox box = new SuggestBox(oracle);

        // When
        Browser.fillText(box, "sug");
        Browser.click(box, 1);

        // Then
        assertThat(box.getText()).isEqualTo("suggestion 2");
    }

    @Test
    public void click_WithPosition() {
        // Given
        b.addClickHandler(event -> {
            tested = !tested;

            // Then
            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(b.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isNull();

            // check positions
            assertThat(event.getX()).isEqualTo(1);
            assertThat(event.getY()).isEqualTo(2);
            assertThat(event.getClientX()).isEqualTo(1);
            assertThat(event.getClientY()).isEqualTo(2);
            assertThat(event.getScreenX()).isEqualTo(3);
            assertThat(event.getScreenY()).isEqualTo(4);
        });

        Event clickEvent = EventBuilder.create(Event.ONCLICK).setMouseX(1).setMouseY(2).setMouseScreenX(
                3).setMouseScreenY(4).build();

        // When
        Browser.dispatchEvent(b, clickEvent);

        // Then
        assertThat(tested).as("onClick event was not triggered").isTrue();
    }

    @Test
    public void click_eventBubbling() {
        // Given
        final Boolean[] clicked = new Boolean[2];
        clicked[0] = false;
        clicked[1] = false;
        FocusPanel panel0 = new FocusPanel();
        FocusPanel panel1 = new FocusPanel();
        panel0.add(panel1);
        panel0.addClickHandler(event -> clicked[0] = true);
        panel1.addClickHandler(event -> clicked[1] = true);

        // When
        Browser.click(panel1);

        // Then
        assertThat(clicked[0]).isTrue();
        assertThat(clicked[1]).isTrue();
    }

    @Test
    public void disabledButton_blur() {
        Button b = new Button();
        b.setEnabled(false);
        setBrowserErrorHandler(errorMessage -> onErrorMessage = errorMessage);
        b.addClickHandler(event -> onClickTriggered = true);
        b.addMouseDownHandler(event -> onMouseDownTriggered = true);

        Browser.click(b);
        assertThat(onClickTriggered).isFalse();
        assertThat(onMouseDownTriggered).isFalse();
    }

    @Test
    public void emptyText_LongPressFalse() {
        // Given
        String initialText = "1234";

        final TextBox tb = new TextBox();
        tb.setText(initialText);

        tb.addChangeHandler(event -> onChangeTriggered = true);

        tb.addBlurHandler(event -> onBlurTriggered = true);

        tb.addKeyPressHandler(event -> fail("no keyPress event should be triggered when pressing backspace button"));

        tb.addKeyUpHandler(event -> {
            assertThat(event.getNativeKeyCode()).isEqualTo(KeyCodes.KEY_BACKSPACE);
            keyUpCount++;
        });

        tb.addKeyDownHandler(event -> {
            assertThat(event.getNativeKeyCode()).isEqualTo(KeyCodes.KEY_BACKSPACE);
            keyDownCount++;
        });

        // When
        Browser.emptyText(tb, false);

        // Then
        // the textbox value should be updated
        assertThat(tb.getText()).isEqualTo("");
        assertThat(keyDownCount).isEqualTo(4);
        assertThat(keyUpCount).isEqualTo(4);
        assertThat(onBlurTriggered).isTrue();
        assertThat(onChangeTriggered).isTrue();
    }

    @Test
    public void emptyText_LongPressTrue() {
        // Given
        String initialText = "1234";

        final TextBox tb = new TextBox();
        tb.setText(initialText);

        tb.addChangeHandler(event -> onChangeTriggered = true);

        tb.addBlurHandler(event -> onBlurTriggered = true);

        tb.addKeyPressHandler(event -> fail("no keyPress event should be triggered when pressing backspace button"));

        tb.addKeyUpHandler(event -> {
            assertThat(event.getNativeKeyCode()).isEqualTo(KeyCodes.KEY_BACKSPACE);
            keyUpCount++;
        });

        tb.addKeyDownHandler(event -> {
            assertThat(event.getNativeKeyCode()).isEqualTo(KeyCodes.KEY_BACKSPACE);
            keyDownCount++;
        });

        // When
        Browser.emptyText(tb, true);

        // Then
        // the textbox value should be updated
        assertThat(tb.getText()).isEqualTo("");
        assertThat(keyDownCount).isEqualTo(4);
        assertThat(keyUpCount).isEqualTo(1);
        assertThat(onBlurTriggered).isTrue();
        assertThat(onChangeTriggered).isTrue();
    }

    @Test
    public void emptyText_LongPressTrue_Does_Not_Update_When_KeyDown_PreventDefault() {
        // Given
        String initialText = "1234";

        final TextBox tb = new TextBox();
        tb.setText(initialText);

        tb.addChangeHandler(event -> onChangeTriggered = true);

        tb.addBlurHandler(event -> onBlurTriggered = true);

        tb.addKeyPressHandler(event -> fail("no keyPress event should be triggered when pressing backspace button"));

        tb.addKeyUpHandler(event -> {
            assertThat(event.getNativeKeyCode()).isEqualTo(KeyCodes.KEY_BACKSPACE);
            keyUpCount++;
        });

        tb.addKeyDownHandler(event -> {
            assertThat(event.getNativeKeyCode()).isEqualTo(KeyCodes.KEY_BACKSPACE);
            keyDownCount++;

            event.preventDefault();
        });

        // When
        Browser.emptyText(tb, true);

        // Then
        // the textbox value should not be updated
        assertThat(tb.getText()).isEqualTo("1234");
        assertThat(keyDownCount).isEqualTo(4);
        assertThat(keyUpCount).isEqualTo(1);
        assertThat(onBlurTriggered).isTrue();
        assertThat(onChangeTriggered).isFalse();
    }

    @Test
    public void fillText() {
        // Given
        String textToFill = "some text";

        final List<Character> keyUpChars = new ArrayList<>();
        final List<Character> keyDownChars = new ArrayList<>();
        final List<Character> keyPressChars = new ArrayList<>();

        final TextBox tb = new TextBox();

        tb.addChangeHandler(event -> onChangeTriggered = true);

        tb.addBlurHandler(event -> onBlurTriggered = true);

        tb.addKeyPressHandler(event -> {
            // Then that onKeyPress is triggered before onKeyUp and after
            // onKeyDown
            assertThat(keyUpChars.size()).isEqualTo(keyPressChars.size());
            assertThat(keyDownChars.size()).isEqualTo(keyPressChars.size() + 1);

            keyPressChars.add(event.getCharCode());
        });

        tb.addKeyUpHandler(event -> {
            // Then that onKeyUp is triggered after onKeyDown and onKeyPress
            assertThat(keyDownChars.size()).isEqualTo(keyUpChars.size() + 1);
            assertThat(keyPressChars.size()).isEqualTo(keyUpChars.size() + 1);

            keyUpChars.add((char) event.getNativeKeyCode());
        });

        tb.addKeyDownHandler(event -> {
            // Then that onKeyDown is triggered before onKeyPress and onKeyUp
            assertThat(keyPressChars.size()).isEqualTo(keyDownChars.size());
            assertThat(keyUpChars.size()).isEqualTo(keyDownChars.size());

            keyDownChars.add((char) event.getNativeKeyCode());
        });

        // When
        Browser.fillText(tb, textToFill);

        // Then
        assertThat(tb.getText()).isEqualTo(textToFill);
        assertThat(tb.getValue()).isEqualTo(textToFill);
        assertTextFilledCorrectly(textToFill, keyDownChars);
        assertTextFilledCorrectly(textToFill, keyPressChars);
        assertTextFilledCorrectly(textToFill, keyUpChars);
        assertThat(onBlurTriggered).isTrue();
        assertThat(onChangeTriggered).isTrue();
        assertThat(tb.getCursorPos()).isEqualTo(textToFill.length());
    }

    @Test
    public void fillText_Does_Not_Update_When_KeyDown_PreventDefault() {
        // Given
        String initialText = "intial text";
        String textToFill = "some text which will not be filled";

        final List<Character> keyUpChars = new ArrayList<>();
        final List<Character> keyDownChars = new ArrayList<>();
        final List<Character> keyPressChars = new ArrayList<>();

        final TextBox tb = new TextBox();
        tb.setText(initialText);

        tb.addChangeHandler(event -> onChangeTriggered = true);

        tb.addBlurHandler(event -> onBlurTriggered = true);

        tb.addKeyPressHandler(event -> {
            // Then that onKeyPress is triggered before onKeyUp and after
            // onKeyDown
            assertThat(keyUpChars.size()).isEqualTo(keyPressChars.size());
            assertThat(keyDownChars.size()).isEqualTo(keyPressChars.size() + 1);

            keyPressChars.add(event.getCharCode());
        });

        tb.addKeyUpHandler(event -> {
            // Then that onKeyUp is triggered after onKeyDown and onKeyPress
            assertThat(keyDownChars.size()).isEqualTo(keyUpChars.size() + 1);
            assertThat(keyPressChars.size()).isEqualTo(keyUpChars.size() + 1);

            keyUpChars.add((char) event.getNativeKeyCode());
        });

        tb.addKeyDownHandler(event -> {
            // Then that onKeyDown is triggered before onKeyPress and onKeyUp
            assertThat(keyPressChars.size()).isEqualTo(keyDownChars.size());
            assertThat(keyUpChars.size()).isEqualTo(keyDownChars.size());

            keyDownChars.add((char) event.getNativeKeyCode());

            // prevent the keydown event : the textbox value should not be
            // updated
            event.preventDefault();
        });

        // When
        Browser.fillText(tb, textToFill);

        // Then
        // the textbox value should not be updated
        assertThat(tb.getText()).isEqualTo(initialText);
        assertThat(tb.getValue()).isEqualTo(initialText);
        assertThat(onChangeTriggered).isFalse();

        assertTextFilledCorrectly(textToFill, keyDownChars);
        assertTextFilledCorrectly(textToFill, keyPressChars);
        assertTextFilledCorrectly(textToFill, keyUpChars);
        assertThat(onBlurTriggered).isTrue();
        assertThat(tb.getCursorPos()).isEqualTo(0);
    }

    @Test
    public void fillText_Does_Not_Update_When_KeyPress_PreventDefault() {
        // Given
        String initialText = "intial text";
        String textToFill = "some text which will not be filled";

        final List<Character> keyUpChars = new ArrayList<>();
        final List<Character> keyDownChars = new ArrayList<>();
        final List<Character> keyPressChars = new ArrayList<>();

        final TextBox tb = new TextBox();
        tb.setText(initialText);

        tb.addChangeHandler(event -> onChangeTriggered = true);

        tb.addBlurHandler(event -> onBlurTriggered = true);

        tb.addKeyPressHandler(event -> {
            // Then that onKeyPress is triggered before onKeyUp and after
            // onKeyDown
            assertThat(keyUpChars.size()).isEqualTo(keyPressChars.size());
            assertThat(keyDownChars.size()).isEqualTo(keyPressChars.size() + 1);

            keyPressChars.add(event.getCharCode());

            // prevent the keyPress event : the textbox value should not be
            // updated
            event.preventDefault();
        });

        tb.addKeyUpHandler(event -> {
            // Then that onKeyUp is triggered after onKeyDown and onKeyPress
            assertThat(keyDownChars.size()).isEqualTo(keyUpChars.size() + 1);
            assertThat(keyPressChars.size()).isEqualTo(keyUpChars.size() + 1);

            keyUpChars.add((char) event.getNativeKeyCode());
        });

        tb.addKeyDownHandler(event -> {
            // Then that onKeyDown is triggered before onKeyPress and onKeyUp
            assertThat(keyPressChars.size()).isEqualTo(keyDownChars.size());
            assertThat(keyUpChars.size()).isEqualTo(keyDownChars.size());

            keyDownChars.add((char) event.getNativeKeyCode());
        });

        // When
        Browser.fillText(tb, textToFill);

        // Then
        // the textbox value should not be updated
        assertThat(tb.getText()).isEqualTo(initialText);
        assertThat(tb.getValue()).isEqualTo(initialText);
        assertThat(onChangeTriggered).isFalse();

        assertTextFilledCorrectly(textToFill, keyDownChars);
        assertTextFilledCorrectly(textToFill, keyPressChars);
        assertTextFilledCorrectly(textToFill, keyUpChars);
        assertThat(onBlurTriggered).isTrue();
        assertThat(tb.getCursorPos()).isEqualTo(0);
    }

    @Test
    public void fillText_EmptyShouldThrowsAnError() {
        // Given
        final TextBox tb = new TextBox();
        tb.setText("test");

        // When
        try {
            Browser.fillText("", tb);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(IllegalArgumentException.class);
            assertThat(e).hasMessage("Cannot fill a null or empty text. If you intent to remove some text, use 'Browser.emptyText(..)' instead");
        }
    }

    @Test
    public void fillText_NullShouldThrowsAnError() {
        // Given
        final TextBox tb = new TextBox();
        tb.setText("test");

        // When
        try {
            Browser.fillText(null, tb);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(IllegalArgumentException.class);
            assertThat(e).hasMessage("Cannot fill a null or empty text. If you intent to remove some text, use 'Browser.emptyText(..)' instead");
        }
    }

    @Test
    public void fillText_Still_Update_When_KeyUp_PreventDefault() {
        // Given
        String initialText = "intial text";
        String textToFill = "some text which will not be filled";

        final List<Character> keyUpChars = new ArrayList<>();
        final List<Character> keyDownChars = new ArrayList<>();
        final List<Character> keyPressChars = new ArrayList<>();

        final TextBox tb = new TextBox();
        tb.setText(initialText);

        tb.addChangeHandler(event -> onChangeTriggered = true);

        tb.addBlurHandler(event -> onBlurTriggered = true);

        tb.addKeyPressHandler(event -> {
            // Then that onKeyPress is triggered before onKeyUp and after
            // onKeyDown
            assertThat(keyUpChars.size()).isEqualTo(keyPressChars.size());
            assertThat(keyDownChars.size()).isEqualTo(keyPressChars.size() + 1);

            keyPressChars.add(event.getCharCode());
        });

        tb.addKeyUpHandler(event -> {
            // Then that onKeyUp is triggered after onKeyDown and onKeyPress
            assertThat(keyDownChars.size()).isEqualTo(keyUpChars.size() + 1);
            assertThat(keyPressChars.size()).isEqualTo(keyUpChars.size() + 1);

            keyUpChars.add((char) event.getNativeKeyCode());

            // prevent the keyUp event : the textbox value should be updated
            event.preventDefault();
        });

        tb.addKeyDownHandler(event -> {
            // Then that onKeyDown is triggered before onKeyPress and onKeyUp
            assertThat(keyPressChars.size()).isEqualTo(keyDownChars.size());
            assertThat(keyUpChars.size()).isEqualTo(keyDownChars.size());

            keyDownChars.add((char) event.getNativeKeyCode());
        });

        // When
        Browser.fillText(tb, textToFill);

        // Then
        // the textbox value should be updated
        assertThat(tb.getText()).isEqualTo(textToFill);
        assertThat(tb.getValue()).isEqualTo(textToFill);
        assertThat(onChangeTriggered).isTrue();

        assertTextFilledCorrectly(textToFill, keyDownChars);
        assertTextFilledCorrectly(textToFill, keyPressChars);
        assertTextFilledCorrectly(textToFill, keyUpChars);
        assertThat(onBlurTriggered).isTrue();
    }

    @Test
    public void focus() {
        // Given
        b.addFocusHandler(event -> {
            tested = !tested;

            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(b.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isNull();
        });

        // When
        Browser.focus(b);

        // Then
        assertThat(tested).as("onFocus event was not triggered").isTrue();
    }

    @Test
    public void keyDown() {
        // Given
        b.addKeyDownHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                tested = !tested;
            }

            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(b.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isNull();
        });

        // When 1
        Browser.keyDown(b, KeyCodes.KEY_ESCAPE);
        // Then 1
        assertThat(tested).as("onKeyDown event should not be triggered").isFalse();

        // When 2
        Browser.keyDown(b, KeyCodes.KEY_ENTER);
        // Then 2
        assertThat(tested).as("onKeyDown event was not triggered").isTrue();
    }

    @Test
    public void keyPress() {
        // Given
        b.addKeyPressHandler(event -> {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                tested = !tested;
            }

            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(b.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isNull();
        });

        // When 1
        Browser.keyDown(b, KeyCodes.KEY_ESCAPE);
        // Then 1
        assertThat(tested).as("onKeyPress event should not be triggered").isFalse();

        // When 2
        Browser.keyPress(b, KeyCodes.KEY_ENTER);
        // Then 2
        assertThat(tested).as("onKeyPress event was not triggered").isTrue();
    }

    @Test
    public void keyUp() {
        // Given
        b.addKeyUpHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                tested = !tested;
            }

            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(b.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isNull();
        });

        // When 1
        Browser.keyDown(b, KeyCodes.KEY_ESCAPE);
        // Then 1
        assertThat(tested).as("onKeyUp event should not be triggered").isFalse();

        // When 2
        Browser.keyUp(b, KeyCodes.KEY_ENTER);
        // Then 2
        assertThat(tested).as("onKeyUp event was not triggered").isTrue();
    }

    @Test
    public void mouseDown() {
        // Given
        b.addMouseDownHandler(event -> {
            tested = !tested;

            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(b.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isNull();
        });

        // When
        Browser.mouseDown(b);

        // Then
        assertThat(tested).as("onMouseDown event was not triggered").isTrue();
    }

    @Test
    public void mouseMove() {
        // Given
        b.addMouseMoveHandler(event -> {
            tested = !tested;

            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(b.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isNull();
        });

        // When
        Browser.mouseMove(b);

        // Then
        assertThat(tested).as("onMouseMove event was not triggered").isTrue();
    }

    @Test
    public void mouseOut() {
        // Given
        b.addMouseOutHandler(event -> {
            tested = !tested;

            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(b.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isEqualTo(b.getParent().getElement());
        });

        // When
        Browser.mouseOut(b);

        // Then
        assertThat(tested).as("onMouseOut event was not triggered").isTrue();
    }

    @Test
    public void mouseOver() {
        // Given
        b.addMouseOverHandler(event -> {
            tested = !tested;

            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(b.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isEqualTo(b.getParent().getElement());
        });

        // When
        Browser.mouseOver(b);

        // Then
        assertThat(tested).as("onMouseOver event was not triggered").isTrue();
    }

    @Test
    public void mouseUp() {
        // Given
        b.addMouseUpHandler(event -> {
            tested = !tested;

            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(b.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isNull();
        });

        // When
        Browser.mouseUp(b);

        // Then
        assertThat(tested).as("onMouseUp event was not triggered").isTrue();
    }

    @Test
    public void mouseWheel() {
        // Given
        b.addMouseWheelHandler(event -> {
            tested = !tested;

            assertThat(event.getNativeEvent().getEventTarget()).isEqualTo(b.getElement());
            assertThat(event.getNativeEvent().getRelatedEventTarget()).isNull();
        });

        // When
        Browser.mouseWheel(b);

        // Then
        assertThat(tested).as("onMouseWheel event was not triggered").isTrue();
    }

    @Test
    public void removeText() {
        // Given
        onChangeTriggered = false;
        onBlurTriggered = false;
        keyDownCount = 0;
        keyUpCount = 0;
        String initialText = "1234";

        final TextBox tb = new TextBox();
        tb.setText(initialText);

        tb.addChangeHandler(event -> onChangeTriggered = true);

        tb.addBlurHandler(event -> onBlurTriggered = true);

        tb.addKeyPressHandler(event -> fail("no keyPress event should be triggered when pressing backspace button"));

        tb.addKeyUpHandler(event -> {
            assertThat(event.getNativeKeyCode()).isEqualTo(KeyCodes.KEY_BACKSPACE);
            keyUpCount++;
        });

        tb.addKeyDownHandler(event -> {
            assertThat(event.getNativeKeyCode()).isEqualTo(KeyCodes.KEY_BACKSPACE);
            keyDownCount++;
        });

        // When
        Browser.removeText(tb, 2);

        // Then
        // the textbox value should be updated
        assertThat(tb.getText()).isEqualTo("12");
        assertThat(keyDownCount).isEqualTo(2);
        assertThat(keyUpCount).isEqualTo(2);
        assertThat(onBlurTriggered).isTrue();
        assertThat(onChangeTriggered).isTrue();
    }

    @Test
    public void removeText_Does_Not_Update_When_KeyDown_PreventDefault() {
        // Given
        onChangeTriggered = false;
        onBlurTriggered = false;
        keyDownCount = 0;
        keyUpCount = 0;
        String initialText = "1234";

        final TextBox tb = new TextBox();
        tb.setText(initialText);

        tb.addChangeHandler(event -> onChangeTriggered = true);

        tb.addBlurHandler(event -> onBlurTriggered = true);

        tb.addKeyPressHandler(event -> fail("no keyPress event should be triggered when pressing backspace button"));

        tb.addKeyUpHandler(event -> {
            assertThat(event.getNativeKeyCode()).isEqualTo(KeyCodes.KEY_BACKSPACE);
            keyUpCount++;
        });

        tb.addKeyDownHandler(event -> {
            assertThat(event.getNativeKeyCode()).isEqualTo(KeyCodes.KEY_BACKSPACE);
            keyDownCount++;

            event.preventDefault();
        });

        // When
        Browser.removeText(tb, 2);

        // Then
        // the textbox value should be updated
        assertThat(tb.getText()).isEqualTo("1234");
        assertThat(keyDownCount).isEqualTo(2);
        assertThat(keyUpCount).isEqualTo(2);
        assertThat(onBlurTriggered).isTrue();
        assertThat(onChangeTriggered).isFalse();
    }

    @Test()
    public void submit() {
        // Given
        final StringBuilder sb = new StringBuilder();
        FormPanel form = new FormPanel();
        form.addSubmitHandler(event -> sb.append("onSubmit"));
        form.addSubmitCompleteHandler(event -> sb.append(" complete : ").append(event.getResults()));

        // Attach to the DOM
        RootPanel.get().add(form);

        // Given
        Browser.submit(form, "mock result");

        // Then
        assertThat(sb.toString()).isEqualTo("onSubmit complete : mock result");
    }

    @Test(expected = AssertionError.class)
    public void submitThrowsErrorIfNotAttached() {
        // Given
        final StringBuilder sb = new StringBuilder();
        FormPanel form = new FormPanel();
        form.addSubmitHandler(new SubmitHandler() {

            public void onSubmit(SubmitEvent event) {
                sb.append("onSubmit");
            }
        });
        form.addSubmitCompleteHandler(event -> sb.append(" complete : ").append(event.getResults()));

        // When
        Browser.submit(form, "mock result");
    }

    private void assertTextFilledCorrectly(String filledText, List<Character> typedChars) {

        assertThat(typedChars.size()).isEqualTo(filledText.length());

        for (int i = 0; i < filledText.length(); i++) {
            assertThat(typedChars.get(i)).isEqualTo((Object) filledText.charAt(i));
        }

    }
}
