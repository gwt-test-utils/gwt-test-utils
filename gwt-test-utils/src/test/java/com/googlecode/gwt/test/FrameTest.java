package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.Frame;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FrameTest extends GwtTestTest {

    @Test
    public void title() {
        // Arrange
        Frame f = new Frame();

        // Act
        f.setTitle("title");

        // Assert
        assertEquals("title", f.getTitle());
    }

    @Test
    public void url() {
        // Arrange
        Frame f = new Frame("url");
        // Pre-Assert
        assertEquals("url", f.getUrl());

        // Act
        f.setUrl("newURL");

        // Assert
        assertEquals("newURL", f.getUrl());
    }

}
