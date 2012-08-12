package com.googlecode.gwt.test.jso;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;
import com.googlecode.gwt.test.GwtTestTest;

public class JsArrayNumberTest extends GwtTestTest {

   private JsArrayNumber jsArrayNumber;

   @Before
   public void beforeJsArrayIntegerTest() {
      // Arrange
      jsArrayNumber = JavaScriptObject.createArray().cast();
      assertThat(jsArrayNumber.length()).isEqualTo(0);

      // Act
      jsArrayNumber.set(4, 23);

      // Assert
      assertThat(jsArrayNumber.length()).isEqualTo(5);
      assertThat(jsArrayNumber.get(3)).isEqualTo(0);
   }

   @Test
   public void join() {
      // Act
      String join = jsArrayNumber.join();

      // Assert
      assertThat(join).isEqualTo(",,,,23.0");
   }

   @Test
   public void join_AfterResize() {
      // Arrange
      jsArrayNumber.setLength(3);

      // Act
      String join = jsArrayNumber.join();

      // Assert
      assertThat(jsArrayNumber.length()).isEqualTo(3);
      assertThat(join).isEqualTo(",,");
   }

   @Test
   public void push() {
      // Act
      jsArrayNumber.push(42);

      // Assert

      assertThat(jsArrayNumber.length()).isEqualTo(6);
      assertThat(jsArrayNumber.join()).isEqualTo(",,,,23.0,42.0");
      assertThat(jsArrayNumber.get(jsArrayNumber.length() - 1)).isEqualTo(42);
   }

   @Test
   public void shift() {
      // Arrange
      jsArrayNumber.set(0, 2);

      // Act
      double shift = jsArrayNumber.shift();

      // Assert
      assertThat(shift).isEqualTo(2d);
      assertThat(jsArrayNumber.length()).isEqualTo(4);
      assertThat(jsArrayNumber.join()).isEqualTo(",,,23.0");
   }

   @Test
   public void unboundedGet_Returns0() {
      // Act
      double unbounded = jsArrayNumber.get(100);

      // Assert
      assertThat(unbounded).isEqualTo(0);
   }

   @Test
   public void unshift() {
      // Act
      jsArrayNumber.unshift(8);

      // Assert
      assertThat(jsArrayNumber.length()).isEqualTo(6);
      assertThat(jsArrayNumber.join()).isEqualTo("8.0,,,,,23.0");
   }
}
