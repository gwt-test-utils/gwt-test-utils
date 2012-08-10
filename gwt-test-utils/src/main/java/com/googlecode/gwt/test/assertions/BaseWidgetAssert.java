package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for all {@link Widget} assertions.
 * 
 * @author Gael Lazzari
 * 
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *           "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *           target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *           implementation</a>.&quot;
 * @param <A> the type the "actual" value.
 */
public abstract class BaseWidgetAssert<S, A extends Widget> extends BaseUIObjectAssert<S, A> {

   /**
    * Creates a new <code>{@link BaseWidgetAssert}</code>.
    * 
    * @param selfType the "self type."
    * @param actual the actual value to verify.
    */
   protected BaseWidgetAssert(Class<S> selfType, A actual) {
      super(selfType, actual);
   }

   /**
    * Verifies that the actual {@link Widget} is currently attached to the browser's document (i.e.,
    * there is an unbroken chain of widgets between this widget and the underlying browser
    * document).
    * 
    * @return this assertion object.
    * @throws AssertionError if the actual {@link UIObject} is not visible.
    */
   public S isAttached() {
      if (actual.isAttached())
         return myself;

      throw failWithMessage("should be attached");
   }

   /**
    * Verifies that the actual {@link Widget} is not currently attached to the browser's document
    * (i.e., there is a broken chain of widgets between this widget and the underlying browser
    * document).
    * 
    * @return this assertion object.
    * @throws AssertionError if the actual {@link UIObject} is visible.
    */
   public S isNotAttached() {
      if (!actual.isAttached())
         return myself;

      throw failWithMessage("should not be attached");

   }

}
