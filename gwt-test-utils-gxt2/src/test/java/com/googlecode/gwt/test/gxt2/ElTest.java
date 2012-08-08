package com.googlecode.gwt.test.gxt2;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.extjs.gxt.ui.client.core.El;

public class ElTest extends GwtGxtTest {

   @Test
   public void addUnitsAuto() {
      // Act
      String result = El.addUnits("auto", "px");

      // Assert
      assertEquals("auto", result);
   }

   @Test
   public void addUnitsComplete() {
      // Act
      String result = El.addUnits("350em", "%");

      // Assert
      assertEquals("350em", result);
   }

   @Test
   public void addUnitsEmpty() {
      // Act
      String result = El.addUnits("", "px");

      // Assert
      assertEquals("", result);
   }

   @Test
   public void addUnitsNoUnit() {
      // Act
      String result = El.addUnits("200", "em");

      // Assert
      assertEquals("200em", result);
   }

   @Test
   public void addUnitsNoUnitAndEmptyDefault() {
      // Act
      String result = El.addUnits("250", "");

      // Assert
      assertEquals("250px", result);
   }

   @Test
   public void addUnitsNoUnitAndNoDefault() {
      // Act
      String result = El.addUnits("250", null);

      // Assert
      assertEquals("250px", result);
   }

   @Test
   public void addUnitsNull() {
      // Act
      String result = El.addUnits(null, "px");

      // Assert
      assertEquals("", result);
   }

   @Test
   public void addUnitsUndefined() {
      // Act
      String result = El.addUnits("undefined", "px");

      // Assert
      assertEquals("", result);
   }

   @Test
   public void addUnitsWithWhitespaces() {
      // Act
      String result = El.addUnits(" 350 em ", "pt");

      // Assert
      assertEquals("350em", result);
   }
}
