package com.googlecode.gwt.test.uibinder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.googlecode.gwt.test.GwtTestTest;

public class UiBinderWithProvidedWidgetsTest extends GwtTestTest {

   @Test
   public void instanciation() {
      // Act
      UiBinderWithProvidedWidgets w = new UiBinderWithProvidedWidgets();

      assertEquals("my first label", w.firstProvidedLabel.getText());
      assertEquals("first custom text setup in ui.xml", w.firstProvidedLabel.getCustomText());
      assertEquals("first provided string", w.firstProvidedLabel.providedString);

      assertEquals("my second label", w.secondProvidedLabel.getText());
      assertEquals("second custom text setup in ui.xml", w.secondProvidedLabel.getCustomText());
      assertEquals("second provided string", w.secondProvidedLabel.providedString);
   }
}
