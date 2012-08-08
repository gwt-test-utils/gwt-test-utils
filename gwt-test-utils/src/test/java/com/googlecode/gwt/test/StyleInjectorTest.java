package com.googlecode.gwt.test;

import org.junit.Test;

import com.google.gwt.dom.client.StyleInjector;

public class StyleInjectorTest extends GwtTestTest {

   @Test
   public void inject() {
      // Assert no expection is thrown
      StyleInjector.inject(".test{color:red;}");
      StyleInjector.inject(".testImmediate{color:green;}", true);
   }

   @Test
   public void injectAtEnd() {
      // Assert no expection is thrown
      StyleInjector.injectAtEnd(".test{color:red;}");
      StyleInjector.injectAtEnd(".testImmediate{color:green;}", true);
   }

   @Test
   public void injectAtStart() {
      // Assert no expection is thrown
      StyleInjector.injectAtStart(".test{color:red;}");
      StyleInjector.injectAtStart(".testImmediate{color:green;}", true);
   }

}
