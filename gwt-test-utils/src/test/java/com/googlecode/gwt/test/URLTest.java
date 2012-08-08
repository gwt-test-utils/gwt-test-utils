package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.http.client.URL;

public class URLTest extends GwtTestTest {

   @Test
   public void encodePathSegment() {
      // Arrange
      String decodedURLComponent = "this\\is\\encoded%2B";

      // Act
      String encoded = URL.encodePathSegment(decodedURLComponent);

      assertEquals("this%5Cis%5Cencoded%252B", encoded);
   }

   @Test
   public void encodeQueryString() {
      // Arrange
      String stringToEncode = "test";

      // Act & Assert
      assertEquals(stringToEncode, URL.encodeQueryString(stringToEncode));
   }

}
