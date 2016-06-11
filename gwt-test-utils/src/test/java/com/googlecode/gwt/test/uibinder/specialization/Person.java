package com.googlecode.gwt.test.uibinder.specialization;

public class Person {

    private final String text;

    Person(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
