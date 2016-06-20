package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.Button;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DebugIdDisabledTest extends GwtTestTest {

    @Override
    public boolean ensureDebugId() {
        return false;
    }

    @Test
    public void ensureDebugId_Disabled() {
        // Given
        Button b = new Button();

        // When
        b.ensureDebugId("myDebugId");

        // Then
        assertThat(b.getElement().getId()).isEqualTo("");
    }

}
