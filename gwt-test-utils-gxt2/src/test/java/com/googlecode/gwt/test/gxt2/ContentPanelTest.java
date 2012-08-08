package com.googlecode.gwt.test.gxt2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;

// TODO: complete tests..
public class ContentPanelTest extends GwtGxtTest {

   private ContentPanel panel;

   @Before
   public void beforeContentPanel() {
      panel = new ContentPanel();
   }

   @Test
   public void parent() {
      // Arrange
      Button b = new Button();

      // Act 1
      panel.add(b);

      // Assert 1
      assertEquals(panel, b.getParent());

      // Act 2
      panel.removeAll();

      // Assert 2
      assertEquals(0, panel.getItemCount());
      assertNull(b.getParent());
   }

}
