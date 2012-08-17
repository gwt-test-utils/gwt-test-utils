package com.googlecode.gwt.test.assertions;

import static org.fest.util.Objects.areEqual;

import com.google.gwt.user.client.ui.ComplexPanel;

/**
 * Base class for all {@link ComplexPanel} assertions.
 * 
 * @author Gael Lazzari
 * 
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *           "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *           target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *           implementation</a>.&quot;
 * @param <A> the type of the "actual" value.
 */
public class BaseComplexPanelAssert<S extends BaseComplexPanelAssert<S, A>, A extends ComplexPanel>
         extends BaseWidgetAssert<S, A> {

   /**
    * Creates a new <code>{@link BaseComplexPanelAssert}</code>.
    * 
    * @param actual the actual value to verify.
    * @param selfType the "self type."
    */
   protected BaseComplexPanelAssert(A actual, Class<S> selfType) {
      super(actual, selfType);
   }

   /**
    * Verifies that the actual {@link ComplexPanel} child widget count is equal to the given one.
    * 
    * @param expected the expected widget count.
    * @return this assertion object.
    * @throws AssertionError if the actual widget count is not equal to the given one.
    * 
    * @see ComplexPanel#getWidgetCount()
    */
   public S widgetCountEquals(int expected) {
      int widgetCount = actual.getWidgetCount();
      if (areEqual(widgetCount, expected))
         return myself;
      throw propertyComparisonFailed("WidgetCount", widgetCount, expected);
   }

}
