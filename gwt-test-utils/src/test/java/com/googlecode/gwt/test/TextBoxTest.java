package com.googlecode.gwt.test;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TextBoxTest extends GwtTestTest {

    @Test
    public void getCursorPos() {
        // Given
        TextBox t = new TextBox();
        t.setText("myText");
        GwtReflectionUtils.setPrivateFieldValue(t, "attached", true);

        // When
        t.setCursorPos(2);

        // Then
        assertThat(t.getCursorPos()).isEqualTo(2);
    }

    @Test
    public void maxLength() {
        // Given
        TextBox t = new TextBox();
        // Preconditions
        assertThat(t.getMaxLength()).isEqualTo(0);

        // When
        t.setMaxLength(10);

        // Then
        assertThat(t.getMaxLength()).isEqualTo(10);
    }

    @Test
    public void name() {
        // Given
        TextBox t = new TextBox();
        // Preconditions
        assertThat(t.getName()).isEqualTo("");

        // When
        t.setName("name");

        // Then
        assertThat(t.getName()).isEqualTo("name");
    }

    @Test
    public void pressKey() {
        // Given
        final List<KeyPressEventData> events = new ArrayList<>();
        TextBox tb = new TextBox();

        tb.addKeyPressHandler(event -> {
            KeyPressEventData data = new KeyPressEventData();
            data.keyCode = event.getNativeEvent().getKeyCode();
            data.charCode = event.getCharCode();
            events.add(data);
        });

        // When
        Browser.fillText(tb, "gael");

        // Then
        assertThat(tb.getValue()).isEqualTo("gael");
        assertThat(events).hasSize(4);
        assertThat(events.get(0).charCode).isEqualTo('g');
        assertThat(events.get(0).keyCode).isEqualTo(103);
        assertThat(events.get(1).charCode).isEqualTo('a');
        assertThat(events.get(1).keyCode).isEqualTo(97);
        assertThat(events.get(2).charCode).isEqualTo('e');
        assertThat(events.get(2).keyCode).isEqualTo(101);
        assertThat(events.get(3).charCode).isEqualTo('l');
        assertThat(events.get(3).keyCode).isEqualTo(108);
    }

    @Test
    public void selectAll() {
        // Given
        TextBox t = new TextBox();
        t.setValue("0123456789");
        RootPanel.get().add(t);

        // When
        t.selectAll();

        // Then
        assertThat(t.getSelectionLength()).isEqualTo(10);
        assertThat(t.getSelectedText()).isEqualTo("0123456789");
    }

    @Test
    public void selectionRange() {
        // Given
        TextBox t = new TextBox();
        t.setValue("0123456789");
        RootPanel.get().add(t);

        // When
        t.setSelectionRange(4, 3);

        // Then
        assertThat(t.getSelectionLength()).isEqualTo(3);
        assertThat(t.getSelectedText()).isEqualTo("456");
    }

    @Test
    public void text() {
        // Given
        TextBox t = new TextBox();
        // Preconditions
        assertThat(t.getText()).isEqualTo("");

        // When
        t.setText("text");

        // Then
        assertThat(t.getText()).isEqualTo("text");
    }

    @Test
    public void title() {
        // Given
        TextBox t = new TextBox();
        // Preconditions
        assertThat(t.getTitle()).isEqualTo("");

        // When
        t.setTitle("title");

        // Then
        assertThat(t.getTitle()).isEqualTo("title");
    }

    @Test
    public void value() {
        // Given
        TextBox t = new TextBox();
        // Preconditions
        assertThat(t.getValue()).isEqualTo("");

        // When
        t.setValue("value");

        // Then
        assertThat(t.getValue()).isEqualTo("value");
    }

    @Test
    public void visible() {
        // Given
        TextBox t = new TextBox();
        // Preconditions
        assertThat(t.isVisible()).isEqualTo(true);

        // When
        t.setVisible(false);

        // Then
        assertThat(t.isVisible()).isEqualTo(false);
    }

    private class KeyPressEventData {

        public char charCode;
        public int keyCode;
    }
}
