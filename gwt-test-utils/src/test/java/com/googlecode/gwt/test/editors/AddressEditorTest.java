package com.googlecode.gwt.test.editors;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.editors.AddressEditor.AddressDriver;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AddressEditorTest extends GwtTestTest {

    @Test
    public void editEmptyBean() {
        // Given
        Address address = new Address();
        AddressEditor editor = new AddressEditor();
        AddressDriver driver = GWT.create(AddressDriver.class);
        driver.initialize(editor);

        // Test
        driver.edit(address);

        // Then
        assertThat(editor.street().getValue()).isEqualTo("");
        assertThat(editor.zipWithPathEditor().getValue()).isEqualTo("");
        assertThat(editor.city.getValue()).isEqualTo("");
        assertThat(editor.stateWithPath.getValue()).isNull();
    }

    @Test
    public void editFilledBean() {
        // Given
        Address address = Address.createFilledAddress();

        AddressEditor editor = new AddressEditor();
        AddressDriver driver = GWT.create(AddressDriver.class);
        driver.initialize(editor);

        // Test
        driver.edit(address);

        // Then
        assertThat(editor.street().getValue()).isEqualTo("Avenue des Champs Elysées");
        assertThat(editor.zipWithPathEditor().getValue()).isEqualTo("75008");
        assertThat(editor.city.getValue()).isEqualTo("Paris");
        assertThat(editor.stateWithPath.getValue()).isEqualTo("France");
    }

    @Test
    public void flushWithData() {
        // Given
        Address address = new Address();
        AddressEditor editor = new AddressEditor();
        AddressDriver driver = GWT.create(AddressDriver.class);
        driver.initialize(editor);
        // Start editing
        driver.edit(address);

        // When : edit widget
        Browser.fillText(editor.street(), "Avenue des Champs Elysées");
        Browser.fillText(editor.zipWithPathEditor(), "75008");
        Browser.fillText(editor.city, "Paris");
        // TODO : API Browser for ValueListBox
        editor.stateWithPath.setValue("France", true);
        driver.flush();

        // Then
        assertThat(address.getStreet()).isEqualTo("Avenue des Champs Elysées");
        assertThat(address.getZip()).isEqualTo("75008");
        assertThat(address.getCity()).isEqualTo("Paris");
        assertThat(address.getState()).isEqualTo("France");
    }

}
