package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.FocusWidget;

/**
 * Base class for all {@link FocusWidget} assertions.
 * 
 * @author Gael Lazzari
 * 
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *           "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *           target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *           implementation</a>.&quot;
 * @param <A> the type the "actual" value.
 */
public class BaseFocusWidgetAssert<S, A extends FocusWidget> extends BaseWidgetAssert<S, A> {

   protected BaseFocusWidgetAssert(Class<S> selfType, A actual) {
      super(selfType, actual);
   }

   /**
    * Verifies that the actual {@link FocusWidget} is currently enabled.
    * 
    * @return this assertion object.
    * @throws AssertionError if the actual {@link FocusWidget} is not enabled.
    */
   public S isEnabled() {
      if (actual.isEnabled())
         return myself;

      throw failWithMessage("should be enabled");
   }

   /**
    * Verifies that the actual {@link FocusWidget} is not currently enabled.
    * 
    * @return this assertion object.
    * @throws AssertionError if the actual {@link FocusWidget} is enabled.
    */
   public S isNotEnabled() {
      if (!actual.isEnabled())
         return myself;

      throw failWithMessage("should not be enabled");
   }

}
