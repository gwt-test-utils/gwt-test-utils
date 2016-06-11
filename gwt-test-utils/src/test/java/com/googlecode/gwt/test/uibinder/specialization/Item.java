package com.googlecode.gwt.test.uibinder.specialization;

class Item {

    private final String text;

    Item(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
