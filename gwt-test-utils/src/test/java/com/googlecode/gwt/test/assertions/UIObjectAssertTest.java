package com.googlecode.gwt.test.assertions;

import static com.googlecode.gwt.test.assertions.GwtAssertions.assertThat;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Fail.fail;

import org.junit.Test;

import com.google.gwt.user.client.ui.Anchor;
import com.googlecode.gwt.test.GwtTestTest;

public class UIObjectAssertTest extends GwtTestTest {

   @Test
   public void htmlEqualsAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setHTML("<h3>Ben Linus</h3>");

      // Act
      try {
         GwtAssertions.assertThat(a).htmlEquals("John Locke");
         fail();
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo(
                  "[Anchor's HTML] expected:<'[John Locke]'> but was:<'[<h3>Ben Linus</h3>]'>");
      }
   }

   @Test
   public void styleNameEqualsAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setStyleName("first");
      a.addStyleName("second");

      // Act
      try {
         assertThat(a).styleNameEquals("not-my-style");
         fail();
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo(
                  "[Anchor's styleName] expected:<'[not-my-style]'> but was:<'[first second]'>");
      }
   }

   @Test
   public void stylePrimaryNameEqualsAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setStylePrimaryName("primary");
      a.addStyleName("second");

      // Act
      try {
         assertThat(a).stylePrimaryNameEquals("not-my-style");
         fail();
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo(
                  "[Anchor's stylePrimaryName] expected:<'[not-my-style]'> but was:<'[primary]'>");
      }
   }

}
