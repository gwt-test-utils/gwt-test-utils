package com.googlecode.gwt.test;

import com.google.gwt.user.client.Timer;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimerTest extends GwtTestTest {

    boolean bool;
    int i;

    @Test
    public void schedule() throws Exception {
        // Given
        bool = false;
        Timer timer = new Timer() {

            @Override
            public void run() {
                bool = !bool;
            }
        };

        // When
        timer.schedule(250);

        // Then 1
        assertThat(bool).overridingErrorMessage("The Timer fired too soon").isFalse();

        Thread.sleep(400);

        // Then
        assertThat(bool).overridingErrorMessage("The token was not set after Timer has run").isTrue();
    }

    @Test
    public void scheduleRepeating() throws Exception {
        // Given
        final int TIMES = 3;
        i = 0;
        Timer timer = new Timer() {

            @Override
            public void run() {
                i++;
            }
        };

        // When
        timer.scheduleRepeating(400);

        // Then
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
