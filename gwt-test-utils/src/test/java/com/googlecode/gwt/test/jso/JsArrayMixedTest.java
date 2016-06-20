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
        // Given
        jsArrayMixed = JavaScriptObject.createArray().cast();
        assertThat(jsArrayMixed.length()).isEqualTo(0);

        // When
        jsArrayMixed.set(4, true);

        // Then
        assertThat(jsArrayMixed.length()).isEqualTo(5);
        assertThat(jsArrayMixed.getBoolean(3)).isFalse();
        assertThat(jsArrayMixed.getString(3)).isNull();

        assertThat(jsArrayMixed.getBoolean(4)).isTrue();
        assertThat(jsArrayMixed.getString(4)).isEqualTo("true");
    }

    @Test
    public void getStringElement() {
        // Given
        AnchorElement anchor = Document.get().createAnchorElement();
        anchor.setHref("http://lost.com");
        jsArrayMixed.set(2, anchor);

        // When
        String elementToString = jsArrayMixed.getString(2);

        // Then
        assertThat(elementToString).isEqualTo("<a href=\"http://lost.com\"></a>");
    }

    @Test
    public void getStringJSON() {
        // Given
        JavaScriptObject json = JsonUtils.safeEval("{test: true}");
        jsArrayMixed.set(2, json);

        // When
        String elementToString = jsArrayMixed.getString(2);

        // Then
        assertThat(elementToString).isEqualTo("{ \"test\": true }");
    }

    @Test
    public void join() {
        // When
        String join = jsArrayMixed.join();

        // Then
        assertThat(join).isEqualTo(",,,,true");
    }

    @Test
    public void join_AfterResize() {
        // Given
        jsArrayMixed.setLength(3);

        // When
        String join = jsArrayMixed.join();

        // Then
        assertThat(jsArrayMixed.length()).isEqualTo(3);
        assertThat(join).isEqualTo(",,");
    }

    @Test
    public void push() {
        // When
        jsArrayMixed.push("pushed");

        // Then
        assertThat(jsArrayMixed.length()).isEqualTo(6);
        assertThat(jsArrayMixed.join()).isEqualTo(",,,,true,pushed");
        assertThat(jsArrayMixed.getString(jsArrayMixed.length() - 1)).isEqualTo("pushed");
    }

    @Test
    public void shiftBoolean() {
        // Given
        jsArrayMixed.set(0, true);

        // When
        boolean shift = jsArrayMixed.shiftBoolean();

        // Then
        assertThat(shift).isTrue();
        assertThat(jsArrayMixed.length()).isEqualTo(4);
        assertThat(jsArrayMixed.join()).isEqualTo(",,,true");
    }

    @Test
    public void shiftInteger() {
        // Given
        jsArrayMixed.set(0, 4);

        // When
        double shift = jsArrayMixed.shiftNumber();

        // Then
        assertThat(shift).isEqualTo(4);
        assertThat(jsArrayMixed.length()).isEqualTo(4);
        assertThat(jsArrayMixed.join()).isEqualTo(",,,true");
    }

    @Test
    public void shiftObject() {
        // Given
        JavaScriptObject jso = JavaScriptObject.createObject();
        jsArrayMixed.set(0, jso);

        // When
        JavaScriptObject shift = jsArrayMixed.shiftObject();

        // Then
        assertThat(shift).isEqualTo(jso);
        assertThat(jsArrayMixed.length()).isEqualTo(4);
        assertThat(jsArrayMixed.join()).isEqualTo(",,,true");
    }

    @Test
    public void shiftString() {
        // Given
        jsArrayMixed.set(0, "shifted");

        // When
        String shift = jsArrayMixed.shiftString();

        // Then
        assertThat(shift).isEqualTo("shifted");
        assertThat(jsArrayMixed.length()).isEqualTo(4);
        assertThat(jsArrayMixed.join()).isEqualTo(",,,true");
    }

    @Test
    public void unboundedGet_Returns0() {
        // When
        boolean unboundedBoolean = jsArrayMixed.getBoolean(100);
        String unboundedString = jsArrayMixed.getString(100);
        double unboundedDouble = jsArrayMixed.getNumber(100);
        JavaScriptObject unboundedObject = jsArrayMixed.getObject(100);

        // Then
        assertThat(unboundedBoolean).isFalse();
        assertThat(unboundedString).isNull();
        assertThat(unboundedDouble).isEqualTo(0);
        assertThat(unboundedObject).isNull();
    }

    @Test
    public void unshiftBoolean() {
        // When
        jsArrayMixed.unshift(true);

        // Then
        assertThat(jsArrayMixed.length()).isEqualTo(6);
        assertThat(jsArrayMixed.join()).isEqualTo("true,,,,,true");
    }

    @Test
    public void unshiftElement() {
        // Given
        AnchorElement anchor = Document.get().createAnchorElement();
        anchor.setHref("http://lost.com");

        // When
        jsArrayMixed.unshift(anchor);

        // Then
        assertThat(jsArrayMixed.length()).isEqualTo(6);
        assertThat(jsArrayMixed.join()).isEqualTo("<a href=\"http://lost.com\"></a>,,,,,true");
    }

    @Test
    public void unshiftJSON() {
        // Given
        JavaScriptObject json = JsonUtils.safeEval("{test: true}");

        // When
        jsArrayMixed.unshift(json);

        // Then
        assertThat(jsArrayMixed.length()).isEqualTo(6);
        assertThat(jsArrayMixed.join()).isEqualTo("{ \"test\": true },,,,,true");
    }

    @Test
    public void unshiftNumber() {
        // When
        jsArrayMixed.unshift(23);

        // Then
        assertThat(jsArrayMixed.length()).isEqualTo(6);
        assertThat(jsArrayMixed.join()).isEqualTo("23.0,,,,,true");
    }

    @Test
    public void unshiftString() {
        // When
        jsArrayMixed.unshift("unshifted");

        // Then
        assertThat(jsArrayMixed.length()).isEqualTo(6);
        assertThat(jsArrayMixed.join()).isEqualTo("unshifted,,,,,true");
    }

}
