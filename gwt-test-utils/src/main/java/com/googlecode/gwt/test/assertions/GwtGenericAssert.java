package com.googlecode.gwt.test.assertions;

import static org.fest.assertions.ComparisonFailureFactory.comparisonFailure;
import static org.fest.assertions.Formatting.format;

import org.fest.assertions.Description;
import org.fest.assertions.Fail;
import org.fest.assertions.GenericAssert;

/**
 * Template for all gwt-test-utils assertions.
 * 
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *           "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *           target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *           implementation</a>.&quot;
 * @param <A> the type the "actual" value.
 * 
 * @author Gael Lazzari
 * 
 */
public abstract class GwtGenericAssert<S, A> extends GenericAssert<S, A> {

   private String prefix;

   /**
    * Creates a new <code>{@link GwtGenericAssert}</code>.
    * 
    * @param selfType the "self type."
    * @param actual the actual value to verify.
    */
   protected GwtGenericAssert(Class<S> selfType, A actual) {
      super(selfType, actual);
   }

   /**
    * Prefixes the assertion {@link Description} with a raw prefix string.
    * 
    * @param prefix
    * @return
    */
   public S withPrefix(String prefix) {
      this.prefix = prefix;
      return myself;
   }

   /**
    * Returns a <code>{@link AssertionError}</code> describing an assertion failure. A default
    * description is set if the default error message is not overrided and a custom description is
    * not applied. In both case, the resulting description would be prefixed by the message
    * eventually supplied through {@link GwtGenericAssert#withPrefix(String)}.
    * 
    * @param message The error message
    * @return a {@code AssertionError} describing the assertion failure based on the supplied
    *         message.
    */
   protected AssertionError failWithMessage(String message) {

      if (customErrorMessage() != null) {
         return failure(customErrorMessage());
      }

      Description d = rawDescription();
      String as = d != null ? d.value() : this.actual.getClass().getSimpleName();

      return Fail.failure(format(getPrefix() + as) + message);
   }

   /**
    * If set through {@link GwtGenericAssert#withPrefix(String)}, returns the trimed prefix where a
    * space character is appended, returns an empty string otherwise.
    * 
    * @return The computed prefix value
    */
   protected final String getPrefix() {
      return (prefix != null) ? prefix.trim() + " " : "";
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

      if (customErrorMessage() != null) {
         return failure(customErrorMessage());
      }

      Description d = rawDescription();
      String message = d != null ? d.value() + " " + propertyName
               : this.actual.getClass().getSimpleName() + "'s " + propertyName;
      AssertionError comparisonFailure = comparisonFailure(getPrefix() + message, expected, actual);

      if (comparisonFailure != null)
         return comparisonFailure;

      return Fail.failure("[" + getPrefix()
               + format("%s] expected:<%s> but was:<%s>", message, expected, actual));
   }

}
