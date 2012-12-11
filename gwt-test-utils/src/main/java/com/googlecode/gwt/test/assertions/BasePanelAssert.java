package com.googlecode.gwt.test.assertions;

import static org.fest.util.Objects.areEqual;

import java.util.Iterator;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for all {@link Panel} assertions.
 * 
 * @author Gael Lazzari
 * 
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *           "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *           target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *           implementation</a>.&quot;
 * @param <A> the type of the "actual" value.
 */
public class BasePanelAssert<S extends BasePanelAssert<S, A>, A extends Panel> extends
         BaseWidgetAssert<S, A> {

   /**
    * Creates a new <code>{@link BasePanelAssert}</code>.
    * 
    * @param actual the actual value to verify.
    * @param selfType the "self type."
    */
   protected BasePanelAssert(A actual, Class<S> selfType) {
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
      int widgetCount = getWigetCount(actual);
      if (areEqual(widgetCount, expected))
         return myself;
      throw propertyComparisonFailed("WidgetCount", widgetCount, expected);
   }

   private int getWigetCount(A actual) {
      if (actual instanceof IndexedPanel) {
         return ((IndexedPanel) actual).getWidgetCount();
      } else if (actual instanceof SimplePanel) {
         return 1;
      }

      Iterator<Widget> it = actual.iterator();
      int count = 0;
      while (it.hasNext()) {
         count++;
         it.next();
      }

      return count;
   }

}
