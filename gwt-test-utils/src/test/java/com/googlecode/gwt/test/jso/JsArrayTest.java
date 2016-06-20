package com.googlecode.gwt.test.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsArrayTest extends GwtTestTest {

    private JsArray<Element> jsArray;

    @Before
    public void beforeJsArrayStringTest() {
        // Given
        jsArray = JavaScriptObject.createObject().cast();
        assertThat(jsArray.length()).isEqualTo(0);

        // When
        jsArray.set(4, Document.get().createAnchorElement());

        // Then
        assertThat(jsArray.length()).isEqualTo(5);
        assertThat(jsArray.get(3)).isNull();
    }

    @Test
    public void join() {
        // When
        String join = jsArray.join();

        // Then
        assertThat(join).isEqualTo(",,,,<a></a>");
    }

    @Test
    public void join_AfterResize() {
        // Given
        jsArray.setLength(3);

        // When
        String join = jsArray.join();

        // Then
        assertThat(join).isEqualTo(",,");
        assertThat(jsArray.length()).isEqualTo(3);
    }

    @Test
    public void push() {
        // Given
        Element h1 = Document.get().createHElement(1);
        // When
        jsArray.push(h1);

        // Then
        assertThat(jsArray.join()).isEqualTo(",,,,<a></a>,<h1></h1>");
        assertThat(jsArray.length()).isEqualTo(6);
        assertThat(jsArray.get(jsArray.length() - 1)).isEqualTo(h1);
    }

    @Test
    public void shift() {
        // Given
        Element h1 = Document.get().createHElement(1);
        jsArray.set(0, h1);

        // When
        Element shift = jsArray.shift();

        // Then
        assertThat(shift).isEqualTo(h1);
        assertThat(jsArray.length()).isEqualTo(4);
        assertThat(jsArray.join()).isEqualTo(",,,<a></a>");
    }

    @Test
    public void unboundedGet_ReturnsNull() {
        // When
        Element unbounded = jsArray.get(100);

        // Then
        assertThat(unbounded).isNull();
    }

    @Test
    public void unshift() {
        // Given
        Element h1 = Document.get().createHElement(1);

        // When
        jsArray.unshift(h1);

        // Then
        assertThat(jsArray.length()).isEqualTo(6);
        assertThat(jsArray.join()).isEqualTo("<h1></h1>,,,,,<a></a>");
    }

}
