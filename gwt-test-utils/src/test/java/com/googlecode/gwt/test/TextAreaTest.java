package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.TextArea;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TextAreaTest extends GwtTestTest {

    @Test
    public void getCursorPos() {
        // Given
        TextArea t = new TextArea();
        t.setText("myText");
        GwtReflectionUtils.setPrivateFieldValue(t, "attached", true);

        // When
        t.setCursorPos(2);

        // Then
        assertThat(t.getCursorPos()).isEqualTo(2);
    }

    @Test
    public void getSelectionLength() {
        // Given
        TextArea t = new TextArea();
        t.setText("myText");
        GwtReflectionUtils.setPrivateFieldValue(t, "attached", true);

        // When
        t.setSelectionRange(1, 3);

        // Then
        assertThat(t.getSelectionLength()).isEqualTo(3);
    }

    @Test
    public void name() {
        // Given
        TextArea t = new TextArea();
        // Preconditions
        assertThat(t.getName()).isEqualTo("");

        // When
        t.setName("name");

        // Then
        assertThat(t.getName()).isEqualTo("name");
    }

    @Test
    public void text() {
        // Given
        TextArea t = new TextArea();
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
        TextArea t = new TextArea();
        // Preconditions
        assertThat(t.getTitle()).isEqualTo("");

        // When
        t.setTitle("title");

        // Then
        assertThat(t.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Given
        TextArea t = new TextArea();
        // Preconditions
        assertThat(t.isVisible()).isEqualTo(true);

        // When
        t.setVisible(false);

        // Then
        assertThat(t.isVisible()).isEqualTo(false);
    }

    @Test
    public void visibleLines() {
        // Given
        TextArea t = new TextArea();

        // When
        t.setVisibleLines(10);

        // Then
        assertThat(t.getVisibleLines()).isEqualTo(10);
    }

}
