package com.googlecode.gwt.test.internal;

import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.internal.MyClassToPatch.MyInnerClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AutomaticPatcherTest extends GwtTestTest {

    private MyClassToPatch instance;

    @Before
    public void beforeAutomaticPatcherTest() {
        instance = new MyClassToPatch();
    }

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

}
