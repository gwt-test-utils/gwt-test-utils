package com.googlecode.gwt.test.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.internal.MyClassToPatch.MyInnerClass;

public class AutomaticPatcherTest extends GwtTestTest {

   private MyClassToPatch instance;

   @Test
   public void checkPatchWithInnerClassAndMultiplePatchers() throws Exception {
      // Arrange
      MyInnerClass innerObject = new MyInnerClass("innerOjbectForUnitTest");

      // Act
      String result = instance.myStringMethod(innerObject);

      // Assert
      assertEquals(
               "myStringMethod has been patched by override patcher : patched by MyInnerClassOverridePatcher : new field added in overrided init",
               result);
   }

   @Before
   public void beforeAutomaticPatcherTest() {
      instance = new MyClassToPatch();
   }

}
