package com.googlecode.gwt.test.json;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.googlecode.gwt.test.GwtTestTest;

public class JSONObjectTest extends GwtTestTest {

   @Test
   public void containsKey() {
      // Arrange
      String json = "{\"string\": \"json string\", \"int\": 3.0}";
      JSONObject o = JSONParser.parseStrict(json).isObject();

      // Test
      assertThat(o.containsKey("string")).isTrue();
      assertThat(o.containsKey("int")).isTrue();
      assertThat(o.containsKey("does-not-exist")).isFalse();

   }

   @Test
   public void parseLenient() {
      // Arrange
      String json = "{string: 'json string', \"int\": 3, float: 3.1415, \"bool\": true, 'array': [1, 33.7, \"l33t\"], \"object\": {\"int\": 4, \"array\": [5, 6, 7]}}";

      // Act
      JSONObject o = JSONParser.parseLenient(json).isObject();
      JSONString string = (JSONString) o.get("string");
      JSONNumber number = (JSONNumber) o.get("int");
      JSONNumber fl = (JSONNumber) o.get("float");
      JSONBoolean bool = (JSONBoolean) o.get("bool");
      JSONArray array = (JSONArray) o.get("array");
      JSONObject object = (JSONObject) o.get("object");

      // Assert
      assertEquals("json string", string.stringValue());
      assertEquals(3.0, number.doubleValue(), 0);
      assertEquals(3.1415, fl.doubleValue(), 0);
      assertTrue(bool.booleanValue());
      // array
      assertEquals(1.0, ((JSONNumber) array.get(0)).doubleValue(), 0);
      assertEquals(33.7, ((JSONNumber) array.get(1)).doubleValue(), 0);
      assertEquals("l33t", ((JSONString) array.get(2)).stringValue());
      // object
      assertEquals(4.0, ((JSONNumber) object.get("int")).doubleValue(), 0);
      assertEquals(3, ((JSONArray) object.get("array")).size());

   }

   @Test
   public void parseStrict() {
      // Arrange
      String json = "{\"string\": \"json string\", \"int\": 3.0, \"float\": 3.1415, \"bool\": true, \"array\": [1, 33.7, \"l33t\"], \"object\": {\"int\": 4, \"array\": [5, 6, 7]}}";

      // Act
      JSONObject o = JSONParser.parseStrict(json).isObject();
      JSONString string = (JSONString) o.get("string");
      JSONNumber number = (JSONNumber) o.get("int");
      JSONNumber fl = (JSONNumber) o.get("float");
      JSONBoolean bool = (JSONBoolean) o.get("bool");
      JSONArray array = (JSONArray) o.get("array");
      JSONObject object = (JSONObject) o.get("object");

      // Assert
      assertEquals("json string", string.stringValue());
      assertEquals(3.0, number.doubleValue(), 0);
      assertEquals(3.1415, fl.doubleValue(), 0);
      assertTrue(bool.booleanValue());
      // array
      assertEquals(1.0, ((JSONNumber) array.get(0)).doubleValue(), 0);
      assertEquals(33.7, ((JSONNumber) array.get(1)).doubleValue(), 0);
      assertEquals("l33t", ((JSONString) array.get(2)).stringValue());
      // object
      assertEquals(4.0, ((JSONNumber) object.get("int")).doubleValue(), 0);
      assertEquals(3, ((JSONArray) object.get("array")).size());

      // toString
      assertEquals(
               "{\"string\":\"json string\", \"int\":3, \"float\":3.1415, \"bool\":true, \"array\":[1,33.7,\"l33t\"], \"object\":{\"int\":4, \"array\":[5,6,7]}}",
               o.toString());

   }

}
