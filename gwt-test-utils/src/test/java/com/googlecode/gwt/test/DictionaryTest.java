package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.gwt.i18n.client.Dictionary;

public class DictionaryTest extends GwtTestTest {

   @Test
   public void checkToString() {
      // Arrange
      addDictionaryEntries("toString", createDictionaryEntries());

      // Act
      String toString = Dictionary.getDictionary("toString").toString();

      // Assert
      assertEquals("Dictionary toString", toString);
   }

   @Test
   public void get() {
      // Arrange
      addDictionaryEntries("get", createDictionaryEntries());

      // Act
      String name = Dictionary.getDictionary("get").get("name");
      String description = Dictionary.getDictionary("get").get("description");

      // Assert
      assertEquals("gwt-test-utils", name);
      assertEquals("An awesome GWT testing tool ;-)", description);
   }

   @Test
   public void keySet() {
      // Arrange
      addDictionaryEntries("keySet", createDictionaryEntries());

      // Act
      Set<String> keySet = Dictionary.getDictionary("keySet").keySet();

      // Assert
      assertEquals(2, keySet.size());
      assertTrue(keySet.contains("name"));
      assertTrue(keySet.contains("description"));
   }

   @Test
   public void values() {
      // Arrange
      addDictionaryEntries("values", createDictionaryEntries());

      // Act
      Collection<String> values = Dictionary.getDictionary("values").values();

      // Assert
      assertEquals(2, values.size());
      assertTrue(values.contains("gwt-test-utils"));
      assertTrue(values.contains("An awesome GWT testing tool ;-)"));
   }

   private Map<String, String> createDictionaryEntries() {

      Map<String, String> entries = new HashMap<String, String>();
      entries.put("name", "gwt-test-utils");
      entries.put("description", "An awesome GWT testing tool ;-)");

      return entries;
   }
}
