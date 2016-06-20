package com.googlecode.gwt.test.json;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONString;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONArrayTest extends GwtTestTest {

    @Test
    public void addElements() {
        // Given
        JSONArray jsonArray = new JSONArray();
        JSONString string = new JSONString("myString");
        JSONBoolean bool = JSONBoolean.getInstance(true);
        // Preconditions
        assertThat(jsonArray.size()).isEqualTo(0);

        // When
        jsonArray.set(0, string);
        jsonArray.set(1, bool);

        // Then
        assertThat(jsonArray.size()).isEqualTo(2);
        assertThat(jsonArray.get(0)).isEqualTo(string);
        assertThat(jsonArray.get(1)).isEqualTo(bool);
        assertThat(jsonArray.get(2)).isNull();
        assertThat(jsonArray.get(-1)).isNull();
    }

    @Test
    public void addElements_unbounded() {
        // Given
        JSONArray jsonArray = new JSONArray();
        JSONString string = new JSONString("myString");
        JSONBoolean bool = JSONBoolean.getInstance(true);
        // Preconditions
        assertThat(jsonArray.size()).isEqualTo(0);

        // When
        jsonArray.set(0, string);
        jsonArray.set(2, bool);

        // Then
        assertThat(jsonArray.size()).isEqualTo(3);
        assertThat(jsonArray.get(0)).isEqualTo(string);
        assertThat(jsonArray.get(1)).isNull();
        assertThat(jsonArray.get(2)).isEqualTo(bool);
        assertThat(jsonArray.get(3)).isNull();
    }

}
