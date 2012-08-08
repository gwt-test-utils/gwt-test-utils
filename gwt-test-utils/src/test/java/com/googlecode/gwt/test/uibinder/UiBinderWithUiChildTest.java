package com.googlecode.gwt.test.uibinder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.user.client.ui.HTML;
import com.googlecode.gwt.test.GwtTestTest;

public class UiBinderWithUiChildTest extends GwtTestTest {

   @Test
   public void instanciation() {
      // Act
      UiBinderWithUiChild w = new UiBinderWithUiChild();

      // Assert
      assertEquals(2, w.getWidgetWithUiChild().labelCount());
      assertEquals("My first child label", w.getWidgetWithUiChild().getLabel(0).getText());
      assertEquals("My second child label", w.getWidgetWithUiChild().getLabel(1).getText());
      assertEquals(1, w.getWidgetWithUiChild().customWidgetCount());
      HTML html = (HTML) w.getWidgetWithUiChild().getCustomChild(0);
      assertEquals("My child HTML", html.getHTML());
   }

}
