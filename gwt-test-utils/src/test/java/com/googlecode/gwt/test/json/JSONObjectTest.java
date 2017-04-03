package com.googlecode.gwt.test.json;

import com.google.gwt.json.client.*;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class JSONObjectTest extends GwtTestTest {

    @Test
    public void containsKey() {
        // Given
        String json = "{\"string\": \"json string\", \"int\": 3.0}";
        JSONObject o = JSONParser.parseStrict(json).isObject();

        // Test
        assertThat(o.containsKey("string")).isTrue();
        assertThat(o.containsKey("int")).isTrue();
        assertThat(o.containsKey("does-not-exist")).isFalse();

    }

    @Test
    public void parseArray()
    {
        String json = "[2,3,4,5,6]";
        JSONArray o = JSONParser.parseStrict(json).isArray();
        assertThat(o.size()).isEqualTo(5);
    }

    @Test
    public void parseLenient() {
        // Given
        String json = "{string: 'json string', \"int\": 3, float: 3.1415, \"bool\": true, 'array': [1, 33.7, \"l33t\"], \"object\": {\"int\": 4, \"array\": [5, 6, 7]}}";

        // When
        JSONObject o = JSONParser.parseLenient(json).isObject();
        JSONString string = (JSONString) o.get("string");
        JSONNumber number = (JSONNumber) o.get("int");
        JSONNumber fl = (JSONNumber) o.get("float");
        JSONBoolean bool = (JSONBoolean) o.get("bool");
        JSONArray array = (JSONArray) o.get("array");
        JSONObject object = (JSONObject) o.get("object");

        // Then
        assertThat(string.stringValue()).isEqualTo("json string");
        assertThat(number.doubleValue()).isCloseTo(3.0, within(new Double(0)));
        assertThat(fl.doubleValue()).isCloseTo(3.1415, within(new Double(0)));
        assertThat(bool.booleanValue()).isTrue();
        // array
        assertThat(((JSONNumber) array.get(0)).doubleValue()).isCloseTo(1.0, within(new Double(0)));
        assertThat(((JSONNumber) array.get(1)).doubleValue()).isCloseTo(33.7, within(new Double(0)));
        assertThat(((JSONString) array.get(2)).stringValue()).isEqualTo("l33t");
        // object
        assertThat(((JSONNumber) object.get("int")).doubleValue()).isCloseTo(4.0, within(new Double(0)));
        assertThat(((JSONArray) object.get("array")).size()).isEqualTo(3);

    }

    @Test
    public void parseStrict() {
        // Given
        String json = "{\"string\": \"json string\", \"int\": 3.0, \"float\": 3.1415, \"bool\": true, \"array\": [1, 33.7, \"l33t\"], \"object\": {\"int\": 4, \"array\": [5, 6, 7]}}";

        // When
        JSONObject o = JSONParser.parseStrict(json).isObject();
        JSONString string = (JSONString) o.get("string");
        JSONNumber number = (JSONNumber) o.get("int");
        JSONNumber fl = (JSONNumber) o.get("float");
        JSONBoolean bool = (JSONBoolean) o.get("bool");
        JSONArray array = (JSONArray) o.get("array");
        JSONObject object = (JSONObject) o.get("object");

        // Then
        assertThat(string.stringValue()).isEqualTo("json string");
        assertThat(number.doubleValue()).isCloseTo(3.0, within(new Double(0)));
        assertThat(fl.doubleValue()).isCloseTo(3.1415, within(new Double(0)));
        assertThat(bool.booleanValue()).isTrue();
        // array
        assertThat(((JSONNumber) array.get(0)).doubleValue()).isCloseTo(1.0, within(new Double(0)));
        assertThat(((JSONNumber) array.get(1)).doubleValue()).isCloseTo(33.7, within(new Double(0)));
        assertThat(((JSONString) array.get(2)).stringValue()).isEqualTo("l33t");
        // object
        assertThat(((JSONNumber) object.get("int")).doubleValue()).isCloseTo(4.0, within(new Double(0)));
        assertThat(((JSONArray) object.get("array")).size()).isEqualTo(3);

        // toString
        assertThat(o.toString()).isEqualTo("{\"string\":\"json string\", \"int\":3, \"float\":3.1415, \"bool\":true, \"array\":[1,33.7,\"l33t\"], \"object\":{\"int\":4, \"array\":[5,6,7]}}");

    }

}
