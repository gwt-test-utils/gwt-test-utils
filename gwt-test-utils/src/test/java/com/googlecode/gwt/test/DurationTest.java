package com.googlecode.gwt.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.core.client.Duration;

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
