package com.googlecode.gwt.test.editors;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.editors.PersonEditor.PersonDriver;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PersonEditorTest extends GwtTestTest {

    @Test
    public void editEmptyBean() {
        // Arrange
        Person Person = new Person();
        PersonEditor editor = new PersonEditor();
        PersonDriver driver = GWT.create(PersonDriver.class);
        driver.initialize(editor);

        // Test
        driver.edit(Person);

        // Assert
        assertEquals("", editor.firstName.getValue());
        assertEquals("", editor.lastName.getValue());

        assertEquals("", editor.address.street().getValue());
        assertEquals("", editor.address.zipWithPathEditor().getValue());
        assertEquals("", editor.address.city.getValue());
        assertNull(editor.address.stateWithPath.getValue());
    }

    @Test
    public void editFilledBean() {
        // Arrange
        Person person = Person.createFilledPerson();

        PersonEditor editor = new PersonEditor();
        PersonDriver driver = GWT.create(PersonDriver.class);
        driver.initialize(editor);

        // Test
        driver.edit(person);

        // Assert
        assertEquals("John", editor.firstName.getValue());
        assertEquals("Locke", editor.lastName.getValue());

        assertEquals("Avenue des Champs Elysées", editor.address.street().getValue());
        assertEquals("75008", editor.address.zipWithPathEditor().getValue());
        assertEquals("Paris", editor.address.city.getValue());
        assertEquals("France", editor.address.stateWithPath.getValue());
    }

    @Test
    public void flushWithData() {
        // Arrange
        Person person = new Person();
        PersonEditor editor = new PersonEditor();
        PersonDriver driver = GWT.create(PersonDriver.class);
        driver.initialize(editor);
        // Start editing
        driver.edit(person);

        // Act : edit widget
        Browser.fillText(editor.firstName, "John");
        Browser.fillText(editor.lastName, "Locke");
        Browser.fillText(editor.address.street(), "Avenue des Champs Elysées");
        Browser.fillText(editor.address.zipWithPathEditor(), "75008");
        Browser.fillText(editor.address.city, "Paris");
        // TODO : API Browser for ValueListBox
        editor.address.stateWithPath.setValue("France", true);
        driver.flush();

        // Assert
        assertEquals("John", person.getFirstName());
        assertEquals("Locke", person.getLastName());

        assertEquals("Avenue des Champs Elysées", person.getAddress().getStreet());
        assertEquals("75008", person.getAddress().getZip());
        assertEquals("Paris", person.getAddress().getCity());
        assertEquals("France", person.getAddress().getState());
    }

}
