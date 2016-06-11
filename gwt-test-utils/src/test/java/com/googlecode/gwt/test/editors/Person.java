package com.googlecode.gwt.test.editors;

public class Person {

    public static Person createFilledPerson() {
        Person person = new Person();
        person.firstName = "John";
        person.lastName = "Locke";
        person.address.setStreet("Avenue des Champs Elysées");
        person.address.setZip("75008");
        person.address.setCity("Paris");
        person.address.setState("France");

        return person;
    }

    private final Address address = new Address();
    private String firstName;
    private String id;

    private String lastName;

    public Address getAddress() {
        return this.address;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getId() {
        return this.id;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
