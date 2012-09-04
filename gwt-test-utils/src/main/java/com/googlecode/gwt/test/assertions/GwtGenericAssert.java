package com.googlecode.gwt.test.assertions;

import static java.lang.String.format;
import static org.fest.assertions.error.ShouldBeEqual.shouldBeEqual;

import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.description.Description;
import org.fest.assertions.error.BasicErrorMessageFactory;
import org.fest.assertions.internal.Failures;

import com.googlecode.gwt.test.utils.GwtReflectionUtils;

/**
 * Template for all gwt-test-utils assertions.
 * 
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *           "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *           target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *           implementation</a>.&quot;
 * @param <A> the type of the "actual" value.
 * 
 * @author Gael Lazzari
 * 
 */
public abstract class GwtGenericAssert<S extends GwtGenericAssert<S, A>, A> extends
         AbstractAssert<S, A> {

   protected final GwtWritableAssertionInfo gwtInfo;

   private final Failures failures = Failures.instance();

   /**
    * Creates a new <code>{@link GwtGenericAssert}</code>.
    * 
    * @param actual the actual value to verify.
    * @param selfType the "self type."
    */
   protected GwtGenericAssert(A actual, Class<S> selfType) {
      super(actual, selfType);

      // hack because fest-assert "info" is not configurable..
      gwtInfo = new GwtWritableAssertionInfo();
      GwtReflectionUtils.setPrivateFieldValue(this, "info", gwtInfo);
   }

   /**
    * Sets the description of this object.
    * 
    * @param description the new description to set.
    * @param args the args used to fill description as in {@link String#format(String, Object...)}.
    * @return {@code this} object.
    * @throws NullPointerException if the description is {@code null}.
    * @see #describedAs(String)
    */
   public S as(String description, Object... args) {
      return describedAs(format(description, args));
   }

   /**
    * Prefixes the assertion {@link Description} with a raw prefix string.
    * 
    * @param prefix the error message prefix.
    * @return this assertion object.
    */
   public S withPrefix(String prefix) {
      this.gwtInfo.prefix(prefix);
      return myself;
   }

   /**
    * Returns a <code>{@link AssertionError}</code> describing an assertion failure. A default
    * description is set if the default error message is not overrided and a custom description is
    * not applied. In both case, the resulting description would be prefixed by the message
    * eventually supplied through {@link GwtGenericAssert#withPrefix(String)}.
    * 
    * @param format the format string.
    * @param arguments arguments referenced by the format specifiers in the format string.
    * 
    * @return a {@code AssertionError} describing the assertion failure based on the supplied
    *         message.
    */
   protected AssertionError failWithMessage(String format, Object... arguments) {
      GwtWritableAssertionInfo info = new GwtWritableAssertionInfo();
      info.prefix(gwtInfo.prefix());
      info.overridingErrorMessage(gwtInfo.overridingErrorMessage());

      Description d = gwtInfo.superDescription();
      String newDescription = d != null ? d.value() : this.actual.getClass().getSimpleName();
      info.description(newDescription);

      return failures.failure(info, new BasicErrorMessageFactory(format, arguments));
   }

   /**
    * Returns a <code>{@link AssertionError}</code> describing a property comparison failure. A
    * default description is set if the default error message is not overrided and a custom
    * description is not applied. In both case, the resulting description would be prefixed by the
    * message eventually supplied through {@link GwtGenericAssert#withPrefix(String)}.
    * 
    * @param propertyName the compared property name
    * @param actual the actual value.
    * @param expected the expected value.
    * @return a {@code AssertionError} describing the comparison failure.
    */
   protected AssertionError propertyComparisonFailed(String propertyName, Object actual,
            Object expected) {
      GwtWritableAssertionInfo info = new GwtWritableAssertionInfo();
      info.prefix(gwtInfo.prefix());
      info.overridingErrorMessage(gwtInfo.overridingErrorMessage());

      Description d = gwtInfo.superDescription();
      String newDescription = d != null ? d.value() + " " + propertyName
               : this.actual.getClass().getSimpleName() + "'s " + propertyName;
      info.description(newDescription);

      return failures.failure(info, shouldBeEqual(actual, expected));

   }

}
