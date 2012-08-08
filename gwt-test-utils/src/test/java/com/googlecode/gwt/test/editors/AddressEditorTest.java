package com.googlecode.gwt.test.editors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.editors.AddressEditor.AddressDriver;
import com.googlecode.gwt.test.utils.events.Browser;

public class AddressEditorTest extends GwtTestTest {

   @Test
   public void editEmptyBean() {
      // Arrange
      Address address = new Address();
      AddressEditor editor = new AddressEditor();
      AddressDriver driver = GWT.create(AddressDriver.class);
      driver.initialize(editor);

      // Test
      driver.edit(address);

      // Assert
      assertEquals("", editor.street().getValue());
      assertEquals("", editor.zipWithPathEditor().getValue());
      assertEquals("", editor.city.getValue());
      assertNull(editor.stateWithPath.getValue());
   }

   @Test
   public void editFilledBean() {
      // Arrange
      Address address = Address.createFilledAddress();

      AddressEditor editor = new AddressEditor();
      AddressDriver driver = GWT.create(AddressDriver.class);
      driver.initialize(editor);

      // Test
      driver.edit(address);

      // Assert
      assertEquals("Avenue des Champs Elysées", editor.street().getValue());
      assertEquals("75008", editor.zipWithPathEditor().getValue());
      assertEquals("Paris", editor.city.getValue());
      assertEquals("France", editor.stateWithPath.getValue());
   }

   @Test
   public void flushWithData() {
      // Arrange
      Address address = new Address();
      AddressEditor editor = new AddressEditor();
      AddressDriver driver = GWT.create(AddressDriver.class);
      driver.initialize(editor);
      // Start editing
      driver.edit(address);

      // Act : edit widget
      Browser.fillText(editor.street(), "Avenue des Champs Elysées");
      Browser.fillText(editor.zipWithPathEditor(), "75008");
      Browser.fillText(editor.city, "Paris");
      // TODO : API Browser for ValueListBox
      editor.stateWithPath.setValue("France", true);
      driver.flush();

      // Assert
      assertEquals("Avenue des Champs Elysées", address.getStreet());
      assertEquals("75008", address.getZip());
      assertEquals("Paris", address.getCity());
      assertEquals("France", address.getState());
   }

}
