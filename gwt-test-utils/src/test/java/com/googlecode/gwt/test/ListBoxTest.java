package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.user.client.ui.ListBox;

public class ListBoxTest extends GwtTestTest {

   @Test
   public void clear() {
      // Arrange
      ListBox listBox = getListBox();
      assertEquals(2, listBox.getVisibleItemCount());
      listBox.setSelectedIndex(1);

      // Act
      listBox.clear();

      // Assert
      assertEquals(-1, listBox.getSelectedIndex());
      assertEquals(0, listBox.getItemCount());
   }

   @SuppressWarnings("deprecation")
   @Test
   public void isMultipleSelect() {
      // Arrange
      ListBox listBox = new ListBox(false);
      // Pre-Assert
      assertEquals(false, listBox.isMultipleSelect());

      // Act
      listBox.setMultipleSelect(true);

      // Assert
      assertEquals(true, listBox.isMultipleSelect());
   }

   @Test
   public void listBox() {
      // Arrange
      ListBox listBox = getListBox();

      // Act & Assert
      assertEquals(2, listBox.getVisibleItemCount());
      assertEquals(2, listBox.getItemCount());
      assertEquals("item 0", listBox.getItemText(0));
      assertEquals("item 1", listBox.getItemText(1));
   }

   @Test
   public void name() {
      // Arrange
      ListBox listBox = new ListBox(false);

      // Act
      listBox.setName("name");

      // Assert
      assertEquals("name", listBox.getName());
   }

   @Test
   public void removeItem() {
      // Arrange
      ListBox listBox = getListBox();
      // Pre-Assert
      assertEquals(2, listBox.getVisibleItemCount());

      // Act
      listBox.removeItem(0);

      // Assert
      assertEquals(1, listBox.getVisibleItemCount());
      assertEquals("item 1", listBox.getItemText(0));
   }

   @Test
   public void selected() {
      // Arrange
      ListBox listBox = getListBox();

      // Act
      listBox.setSelectedIndex(1);

      // Assert
      assertEquals("item 1", listBox.getItemText(listBox.getSelectedIndex()));
   }

   @Test
   public void selectedIndex() {
      // Arrange
      ListBox listBox = getListBox();
      // Pre-Assert
      assertEquals(-1, listBox.getSelectedIndex());

      // Act
      listBox.setSelectedIndex(1);

      // Assert
      assertEquals(1, listBox.getSelectedIndex());
   }

   @Test
   public void tabIndex() {
      // Arrange
      ListBox listBox = new ListBox(false);

      // Act
      listBox.setTabIndex(2);

      // Assert
      assertEquals(2, listBox.getTabIndex());
   }

   @Test
   public void title() {
      // Arrange
      ListBox listBox = new ListBox(false);

      // Act
      listBox.setTitle("title");

      // Assert
      assertEquals("title", listBox.getTitle());
   }

   @Test
   public void visible() {
      // Arrange
      ListBox listBox = new ListBox(false);
      // Pre-Assert
      assertEquals(true, listBox.isVisible());

      // Act
      listBox.setVisible(false);

      // Assert
      assertEquals(false, listBox.isVisible());
   }

   private ListBox getListBox() {
      ListBox listBox = new ListBox(false);
      listBox.setVisibleItemCount(2);
      listBox.addItem("item 0");
      listBox.addItem("item 1");

      return listBox;
   }

}
