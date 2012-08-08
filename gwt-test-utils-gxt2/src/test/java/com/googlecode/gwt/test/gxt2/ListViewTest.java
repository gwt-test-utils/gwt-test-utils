package com.googlecode.gwt.test.gxt2;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.widget.ListView;

// TODO: complete tests..
public class ListViewTest extends GwtGxtTest {

   @Test
   public void simpleTemplate() {
      // Arrange
      ListView<ModelData> list = new ListView<ModelData>();

      // Act
      list.setSimpleTemplate("<ul><li>1</li><li>2</li></ul>");

      // Assert
      assertNotNull(list.getTemplate());
   }

}
