package com.googlecode.gwt.test.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsArrayStringTest extends GwtTestTest {

    private JsArrayString jsArrayString;

    @Before
    public void beforeJsArrayStringTest() {
        // Given
        jsArrayString = JavaScriptObject.createArray().cast();
        assertThat(jsArrayString.length()).isEqualTo(0);

        // When
        jsArrayString.set(4, "test");

        // Then
        assertThat(jsArrayString.length()).isEqualTo(5);
        assertThat(jsArrayString.get(3)).isNull();
    }

    @Test
    public void join() {
        // When
        String join = jsArrayString.join();

        // Then
        assertThat(join).isEqualTo(",,,,test");
    }

    @Test
    public void join_AfterResize() {
        // Given
        jsArrayString.setLength(3);

        // When
        String join = jsArrayString.join();

        // Then
        assertThat(join).isEqualTo(",,");
        assertThat(jsArrayString.length()).isEqualTo(3);
    }

    @Test
    public void push() {
        // When
        jsArrayString.push("pushed");

        // Then
        assertThat(jsArrayString.join()).isEqualTo(",,,,test,pushed");
        assertThat(jsArrayString.length()).isEqualTo(6);
        assertThat(jsArrayString.get(jsArrayString.length() - 1)).isEqualTo("pushed");
    }

    @Test
    public void shift() {
        // Given
        jsArrayString.set(0, "toshift");

        // When
        String shift = jsArrayString.shift();

        // Then
        assertThat(shift).isEqualTo("toshift");
        assertThat(jsArrayString.length()).isEqualTo(4);
        assertThat(jsArrayString.join()).isEqualTo(",,,test");
    }

    @Test
    public void unboundedGet_ReturnsNull() {
        // When
        String unbounded = jsArrayString.get(100);

        // Then
        assertThat(unbounded).isNull();
    }

    @Test
    public void unshift() {
        // When
        jsArrayString.unshift("tounshift");

        // Then
        assertThat(jsArrayString.length()).isEqualTo(6);
        assertThat(jsArrayString.join()).isEqualTo("tounshift,,,,,test");
    }
}
