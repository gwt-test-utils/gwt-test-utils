package com.googlecode.gwt.test.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsArrayNumberTest extends GwtTestTest {

    private JsArrayNumber jsArrayNumber;

    @Before
    public void beforeJsArrayIntegerTest() {
        // Given
        jsArrayNumber = JavaScriptObject.createArray().cast();
        assertThat(jsArrayNumber.length()).isEqualTo(0);

        // When
        jsArrayNumber.set(4, 23);

        // Then
        assertThat(jsArrayNumber.length()).isEqualTo(5);
        assertThat(jsArrayNumber.get(3)).isEqualTo(0);
    }

    @Test
    public void join() {
        // When
        String join = jsArrayNumber.join();

        // Then
        assertThat(join).isEqualTo(",,,,23.0");
    }

    @Test
    public void join_AfterResize() {
        // Given
        jsArrayNumber.setLength(3);

        // When
        String join = jsArrayNumber.join();

        // Then
        assertThat(jsArrayNumber.length()).isEqualTo(3);
        assertThat(join).isEqualTo(",,");
    }

    @Test
    public void push() {
        // When
        jsArrayNumber.push(42);

        // Then

        assertThat(jsArrayNumber.length()).isEqualTo(6);
        assertThat(jsArrayNumber.join()).isEqualTo(",,,,23.0,42.0");
        assertThat(jsArrayNumber.get(jsArrayNumber.length() - 1)).isEqualTo(42);
    }

    @Test
    public void shift() {
        // Given
        jsArrayNumber.set(0, 2);

        // When
        double shift = jsArrayNumber.shift();

        // Then
        assertThat(shift).isEqualTo(2d);
        assertThat(jsArrayNumber.length()).isEqualTo(4);
        assertThat(jsArrayNumber.join()).isEqualTo(",,,23.0");
    }

    @Test
    public void unboundedGet_Returns0() {
        // When
        double unbounded = jsArrayNumber.get(100);

        // Then
        assertThat(unbounded).isEqualTo(0);
    }

    @Test
    public void unshift() {
        // When
        jsArrayNumber.unshift(8);

        // Then
        assertThat(jsArrayNumber.length()).isEqualTo(6);
        assertThat(jsArrayNumber.join()).isEqualTo("8.0,,,,,23.0");
    }
}
