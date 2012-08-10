package com.googlecode.gwt.test.assertions;

import org.fest.assertions.BasicDescription;
import org.fest.assertions.Description;
import org.fest.assertions.GenericAssert;

public abstract class GwtGenericAssert<S, A> extends GenericAssert<S, A> {

   private String prefix;

   protected GwtGenericAssert(Class<S> selfType, A actual) {
      super(selfType, actual);
   }

   @Override
   public S as(Description description) {
      description = (prefix != null) ? new BasicDescription(prefix + description.value())
               : description;
      return super.as(description);
   }

   @Override
   public S as(String description) {
      description = (prefix != null) ? prefix + description : description;
      return super.as(description);
   }

   public S withPrefix(String prefix) {
      this.prefix = prefix;
      return as(prefix + rawDescription().value());
   }

}
