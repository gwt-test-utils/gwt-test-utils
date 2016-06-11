package com.googlecode.gwt.test;

import com.google.gwt.core.client.Duration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DurationTest extends GwtTestTest {

    Duration duration;

    @Before
    public void beforeDurationTest() {
        duration = new Duration();
    }

    @Test
    public void currentTimeMillis() {
        // Act
        double currentTimeMillis = Duration.currentTimeMillis();

        // Assert
        assertTrue(currentTimeMillis > 0);
    }

    @Test
    public void elapsedMillis() {
        // Act
        int elapsed = duration.elapsedMillis();

        // Assert
        assertTrue(elapsed > -1);
    }

}
