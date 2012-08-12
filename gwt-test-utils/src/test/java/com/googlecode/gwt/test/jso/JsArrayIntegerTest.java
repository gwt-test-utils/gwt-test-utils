package com.googlecode.gwt.test.jso;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import com.googlecode.gwt.test.GwtTestTest;

public class JsArrayIntegerTest extends GwtTestTest {

   private JsArrayInteger jsArrayInteger;

   @Before
   public void beforeJsArrayIntegerTest() {
      // Arrange
      jsArrayInteger = JavaScriptObject.createArray().cast();
      assertThat(jsArrayInteger.length()).isEqualTo(0);

      // Act
      jsArrayInteger.set(4, 23);

      // Assert
      assertThat(jsArrayInteger.length()).isEqualTo(5);
      assertThat(jsArrayInteger.get(3)).isEqualTo(0);
   }

   @Test
   public void join() {
      // Act
      String join = jsArrayInteger.join();

      // Assert
      assertThat(join).isEqualTo(",,,,23");
   }

   @Test
   public void join_AfterResize() {
      // Arrange
      jsArrayInteger.setLength(3);

      // Act
      String join = jsArrayInteger.join();

      // Assert
      assertThat(jsArrayInteger.length()).isEqualTo(3);
      assertThat(join).isEqualTo(",,");
   }

   @Test
   public void push() {
      // Act
      jsArrayInteger.push(42);

      // Assert

      assertThat(jsArrayInteger.length()).isEqualTo(6);
      assertThat(jsArrayInteger.join()).isEqualTo(",,,,23,42");
      assertThat(jsArrayInteger.get(jsArrayInteger.length() - 1)).isEqualTo(42);
   }

   @Test
   public void shift() {
      // Arrange
      jsArrayInteger.set(0, 2);

      // Act
      double shift = jsArrayInteger.shift();

      // Assert
      assertThat(shift).isEqualTo(2d);
      assertThat(jsArrayInteger.length()).isEqualTo(4);
      assertThat(jsArrayInteger.join()).isEqualTo(",,,23");
   }

   @Test
   public void unboundedGet_Returns0() {
      // Act
      double unbounded = jsArrayInteger.get(100);

      // Assert
      assertThat(unbounded).isEqualTo(0);
   }

   @Test
   public void unshift() {
      // Act
      jsArrayInteger.unshift(8);

      // Assert
      assertThat(jsArrayInteger.length()).isEqualTo(6);
      assertThat(jsArrayInteger.join()).isEqualTo("8,,,,,23");
   }
}
