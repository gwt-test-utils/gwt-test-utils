package com.googlecode.gwt.test.gxt2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.extjs.gxt.ui.client.widget.DataList;
import com.extjs.gxt.ui.client.widget.DataListItem;

// TODO: complete tests..
@SuppressWarnings("deprecation")
public class DataListTest extends GwtGxtTest {

   private DataList dataList;

   @Before
   public void beforeDataList() {
      dataList = new DataList();
   }

   @Test
   public void selectedItem() {
      // Arrange
      DataListItem item0 = new DataListItem("item 0");
      dataList.add(item0);
      DataListItem item1 = new DataListItem("item 1");
      dataList.add(item1);

      // Act
      dataList.setSelectedItem(item1);

      // Assert
      assertEquals(item1, dataList.getSelectedItem());

   }

}
