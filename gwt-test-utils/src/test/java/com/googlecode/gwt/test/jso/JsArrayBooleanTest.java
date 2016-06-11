package com.googlecode.gwt.test.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayBoolean;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class JsArrayBooleanTest extends GwtTestTest {

    private JsArrayBoolean jsArrayBoolean;

    @Before
    public void beforeJsArrayIntegerTest() {
        // Arrange
        jsArrayBoolean = JavaScriptObject.createArray().cast();
        assertThat(jsArrayBoolean.length()).isEqualTo(0);

        // Act
        jsArrayBoolean.set(4, true);

        // Assert
        assertThat(jsArrayBoolean.length()).isEqualTo(5);
        assertThat(jsArrayBoolean.get(3)).isFalse();
        assertThat(jsArrayBoolean.get(4)).isTrue();
    }

    @Test
    public void join() {
        // Act
        String join = jsArrayBoolean.join();

        // Assert
        assertThat(join).isEqualTo(",,,,true");
    }

    @Test
    public void join_AfterResize() {
        // Arrange
        jsArrayBoolean.setLength(3);

        // Act
        String join = jsArrayBoolean.join();

        // Assert
        assertThat(jsArrayBoolean.length()).isEqualTo(3);
        assertThat(join).isEqualTo(",,");
    }

    @Test
    public void push() {
        // Act
        jsArrayBoolean.push(false);

        // Assert

        assertThat(jsArrayBoolean.length()).isEqualTo(6);
        assertThat(jsArrayBoolean.join()).isEqualTo(",,,,true,false");
        assertThat(jsArrayBoolean.get(jsArrayBoolean.length() - 1)).isFalse();
    }

    @Test
    public void shift() {
        // Arrange
        jsArrayBoolean.set(0, true);

        // Act
        boolean shift = jsArrayBoolean.shift();

        // Assert
        assertThat(shift).isTrue();
        assertThat(jsArrayBoolean.length()).isEqualTo(4);
        assertThat(jsArrayBoolean.join()).isEqualTo(",,,true");
    }

    @Test
    public void unboundedGet_Returns0() {
        // Act
        boolean unbounded = jsArrayBoolean.get(100);

        // Assert
        assertThat(unbounded).isFalse();
    }

    @Test
    public void unshift() {
        // Act
        jsArrayBoolean.unshift(true);

        // Assert
        assertThat(jsArrayBoolean.length()).isEqualTo(6);
        assertThat(jsArrayBoolean.join()).isEqualTo("true,,,,,true");
    }
}
