package com.googlecode.gwt;

import java.util.LinkedList;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

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
