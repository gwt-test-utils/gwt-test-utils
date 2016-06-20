package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.Frame;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FrameTest extends GwtTestTest {

    @Test
    public void title() {
        // Given
        Frame f = new Frame();

        // When
        f.setTitle("title");

        // Then
        assertThat(f.getTitle()).isEqualTo("title");
    }

    @Test
    public void url() {
        // Given
        Frame f = new Frame("url");
        // Preconditions
        assertThat(f.getUrl()).isEqualTo("url");

        // When
        f.setUrl("newURL");

        // Then
        assertThat(f.getUrl()).isEqualTo("newURL");
    }

}
