package com.googlecode.gwt;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import java.util.LinkedList;

public class MockValueChangeHandler<T> implements ValueChangeHandler<T> {

    LinkedList<T> reccorded = new LinkedList<T>();

    public int getCallCount() {
        return reccorded.size();
    }

    public T getLast() {
        return reccorded.getLast();
    }

    public void onValueChange(ValueChangeEvent<T> event) {
        reccorded.add(event.getValue());
    }
}
