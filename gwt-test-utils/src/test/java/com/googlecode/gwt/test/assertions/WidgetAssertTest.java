package com.googlecode.gwt.test.assertions;

import static com.googlecode.gwt.test.assertions.GwtAssertions.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Fail.fail;

import org.junit.Test;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.test.GwtTestTest;

public class WidgetAssertTest extends GwtTestTest {

   @Test
   public void htmlEquals() {
      // Arrange
      Anchor a = new Anchor();
      a.setHTML("<h3>Ben Linus</h3>");

      // Act
      try {
         GwtAssertions.assertThat(a).htmlEquals("<h3>Ben Linus</h3>");
      } catch (AssertionError e) {
         fail("assertion should pass but failed with message : " + e.getMessage());
      }
   }

   @Test
   public void htmlEqualsAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setHTML("<h3>Ben Linus</h3>");

      // Act
      try {
         GwtAssertions.assertThat(a).htmlEquals("John Locke");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo(
                  "[Anchor's HTML] expected:<'[John Locke]'> but was:<'[<h3>Ben Linus</h3>]'>");
      }
   }

   @Test
   public void htmlEqualsAssertionErrorMessageWithPrefix() {
      // Arrange
      Anchor a = new Anchor();
      a.setHTML("<h3>Ben Linus</h3>");

      // Act
      try {
         GwtAssertions.assertThat(a).withPrefix("my prefix").htmlEquals("John Locke");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo(
                  "[my prefix Anchor's HTML] expected:<'[John Locke]'> but was:<'[<h3>Ben Linus</h3>]'>");
      }
   }

   @Test
   public void htmlEqualsWithAsAndCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setHTML("<h3>Ben Linus</h3>");

      // Act
      try {
         GwtAssertions.assertThat(a).overridingErrorMessage("custom error message").as("my anchor").htmlEquals(
                  "John Locke");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[my anchor HTML] custom error message");
      }
   }

   @Test
   public void htmlEqualsWithAsAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setHTML("<h3>Ben Linus</h3>");

      // Act
      try {
         GwtAssertions.assertThat(a).as("my anchor").htmlEquals("John Locke");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo(
                  "[my anchor HTML] expected:<'[John Locke]'> but was:<'[<h3>Ben Linus</h3>]'>");
      }
   }

   @Test
   public void htmlEqualsWithCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setHTML("<h3>Ben Linus</h3>");

      // Act
      try {
         GwtAssertions.assertThat(a).overridingErrorMessage("custom error message").htmlEquals(
                  "John Locke");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[Anchor's HTML] custom error message");
      }
   }

   @Test
   public void isAttached() {
      // Arrange
      Anchor a = new Anchor();
      RootPanel.get().add(a);

      // Act
      try {
         assertThat(a).isAttached();
      } catch (AssertionError e) {
         fail("assertion should pass but failed with message : " + e.getMessage());
      }
   }

   @Test
   public void isAttachedAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();

      // Act
      try {
         GwtAssertions.assertThat(a).isAttached();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[Anchor] should be attached");
      }
   }

   @Test
   public void isAttachedWithAsAndCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();

      // Act
      try {
         GwtAssertions.assertThat(a).as("my anchor").overridingErrorMessage("custom failure").isAttached();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[my anchor] custom failure");
      }
   }

   @Test
   public void isAttachedWithAsAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();

      // Act
      try {
         GwtAssertions.assertThat(a).as("my anchor").isAttached();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[my anchor] should be attached");
      }
   }

   @Test
   public void isAttachedWithCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();

      // Act
      try {
         GwtAssertions.assertThat(a).overridingErrorMessage("custom failure").isAttached();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[Anchor] custom failure");
      }
   }

   @Test
   public void isNotAttached() {
      // Arrange
      Anchor a = new Anchor();

      // Act
      try {
         GwtAssertions.assertThat(a).isNotAttached();
      } catch (AssertionError e) {
         fail("assertion should pass but failed with message : " + e.getMessage());
      }
   }

   @Test
   public void isNotAttachedAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      RootPanel.get().add(a);

      // Act
      try {
         GwtAssertions.assertThat(a).isNotAttached();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[Anchor] should not be attached");
      }
   }

   @Test
   public void isNotAttachedWithAsAndCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      RootPanel.get().add(a);

      // Act
      try {
         GwtAssertions.assertThat(a).as("my anchor").overridingErrorMessage("custom failure").isNotAttached();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[my anchor] custom failure");
      }
   }

   @Test
   public void isNotAttachedWithAsAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      RootPanel.get().add(a);

      // Act
      try {
         GwtAssertions.assertThat(a).as("my anchor").isNotAttached();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[my anchor] should not be attached");
      }
   }

   @Test
   public void isNotAttachedWithCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      RootPanel.get().add(a);

      // Act
      try {
         GwtAssertions.assertThat(a).overridingErrorMessage("custom failure").isNotAttached();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[Anchor] custom failure");
      }
   }

   @Test
   public void isNotVisible() {
      // Arrange
      Anchor a = new Anchor();
      a.setVisible(false);

      // Act
      try {
         GwtAssertions.assertThat(a).isNotVisible();
      } catch (AssertionError e) {
         fail("assertion should pass but failed with message : " + e.getMessage());
      }
   }

   @Test
   public void isNotVisibleAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();

      // Act
      try {
         GwtAssertions.assertThat(a).isNotVisible();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[Anchor] should not be visible");
      }
   }

   @Test
   public void isNotVisibleWithAsAndCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();

      // Act
      try {
         GwtAssertions.assertThat(a).as("my anchor").overridingErrorMessage("custom failure").isNotVisible();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[my anchor] custom failure");
      }
   }

   @Test
   public void isNotVisibleWithAsAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();

      // Act
      try {
         GwtAssertions.assertThat(a).as("my anchor").isNotVisible();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[my anchor] should not be visible");
      }
   }

   @Test
   public void isNotVisibleWithCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();

      // Act
      try {
         GwtAssertions.assertThat(a).overridingErrorMessage("custom failure").isNotVisible();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[Anchor] custom failure");
      }
   }

   @Test
   public void isVisible() {
      // Arrange
      Anchor a = new Anchor();

      // Act
      try {
         GwtAssertions.assertThat(a).isVisible();
      } catch (AssertionError e) {
         fail("assertion should pass but failed with message : " + e.getMessage());
      }
   }

   @Test
   public void isVisibleAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setVisible(false);

      // Act
      try {
         GwtAssertions.assertThat(a).isVisible();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[Anchor] should be visible");
      }
   }

   @Test
   public void isVisibleWithAsAndCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setVisible(false);

      // Act
      try {
         GwtAssertions.assertThat(a).as("my anchor").overridingErrorMessage("custom failure").isVisible();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[my anchor] custom failure");
      }
   }

   @Test
   public void isVisibleWithAsAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setVisible(false);

      // Act
      try {
         GwtAssertions.assertThat(a).as("my anchor").isVisible();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[my anchor] should be visible");
      }
   }

   @Test
   public void isVisibleWithCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setVisible(false);

      // Act
      try {
         GwtAssertions.assertThat(a).overridingErrorMessage("custom failure").isVisible();
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[Anchor] custom failure");
      }
   }

   @Test
   public void styleNameEquals() {
      // Arrange
      Anchor a = new Anchor();
      a.setStyleName("first");
      a.addStyleName("second");

      // Act
      try {
         assertThat(a).styleNameEquals("first second");
      } catch (AssertionError e) {
         fail("assertion should pass but failed with message : " + e.getMessage());
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
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo(
                  "[Anchor's styleName] expected:<'[not-my-style]'> but was:<'[first second]'>");
      }
   }

   @Test
   public void styleNameEqualsWithAsAndCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setStyleName("first");
      a.addStyleName("second");

      // Act
      try {
         assertThat(a).as("my anchor").overridingErrorMessage("custom error message").styleNameEquals(
                  "not-my-style");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[my anchor styleName] custom error message");
      }
   }

   @Test
   public void styleNameEqualsWithAsAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setStyleName("first");
      a.addStyleName("second");

      // Act
      try {
         assertThat(a).as("my anchor").styleNameEquals("not-my-style");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo(
                  "[my anchor styleName] expected:<'[not-my-style]'> but was:<'[first second]'>");
      }
   }

   @Test
   public void styleNameEqualsWithCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setStyleName("first");
      a.addStyleName("second");

      // Act
      try {
         assertThat(a).overridingErrorMessage("custom error message").styleNameEquals(
                  "not-my-style");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[Anchor's styleName] custom error message");
      }
   }

   @Test
   public void stylePrimaryNameEquals() {
      // Arrange
      Anchor a = new Anchor();
      a.setStylePrimaryName("primary");
      a.addStyleName("second");

      // Act
      try {
         assertThat(a).stylePrimaryNameEquals("primary");
      } catch (AssertionError e) {
         fail("assertion should pass but failed with message : " + e.getMessage());
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
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo(
                  "[Anchor's stylePrimaryName] expected:<'[not-my-style]'> but was:<'[primary]'>");
      }
   }

   @Test
   public void stylePrimaryNameEqualsWithAsAndCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setStylePrimaryName("primary");
      a.addStyleName("second");

      // Act
      try {
         assertThat(a).as("my anchor").overridingErrorMessage("my custom error").stylePrimaryNameEquals(
                  "not-my-style");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[my anchor stylePrimaryName] my custom error");
      }
   }

   @Test
   public void stylePrimaryNameEqualsWithAsAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setStylePrimaryName("primary");
      a.addStyleName("second");

      // Act
      try {
         assertThat(a).as("my anchor").stylePrimaryNameEquals("not-my-style");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo(
                  "[my anchor stylePrimaryName] expected:<'[not-my-style]'> but was:<'[primary]'>");
      }
   }

   @Test
   public void stylePrimaryNameEqualsWithCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setStylePrimaryName("primary");
      a.addStyleName("second");

      // Act
      try {
         assertThat(a).overridingErrorMessage("my custom error").stylePrimaryNameEquals(
                  "not-my-style");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[Anchor's stylePrimaryName] my custom error");
      }
   }

   @Test
   public void textEquals() {
      // Arrange
      Anchor a = new Anchor();
      a.setText("Ben Linus");

      // Act
      try {
         GwtAssertions.assertThat(a).textEquals("Ben Linus");
      } catch (AssertionError e) {
         fail("assertion should pass but failed with message : " + e.getMessage());
      }
   }

   @Test
   public void textEqualsAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setText("Ben Linus");

      // Act
      try {
         GwtAssertions.assertThat(a).textEquals("John Locke");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo(
                  "[Anchor's text] expected:<'[John Locke]'> but was:<'[Ben Linus]'>");
      }
   }

   @Test
   public void textEqualsWithAsAndCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setText("Ben Linus");

      // Act
      try {
         GwtAssertions.assertThat(a).overridingErrorMessage("custom error message").as("my anchor").textEquals(
                  "John Locke");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[my anchor text] custom error message");
      }
   }

   @Test
   public void textEqualsWithAsAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setText("Ben Linus");

      // Act
      try {
         GwtAssertions.assertThat(a).as("my anchor").textEquals("John Locke");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo(
                  "[my anchor text] expected:<'[John Locke]'> but was:<'[Ben Linus]'>");
      }
   }

   @Test
   public void textEqualsWithCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setText("Ben Linus");

      // Act
      try {
         GwtAssertions.assertThat(a).overridingErrorMessage("custom error message").textEquals(
                  "John Locke");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[Anchor's text] custom error message");
      }
   }

   @Test
   public void titleEquals() {
      // Arrange
      Anchor a = new Anchor();
      a.setTitle("Lost 108");

      // Act
      try {
         GwtAssertions.assertThat(a).titleEquals("Lost 108");
      } catch (AssertionError e) {
         fail("assertion should pass but failed with message : " + e.getMessage());
      }
   }

   @Test
   public void titleEqualsAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setTitle("Lost 108");

      // Act
      try {
         GwtAssertions.assertThat(a).titleEquals("John Locke");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo(
                  "[Anchor's title] expected:<'[John Locke]'> but was:<'[Lost 108]'>");
      }
   }

   @Test
   public void titleEqualsAssertionErrorMessageWithPrefix() {
      // Arrange
      Anchor a = new Anchor();
      a.setTitle("Lost 108");

      // Act
      try {
         GwtAssertions.assertThat(a).withPrefix("my prefix").titleEquals("John Locke");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo(
                  "[my prefix Anchor's title] expected:<'[John Locke]'> but was:<'[Lost 108]'>");
      }
   }

   @Test
   public void titleEqualsWithAsAndCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setTitle("Lost 108");

      // Act
      try {
         GwtAssertions.assertThat(a).overridingErrorMessage("custom error message").as("my anchor").titleEquals(
                  "John Locke");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[my anchor title] custom error message");
      }
   }

   @Test
   public void titleEqualsWithAsAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setTitle("Lost 108");

      // Act
      try {
         GwtAssertions.assertThat(a).as("my anchor").titleEquals("John Locke");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo(
                  "[my anchor title] expected:<'[John Locke]'> but was:<'[Lost 108]'>");
      }
   }

   @Test
   public void titleEqualsWithCustomAssertionErrorMessage() {
      // Arrange
      Anchor a = new Anchor();
      a.setTitle("Lost 108");

      // Act
      try {
         GwtAssertions.assertThat(a).overridingErrorMessage("custom error message").titleEquals(
                  "John Locke");
         fail("AssertionError should be thrown");
      } catch (AssertionError e) {
         assertThat(e.getMessage()).isEqualTo("[Anchor's title] custom error message");
      }
   }

}
