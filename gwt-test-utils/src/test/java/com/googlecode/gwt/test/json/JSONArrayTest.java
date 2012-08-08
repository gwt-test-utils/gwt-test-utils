package com.googlecode.gwt.test.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONString;
import com.googlecode.gwt.test.GwtTestTest;

public class JSONArrayTest extends GwtTestTest {

   @Test
   public void addElements() {
      // Arrange
      JSONArray jsonArray = new JSONArray();
      JSONString string = new JSONString("myString");
      JSONBoolean bool = JSONBoolean.getInstance(true);
      // Pre-Assert
      assertEquals(0, jsonArray.size());

      // Act
      jsonArray.set(0, string);
      jsonArray.set(1, bool);

      // Assert
      assertEquals(2, jsonArray.size());
      assertEquals(string, jsonArray.get(0));
      assertEquals(bool, jsonArray.get(1));
      assertNull(jsonArray.get(2));
      assertNull(jsonArray.get(-1));
   }

   @Test
   public void addElements_unbounded() {
      // Arrange
      JSONArray jsonArray = new JSONArray();
      JSONString string = new JSONString("myString");
      JSONBoolean bool = JSONBoolean.getInstance(true);
      // Pre-Assert
      assertEquals(0, jsonArray.size());

      // Act
      jsonArray.set(0, string);
      jsonArray.set(2, bool);

      // Assert
      assertEquals(3, jsonArray.size());
      assertEquals(string, jsonArray.get(0));
      assertNull(jsonArray.get(1));
      assertEquals(bool, jsonArray.get(2));
      assertNull(jsonArray.get(3));
   }

}
