package com.googlecode.gwt.test.editors;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.editors.PersonEditor.PersonDriver;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonEditorTest extends GwtTestTest {

    @Test
    public void editEmptyBean() {
        // Given
        Person Person = new Person();
        PersonEditor editor = new PersonEditor();
        PersonDriver driver = GWT.create(PersonDriver.class);
        driver.initialize(editor);

        // Test
        driver.edit(Person);

        // Then
        assertThat(editor.firstName.getValue()).isEqualTo("");
        assertThat(editor.lastName.getValue()).isEqualTo("");

        assertThat(editor.address.street().getValue()).isEqualTo("");
        assertThat(editor.address.zipWithPathEditor().getValue()).isEqualTo("");
        assertThat(editor.address.city.getValue()).isEqualTo("");
        assertThat(editor.address.stateWithPath.getValue()).isNull();
    }

    @Test
    public void editFilledBean() {
        // Given
        Person person = Person.createFilledPerson();

        PersonEditor editor = new PersonEditor();
        PersonDriver driver = GWT.create(PersonDriver.class);
        driver.initialize(editor);

        // Test
        driver.edit(person);

        // Then
        assertThat(editor.firstName.getValue()).isEqualTo("John");
        assertThat(editor.lastName.getValue()).isEqualTo("Locke");

        assertThat(editor.address.street().getValue()).isEqualTo("Avenue des Champs Elysées");
        assertThat(editor.address.zipWithPathEditor().getValue()).isEqualTo("75008");
        assertThat(editor.address.city.getValue()).isEqualTo("Paris");
        assertThat(editor.address.stateWithPath.getValue()).isEqualTo("France");
    }

    @Test
    public void flushWithData() {
        // Given
        Person person = new Person();
        PersonEditor editor = new PersonEditor();
        PersonDriver driver = GWT.create(PersonDriver.class);
        driver.initialize(editor);
        // Start editing
        driver.edit(person);

        // When : edit widget
        Browser.fillText(editor.firstName, "John");
        Browser.fillText(editor.lastName, "Locke");
        Browser.fillText(editor.address.street(), "Avenue des Champs Elysées");
        Browser.fillText(editor.address.zipWithPathEditor(), "75008");
        Browser.fillText(editor.address.city, "Paris");
        // TODO : API Browser for ValueListBox
        editor.address.stateWithPath.setValue("France", true);
        driver.flush();

        // Then
        assertThat(person.getFirstName()).isEqualTo("John");
        assertThat(person.getLastName()).isEqualTo("Locke");

        assertThat(person.getAddress().getStreet()).isEqualTo("Avenue des Champs Elysées");
        assertThat(person.getAddress().getZip()).isEqualTo("75008");
        assertThat(person.getAddress().getCity()).isEqualTo("Paris");
        assertThat(person.getAddress().getState()).isEqualTo("France");
    }

}
