package com.googlecode.gwt.test.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.utils.JavaScriptObjects;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleJSTest extends GwtTestTest {

    @Test
    public void setNotPatchedProperty() {
        // Given
        JavaScriptObject jso = JavaScriptObject.createObject();
        // SimpleJS.getString() native method by default will try to get "string" property in
        // gwt-test-utils's related PropertyContainer
        JavaScriptObjects.setProperty(jso, "string", "my string for testing");

        // When
        SimpleJS simpleJs = jso.cast();

        // Then
        assertThat(simpleJs.getString()).isEqualTo("my string for testing");
    }

}
