package com.googlecode.gwt.test;

import com.google.gwt.user.client.Timer;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class TimerTest extends GwtTestTest {

    boolean bool;
    int i;

    @Test
    public void schedule() throws Exception {
        // Arrange
        bool = false;
        Timer timer = new Timer() {

            @Override
            public void run() {
                bool = !bool;
            }
        };

        // Act
        timer.schedule(250);

        // Assert 1
        assertThat(bool).overridingErrorMessage("The Timer fired too soon").isFalse();

        Thread.sleep(400);

        // Assert
        assertThat(bool).overridingErrorMessage("The token was not set after Timer has run").isTrue();
    }

    @Test
    public void scheduleRepeating() throws Exception {
        // Arrange
        final int TIMES = 3;
        i = 0;
        Timer timer = new Timer() {

            @Override
            public void run() {
                i++;
            }
        };

        // Act
        timer.scheduleRepeating(400);

        // Assert
        Thread.sleep(200);
        for (int count = 0; count <= TIMES; count++) {
            // tests at instant 200, 600, 1000, 1400
            assertThat(count).overridingErrorMessage(
                    "Timer didn't fire correctly, expected number of fire <%s> but was </%s>", count,
                    i).isEqualTo(i);
            Thread.sleep(400);
        }
    }
}
