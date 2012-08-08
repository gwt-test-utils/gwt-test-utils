package com.googlecode.gwt.test.dom;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Text;
import com.googlecode.gwt.test.GwtTestTest;

public class TextTest extends GwtTestTest {

   @Test
   public void checkToString() {
      // Arrange
      Text text = Document.get().createTextNode("some text");

      // Act
      String toString = text.toString();

      // Assert
      assertEquals("'some text'", toString);

   }

}
