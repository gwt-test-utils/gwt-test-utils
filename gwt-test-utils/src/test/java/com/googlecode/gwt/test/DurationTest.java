package com.googlecode.gwt.test;

import com.google.gwt.core.client.Duration;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DurationTest extends GwtTestTest {

    Duration duration;

    @Before
    public void beforeDurationTest() {
        duration = new Duration();
    }

    @Test
    public void currentTimeMillis() {
        // When
        double currentTimeMillis = Duration.currentTimeMillis();

        // Then
        assertThat(currentTimeMillis > 0).isTrue();
    }

    @Test
    public void elapsedMillis() {
        // When
        int elapsed = duration.elapsedMillis();

        // Then
        assertThat(elapsed > -1).isTrue();
    }

}
