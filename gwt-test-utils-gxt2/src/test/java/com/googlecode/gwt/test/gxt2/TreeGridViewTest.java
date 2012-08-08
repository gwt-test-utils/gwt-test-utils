package com.googlecode.gwt.test.gxt2;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.extjs.gxt.ui.client.widget.treegrid.TreeGridView;

// TODO: complete tests..
public class TreeGridViewTest extends GwtGxtTest {

   @Test
   public void autoFill() {
      // Arrange
      TreeGridView view = new TreeGridView();

      // Act
      view.setAutoFill(true);

      // Assert
      assertTrue(view.isAutoFill());
   }

}
