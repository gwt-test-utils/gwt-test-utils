package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.UIObject;

/**
 * Entry point for assertion methods for different GWT types. Each method in this class is a static
 * factory for the type-specific assertion objects. The purpose of this class is to make test code
 * more readable, following <strong>fest-assert</strong> principles.
 * 
 * @author Gael Lazzari
 */
public class GwtAssertions {

   /**
    * Creates a new instance of <code>{@link UIObjectAssert}</code>.
    * 
    * @param actual the value to be the target of the assertions methods.
    * @return the created assertion object.
    */
   public static UIObjectAssert assertThat(UIObject actual) {
      return new UIObjectAssert(actual);
   }

}
