package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.PasswordTextBox;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordTextBoxTest extends GwtTestTest {

    @Test
    public void name() {
        // Given
        PasswordTextBox ptb = new PasswordTextBox();
        // Preconditions
        assertThat(ptb.getName()).isEqualTo("");

        // When
        ptb.setName("name");

        // Then
        assertThat(ptb.getName()).isEqualTo("name");
    }

    @Test
    public void text() {
        // Given
        PasswordTextBox ptb = new PasswordTextBox();
        // Preconditions
        assertThat(ptb.getText()).isEqualTo("");

        // When
        ptb.setText("text");

        // Then
        assertThat(ptb.getText()).isEqualTo("text");
    }

    @Test
    public void title() {
        // Given
        PasswordTextBox ptb = new PasswordTextBox();
        // Preconditions
        assertThat(ptb.getTitle()).isEqualTo("");

        // When
        ptb.setTitle("title");

        // Then
        assertThat(ptb.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Given
        PasswordTextBox ptb = new PasswordTextBox();
        // Preconditions
        assertThat(ptb.isVisible()).isEqualTo(true);

        // When
        ptb.setVisible(false);

        // Then
        assertThat(ptb.isVisible()).isEqualTo(false);
    }

}
