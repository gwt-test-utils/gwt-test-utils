package com.googlecode.gwt.test.editors;

public class Address {

    public static Address createFilledAddress() {
        Address address = new Address();
        address.street = "Avenue des Champs Elysées";
        address.zip = "75008";
        address.city = "Paris";
        address.state = "France";

        return address;
    }

    private String city;
    private String state;
    private String street;
    private String zip;

    public String getCity() {
        return this.city;
    }

    public String getState() {
        return this.state;
    }

    public String getStreet() {
        return this.street;
    }

    public String getZip() {
        return this.zip;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
