package com.googlecode.gwt.test.uibinder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.googlecode.gwt.test.GwtTestTest;

public class UiBinderWithMenuBarTest extends GwtTestTest {

   @Test
   public void uiBinderWithMenuBar() {
      // Arrange
      UiBinderWithMenuBar panel = new UiBinderWithMenuBar();

      // Act
      RootLayoutPanel.get().add(panel);

      // Assert
      assertEquals(0, panel.getMenu().getItemIndex(panel.menuItem1));
      assertEquals(panel.getMenu(), panel.menuItem1.getParentMenu());
      assertEquals(1, panel.getMenu().getItemIndex(panel.menuItem2));
      assertEquals(panel.getMenu(), panel.menuItem2.getParentMenu());
      assertEquals(2, panel.getMenu().getItemIndex(panel.menuItem3));
      assertEquals(panel.getMenu(), panel.menuItem3.getParentMenu());

      assertEquals(panel.menu1, panel.menuItem1.getSubMenu());
      assertEquals(panel.menu2, panel.menuItem2.getSubMenu());
      assertEquals(panel.menu3, panel.menuItem3.getSubMenu());

      assertEquals(0, panel.menu1.getItemIndex(panel.subMenuItem1));
      assertEquals(panel.menu1, panel.subMenuItem1.getParentMenu());
      assertEquals(1, panel.menu2.getItemIndex(panel.subMenuItem2));
      assertEquals(panel.menu2, panel.subMenuItem2.getParentMenu());
      assertEquals(2, panel.menu3.getItemIndex(panel.subMenuItem3));
      assertEquals(panel.menu3, panel.subMenuItem3.getParentMenu());
   }

}
