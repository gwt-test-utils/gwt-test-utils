package com.googlecode.gwt.test.uibinder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.googlecode.gwt.test.GwtTestTest;

public class UiBinderWithLayoutPanelTest extends GwtTestTest {

   @Test
   public void content() {
      // Act
      UiBinderWithLayoutPanel panel = new UiBinderWithLayoutPanel();

      // Assert
      assertEquals(2, panel.getPanel().getWidgetCount());
      assertEquals(panel.defaultLabel, panel.getPanel().getWidget(0));
      assertEquals(panel.headerLabel, panel.getPanel().getWidget(1));
   }

}
