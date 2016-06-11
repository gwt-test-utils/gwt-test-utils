package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.Button;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DebugIdDisabledTest extends GwtTestTest {

    @Override
    public boolean ensureDebugId() {
        return false;
    }

    @Test
    public void ensureDebugId_Disabled() {
        // Arrange
        Button b = new Button();

        // Act
        b.ensureDebugId("myDebugId");

        // Assert
        assertEquals("", b.getElement().getId());
    }

}
