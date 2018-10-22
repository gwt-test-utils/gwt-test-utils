package com.googlecode.gwt.test;

import com.google.gwt.core.client.GWT;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GwtLogTest extends GwtTestTest {

    private String message;

    private Throwable t;

    @Before
    public void beforeGwtLogTest() {
        setLogHandler((message, t) -> {
            GwtLogTest.this.message = message;
            GwtLogTest.this.t = t;
        });
    }

    @Test
    public void log() {
        // Given
        message = null;
        t = null;
        Throwable throwable = new Exception("test");

        // When
        GWT.log("toto", throwable);

        // Then
        assertThat(message).isEqualTo("toto");
        assertThat(t).isEqualTo(throwable);
    }
}
