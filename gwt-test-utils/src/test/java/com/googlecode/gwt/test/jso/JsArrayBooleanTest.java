package com.googlecode.gwt.test.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayBoolean;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsArrayBooleanTest extends GwtTestTest {

    private JsArrayBoolean jsArrayBoolean;

    @Before
    public void beforeJsArrayIntegerTest() {
        // Given
        jsArrayBoolean = JavaScriptObject.createArray().cast();
        assertThat(jsArrayBoolean.length()).isEqualTo(0);

        // When
        jsArrayBoolean.set(4, true);

        // Then
        assertThat(jsArrayBoolean.length()).isEqualTo(5);
        assertThat(jsArrayBoolean.get(3)).isFalse();
        assertThat(jsArrayBoolean.get(4)).isTrue();
    }

    @Test
    public void join() {
        // When
        String join = jsArrayBoolean.join();

        // Then
        assertThat(join).isEqualTo(",,,,true");
    }

    @Test
    public void join_AfterResize() {
        // Given
        jsArrayBoolean.setLength(3);

        // When
        String join = jsArrayBoolean.join();

        // Then
        assertThat(jsArrayBoolean.length()).isEqualTo(3);
        assertThat(join).isEqualTo(",,");
    }

    @Test
    public void push() {
        // When
        jsArrayBoolean.push(false);

        // Then

        assertThat(jsArrayBoolean.length()).isEqualTo(6);
        assertThat(jsArrayBoolean.join()).isEqualTo(",,,,true,false");
        assertThat(jsArrayBoolean.get(jsArrayBoolean.length() - 1)).isFalse();
    }

    @Test
    public void shift() {
        // Given
        jsArrayBoolean.set(0, true);

        // When
        boolean shift = jsArrayBoolean.shift();

        // Then
        assertThat(shift).isTrue();
        assertThat(jsArrayBoolean.length()).isEqualTo(4);
        assertThat(jsArrayBoolean.join()).isEqualTo(",,,true");
    }

    @Test
    public void unboundedGet_Returns0() {
        // When
        boolean unbounded = jsArrayBoolean.get(100);

        // Then
        assertThat(unbounded).isFalse();
    }

    @Test
    public void unshift() {
        // When
        jsArrayBoolean.unshift(true);

        // Then
        assertThat(jsArrayBoolean.length()).isEqualTo(6);
        assertThat(jsArrayBoolean.join()).isEqualTo("true,,,,,true");
    }
}
