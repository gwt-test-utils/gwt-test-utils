package com.googlecode.gwt.test.gxt2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.google.gwt.user.client.ui.RootPanel;

public class MyGXTAppTest extends GwtGxtTest {

   @Test
   public void onModuleLoad() {
      // Arrange
      MyGXTApp app = new MyGXTApp();

      // Act
      app.onModuleLoad();

      // Assert
      ContentPanel cp = (ContentPanel) RootPanel.get().getWidget(0);
      assertEquals(10, cp.getPosition(true).x);
      assertEquals(10, cp.getPosition(true).y);
      assertEquals("my title", cp.getTitle());
      assertEquals(250, cp.getSize().width);
      // assertEquals(140, cp.getSize().height);
      assertTrue(cp.getCollapsible());
      assertTrue(cp.getFrame());
      assertEquals("backgroundColor: white;", cp.getBodyStyle());
      assertEquals(3, cp.getHeader().getToolCount());

      ToolButton tool0 = (ToolButton) cp.getHeader().getTool(0);
      assertEquals("x-nodrag x-tool-gear x-tool x-component", tool0.getStyleName());

      ToolButton tool1 = (ToolButton) cp.getHeader().getTool(1);
      assertEquals("x-nodrag x-tool-close x-tool x-component", tool1.getStyleName());

      ToolButton tool2 = (ToolButton) cp.getHeader().getTool(2);
      assertEquals("x-nodrag x-tool-toggle x-tool x-component", tool2.getStyleName());
   }

}
