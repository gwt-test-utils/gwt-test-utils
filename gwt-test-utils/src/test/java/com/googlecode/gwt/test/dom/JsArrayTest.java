package com.googlecode.gwt.test.dom;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.googlecode.gwt.test.GwtTestTest;

public class JsArrayTest extends GwtTestTest {

   @Test
   public void push() {
      // Arrange
      JavaScriptObject jso = JavaScriptObject.createObject();
      JsArray<JavaScriptObject> array = JavaScriptObject.createArray().cast();

      // Act
      array.push(jso);

      // Assert
      assertThat(array.length()).isEqualTo(1);
      assertThat(array.get(0)).isEqualTo(jso);
   }

}
