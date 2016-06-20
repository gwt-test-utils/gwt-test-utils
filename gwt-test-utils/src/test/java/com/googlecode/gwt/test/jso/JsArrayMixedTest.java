package com.googlecode.gwt.test.jso;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsArrayMixedTest extends GwtTestTest {

    private JsArrayMixed jsArrayMixed;

    @Before
    public void beforeJsArrayIntegerTest() {
        // Arrange
        jsArrayMixed = JavaScriptObject.createArray().cast();
        assertThat(jsArrayMixed.length()).isEqualTo(0);

        // Act
        jsArrayMixed.set(4, true);

        // Assert
        assertThat(jsArrayMixed.length()).isEqualTo(5);
        assertThat(jsArrayMixed.getBoolean(3)).isFalse();
        assertThat(jsArrayMixed.getString(3)).isNull();

        assertThat(jsArrayMixed.getBoolean(4)).isTrue();
        assertThat(jsArrayMixed.getString(4)).isEqualTo("true");
    }

    @Test
    public void getStringElement() {
        // Arrange
        AnchorElement anchor = Document.get().createAnchorElement();
        anchor.setHref("http://lost.com");
        jsArrayMixed.set(2, anchor);

        // Act
        String elementToString = jsArrayMixed.getString(2);

        // Assert
        assertThat(elementToString).isEqualTo("<a href=\"http://lost.com\"></a>");
    }

    @Test
    public void getStringJSON() {
        // Arrange
        JavaScriptObject json = JsonUtils.safeEval("{test: true}");
        jsArrayMixed.set(2, json);

        // Act
        String elementToString = jsArrayMixed.getString(2);

        // Assert
        assertThat(elementToString).isEqualTo("{ \"test\": true }");
    }

    @Test
    public void join() {
        // Act
        String join = jsArrayMixed.join();

        // Assert
        assertThat(join).isEqualTo(",,,,true");
    }

    @Test
    public void join_AfterResize() {
        // Arrange
        jsArrayMixed.setLength(3);

        // Act
        String join = jsArrayMixed.join();

        // Assert
        assertThat(jsArrayMixed.length()).isEqualTo(3);
        assertThat(join).isEqualTo(",,");
    }

    @Test
    public void push() {
        // Act
        jsArrayMixed.push("pushed");

        // Assert
        assertThat(jsArrayMixed.length()).isEqualTo(6);
        assertThat(jsArrayMixed.join()).isEqualTo(",,,,true,pushed");
        assertThat(jsArrayMixed.getString(jsArrayMixed.length() - 1)).isEqualTo("pushed");
    }

    @Test
    public void shiftBoolean() {
        // Arrange
        jsArrayMixed.set(0, true);

        // Act
        boolean shift = jsArrayMixed.shiftBoolean();

        // Assert
        assertThat(shift).isTrue();
        assertThat(jsArrayMixed.length()).isEqualTo(4);
        assertThat(jsArrayMixed.join()).isEqualTo(",,,true");
    }

    @Test
    public void shiftInteger() {
        // Arrange
        jsArrayMixed.set(0, 4);

        // Act
        double shift = jsArrayMixed.shiftNumber();

        // Assert
        assertThat(shift).isEqualTo(4);
        assertThat(jsArrayMixed.length()).isEqualTo(4);
        assertThat(jsArrayMixed.join()).isEqualTo(",,,true");
    }

    @Test
    public void shiftObject() {
        // Arrange
        JavaScriptObject jso = JavaScriptObject.createObject();
        jsArrayMixed.set(0, jso);

        // Act
        JavaScriptObject shift = jsArrayMixed.shiftObject();

        // Assert
        assertThat(shift).isEqualTo(jso);
        assertThat(jsArrayMixed.length()).isEqualTo(4);
        assertThat(jsArrayMixed.join()).isEqualTo(",,,true");
    }

    @Test
    public void shiftString() {
        // Arrange
        jsArrayMixed.set(0, "shifted");

        // Act
        String shift = jsArrayMixed.shiftString();

        // Assert
        assertThat(shift).isEqualTo("shifted");
        assertThat(jsArrayMixed.length()).isEqualTo(4);
        assertThat(jsArrayMixed.join()).isEqualTo(",,,true");
    }

    @Test
    public void unboundedGet_Returns0() {
        // Act
        boolean unboundedBoolean = jsArrayMixed.getBoolean(100);
        String unboundedString = jsArrayMixed.getString(100);
        double unboundedDouble = jsArrayMixed.getNumber(100);
        JavaScriptObject unboundedObject = jsArrayMixed.getObject(100);

        // Assert
        assertThat(unboundedBoolean).isFalse();
        assertThat(unboundedString).isNull();
        assertThat(unboundedDouble).isEqualTo(0);
        assertThat(unboundedObject).isNull();
    }

    @Test
    public void unshiftBoolean() {
        // Act
        jsArrayMixed.unshift(true);

        // Assert
        assertThat(jsArrayMixed.length()).isEqualTo(6);
        assertThat(jsArrayMixed.join()).isEqualTo("true,,,,,true");
    }

    @Test
    public void unshiftElement() {
        // Arrange
        AnchorElement anchor = Document.get().createAnchorElement();
        anchor.setHref("http://lost.com");

        // Act
        jsArrayMixed.unshift(anchor);

        // Assert
        assertThat(jsArrayMixed.length()).isEqualTo(6);
        assertThat(jsArrayMixed.join()).isEqualTo("<a href=\"http://lost.com\"></a>,,,,,true");
    }

    @Test
    public void unshiftJSON() {
        // Arrange
        JavaScriptObject json = JsonUtils.safeEval("{test: true}");

        // Act
        jsArrayMixed.unshift(json);

        // Assert
        assertThat(jsArrayMixed.length()).isEqualTo(6);
        assertThat(jsArrayMixed.join()).isEqualTo("{ \"test\": true },,,,,true");
    }

    @Test
    public void unshiftNumber() {
        // Act
        jsArrayMixed.unshift(23);

        // Assert
        assertThat(jsArrayMixed.length()).isEqualTo(6);
        assertThat(jsArrayMixed.join()).isEqualTo("23.0,,,,,true");
    }

    @Test
    public void unshiftString() {
        // Act
        jsArrayMixed.unshift("unshifted");

        // Assert
        assertThat(jsArrayMixed.length()).isEqualTo(6);
        assertThat(jsArrayMixed.join()).isEqualTo("unshifted,,,,,true");
    }

}
