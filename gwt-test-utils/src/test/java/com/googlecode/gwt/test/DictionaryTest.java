package com.googlecode.gwt.test;

import com.google.gwt.i18n.client.Dictionary;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DictionaryTest extends GwtTestTest {

    @Test
    public void checkToString() {
        // Given
        addDictionaryEntries("toString", createDictionaryEntries());

        // When
        String toString = Dictionary.getDictionary("toString").toString();

        // Then
        assertThat(toString).isEqualTo("Dictionary toString");
    }

    @Test
    public void get() {
        // Given
        addDictionaryEntries("get", createDictionaryEntries());

        // When
        String name = Dictionary.getDictionary("get").get("name");
        String description = Dictionary.getDictionary("get").get("description");

        // Then
        assertThat(name).isEqualTo("gwt-test-utils");
        assertThat(description).isEqualTo("An awesome GWT testing tool ;-)");
    }

    @Test
    public void keySet() {
        // Given
        addDictionaryEntries("keySet", createDictionaryEntries());

        // When
        Set<String> keySet = Dictionary.getDictionary("keySet").keySet();

        // Then
        assertThat(keySet).hasSize(2);
        assertThat(keySet.contains("name")).isTrue();
        assertThat(keySet.contains("description")).isTrue();
    }

    @Test
    public void values() {
        // Given
        addDictionaryEntries("values", createDictionaryEntries());

        // When
        Collection<String> values = Dictionary.getDictionary("values").values();

        // Then
        assertThat(values).hasSize(2);
        assertThat(values.contains("gwt-test-utils")).isTrue();
        assertThat(values.contains("An awesome GWT testing tool ;-)")).isTrue();
    }

    private Map<String, String> createDictionaryEntries() {

        Map<String, String> entries = new HashMap<>();
        entries.put("name", "gwt-test-utils");
        entries.put("description", "An awesome GWT testing tool ;-)");

        return entries;
    }
}
