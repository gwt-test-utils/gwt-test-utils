package com.googlecode.gwt.test.internal;

import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.internal.MyClassToPatch.MyInnerClass;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AutomaticPatcherTest extends GwtTestTest {

    private MyClassToPatch instance;

    @Before
    public void beforeAutomaticPatcherTest() {
        instance = new MyClassToPatch();
    }

    @Test
    public void checkPatchWithInnerClassAndMultiplePatchers() throws Exception {
        // Given
        MyInnerClass innerObject = new MyInnerClass("innerOjbectForUnitTest");

        // When
        String result = instance.myStringMethod(innerObject);

        // Then
        assertThat(result).isEqualTo("myStringMethod has been patched by override patcher : patched by MyInnerClassOverridePatcher : new field added in overrided init");
    }

}
