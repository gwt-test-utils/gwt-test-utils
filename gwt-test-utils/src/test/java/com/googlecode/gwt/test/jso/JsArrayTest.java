package com.googlecode.gwt.test.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsArrayTest extends GwtTestTest {

    private JsArray<Element> jsArray;

    @Before
    public void beforeJsArrayStringTest() {
        // Arrange
        jsArray = JavaScriptObject.createObject().cast();
        assertEquals(0, jsArray.length());

        // Act
        jsArray.set(4, Document.get().createAnchorElement());

        // Assert
        assertEquals(5, jsArray.length());
        assertNull(jsArray.get(3));
    }

    @Test
    public void join() {
        // Act
        String join = jsArray.join();

        // Assert
        assertEquals(",,,,<a></a>", join);
    }

    @Test
    public void join_AfterResize() {
        // Arrange
        jsArray.setLength(3);

        // Act
        String join = jsArray.join();

        // Assert
        assertEquals(",,", join);
        assertEquals(3, jsArray.length());
    }

    @Test
    public void push() {
        // Arrange
        Element h1 = Document.get().createHElement(1);
        // Act
        jsArray.push(h1);

        // Assert
        assertEquals(",,,,<a></a>,<h1></h1>", jsArray.join());
        assertEquals(6, jsArray.length());
        assertEquals(h1, jsArray.get(jsArray.length() - 1));
    }

    @Test
    public void shift() {
        // Arrange
        Element h1 = Document.get().createHElement(1);
        jsArray.set(0, h1);

        // Act
        Element shift = jsArray.shift();

        // Assert
        assertEquals(h1, shift);
        assertEquals(4, jsArray.length());
        assertEquals(",,,<a></a>", jsArray.join());
    }

    @Test
    public void unboundedGet_ReturnsNull() {
        // Act
        Element unbounded = jsArray.get(100);

        // Assert
        assertNull(unbounded);
    }

    @Test
    public void unshift() {
        // Arrange
        Element h1 = Document.get().createHElement(1);

        // Act
        jsArray.unshift(h1);

        // Assert
        assertEquals(6, jsArray.length());
        assertEquals("<h1></h1>,,,,,<a></a>", jsArray.join());
    }

}
