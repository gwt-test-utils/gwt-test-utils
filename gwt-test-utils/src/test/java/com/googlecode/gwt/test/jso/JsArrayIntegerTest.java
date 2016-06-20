package com.googlecode.gwt.test.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsArrayIntegerTest extends GwtTestTest {

    private JsArrayInteger jsArrayInteger;

    @Before
    public void beforeJsArrayIntegerTest() {
        // Given
        jsArrayInteger = JavaScriptObject.createArray().cast();
        assertThat(jsArrayInteger.length()).isEqualTo(0);

        // When
        jsArrayInteger.set(4, 23);

        // Then
        assertThat(jsArrayInteger.length()).isEqualTo(5);
        assertThat(jsArrayInteger.get(3)).isEqualTo(0);
    }

    @Test
    public void join() {
        // When
        String join = jsArrayInteger.join();

        // Then
        assertThat(join).isEqualTo(",,,,23");
    }

    @Test
    public void join_AfterResize() {
        // Given
        jsArrayInteger.setLength(3);

        // When
        String join = jsArrayInteger.join();

        // Then
        assertThat(jsArrayInteger.length()).isEqualTo(3);
        assertThat(join).isEqualTo(",,");
    }

    @Test
    public void push() {
        // When
        jsArrayInteger.push(42);

        // Then

        assertThat(jsArrayInteger.length()).isEqualTo(6);
        assertThat(jsArrayInteger.join()).isEqualTo(",,,,23,42");
        assertThat(jsArrayInteger.get(jsArrayInteger.length() - 1)).isEqualTo(42);
    }

    @Test
    public void shift() {
        // Given
        jsArrayInteger.set(0, 2);

        // When
        double shift = jsArrayInteger.shift();

        // Then
        assertThat(shift).isEqualTo(2d);
        assertThat(jsArrayInteger.length()).isEqualTo(4);
        assertThat(jsArrayInteger.join()).isEqualTo(",,,23");
    }

    @Test
    public void unboundedGet_Returns0() {
        // When
        double unbounded = jsArrayInteger.get(100);

        // Then
        assertThat(unbounded).isEqualTo(0);
    }

    @Test
    public void unshift() {
        // When
        jsArrayInteger.unshift(8);

        // Then
        assertThat(jsArrayInteger.length()).isEqualTo(6);
        assertThat(jsArrayInteger.join()).isEqualTo("8,,,,,23");
    }
}
