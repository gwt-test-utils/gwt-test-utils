package com.googlecode.gwt.test.assertions;

import static org.fest.assertions.ComparisonFailureFactory.comparisonFailure;
import static org.fest.assertions.Formatting.format;
import static org.fest.util.Objects.areEqual;

import org.fest.assertions.Description;

import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.gwt.test.utils.WidgetUtils;

public class UIObjectAssert extends GwtGenericAssert<UIObjectAssert, UIObject> {

   protected UIObjectAssert(UIObject actual) {
      super(UIObjectAssert.class, actual);
   }

   public UIObjectAssert htmlEquals(String expected) {
      String html = HasHTML.class.isInstance(actual) ? ((HasHTML) actual).getHTML()
               : actual.getElement().getInnerHTML();
      if (areEqual(html, expected))
         return this;
      throw propertyComparisonFailed("HTML", html, expected);
   }

   /**
    * Verifies that the actual stylePrimaryName is equal to the given one.
    * 
    * @param expected the given stylePrimaryName to compare the stylePrimaryName value to.
    * @return this assertion object.
    * @throws AssertionError if the actual stylePrimaryName is not equal to the given one.
    */
   public UIObjectAssert isVisible() {
      if (WidgetUtils.isWidgetVisible(actual))
         return this;
      failIfCustomMessageIsSet();
      throw failure(format(rawDescription(), "should be visible"));
   }

   /**
    * Verifies that the actual styleName is equal to the given one.
    * 
    * @param expected the given styleName to compare the styleName value to.
    * @return this assertion object.
    * @throws AssertionError if the actual styleName is not equal to the given one.
    */
   public UIObjectAssert styleNameEquals(String expected) {
      String styleName = actual.getStyleName();
      if (areEqual(styleName, expected))
         return this;
      throw propertyComparisonFailed("styleName", styleName, expected);
   }

   /**
    * Verifies that the actual stylePrimaryName is equal to the given one.
    * 
    * @param expected the given stylePrimaryName to compare the stylePrimaryName value to.
    * @return this assertion object.
    * @throws AssertionError if the actual stylePrimaryName is not equal to the given one.
    */
   public UIObjectAssert stylePrimaryNameEquals(String expected) {
      String stylePrimaryName = actual.getStylePrimaryName();
      if (areEqual(stylePrimaryName, expected))
         return this;
      throw propertyComparisonFailed("stylePrimaryName", stylePrimaryName, expected);
   }

   public UIObjectAssert textEquals(String expected) {
      String text = HasText.class.isInstance(actual) ? ((HasText) actual).getText()
               : actual.getElement().getInnerText();
      if (areEqual(text, expected))
         return this;
      throw propertyComparisonFailed("text", actual, expected);
   }

   /**
    * Returns a <code>{@link AssertionError}</code> describing a UIObject property comparison
    * failure.
    * 
    * @param propertyName the compared UIObject property name
    * @param actual the actual value.
    * @param expected the expected value.
    * @return a {@code AssertionError} describing the comparison failure.
    */
   protected AssertionError propertyComparisonFailed(String propertyName, Object actual,
            Object expected) {
      Description d = rawDescription();
      String message = d != null ? d.value() + " " + propertyName
               : this.actual.getClass().getSimpleName() + "'s " + propertyName;
      AssertionError comparisonFailure = comparisonFailure(message, expected, actual);
      if (comparisonFailure != null)
         return comparisonFailure;
      return failure(format("[%s] expected:<%s> but was:<%s>", propertyName, expected, actual));
   }
}
