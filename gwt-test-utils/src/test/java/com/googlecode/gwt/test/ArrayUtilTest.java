package com.googlecode.gwt.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.googlecode.gwt.test.internal.utils.ArrayUtils;

public class ArrayUtilTest {

   @Test
   public void contains() {
      // Arrange
      String[] strings = new String[]{"test1", "test2", "test3"};

      // Act & Assert
      assertTrue(ArrayUtils.contains(strings, "test1"));
      assertTrue(ArrayUtils.contains(strings, "test2"));
      assertTrue(ArrayUtils.contains(strings, "test3"));
      assertFalse(ArrayUtils.contains(strings, "test4"));
   }

}
