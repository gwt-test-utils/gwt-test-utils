package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static com.googlecode.gwt.test.assertions.GwtAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

public class WidgetAssertTest extends GwtTestTest {

    @Test
    public void hasStyle() {
        // Given
        Anchor a = new Anchor();
        a.setStyleName("first-style");
        a.addStyleName("second-style");
        a.addStyleName("third-style");

        // When
        try {
            assertThat(a).hasStyle("first-style", "second-style", "third-style");
        } catch (AssertionError e) {
            // Then
            fail("assertion should pass but failed with message : " + e.getMessage());
        }
    }

    @Test
    public void hasStyleAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setStyleName("first-style");
        a.addStyleName("second-style");
        a.addStyleName("third-style");

        // When
        try {
            assertThat(a).hasStyle("first-style", "second-style", "thrid-style", "fourth-style");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[Anchor] should have style \"thrid-style\"");
        }
    }

    @Test
    public void htmlEquals() {
        // Given
        Anchor a = new Anchor();
        a.setHTML("<h3>Ben Linus</h3>");

        // When
        try {
            GwtAssertions.assertThat(a).htmlEquals("<h3>Ben Linus</h3>");
        } catch (AssertionError e) {
            // Then
            fail("assertion should pass but failed with message : " + e.getMessage());
        }
    }

    @Test
    public void htmlEqualsAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setHTML("<h3>Ben Linus</h3>");

        // When
        try {
            GwtAssertions.assertThat(a).htmlEquals("John Locke");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo(
                    "[Anchor's HTML] expected:<\"[John Locke]\"> but was:<\"[<h3>Ben Linus</h3>]\">");
        }
    }

    @Test
    public void htmlEqualsAssertionErrorMessageWithPrefix() {
        // Given
        Anchor a = new Anchor();
        a.setHTML("<h3>Ben Linus</h3>");

        // When
        try {
            GwtAssertions.assertThat(a).withPrefix("my prefix").htmlEquals("John Locke");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo(
                    "[my prefix Anchor's HTML] expected:<\"[John Locke]\"> but was:<\"[<h3>Ben Linus</h3>]\">");
        }
    }

    @Test
    public void htmlEqualsWithAsAndCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setHTML("<h3>Ben Linus</h3>");

        // When
        try {
            GwtAssertions.assertThat(a).overridingErrorMessage("custom error message").as("my anchor").htmlEquals(
                    "John Locke");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[my anchor HTML] custom error message");
        }
    }

    @Test
    public void htmlEqualsWithAsAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setHTML("<h3>Ben Linus</h3>");

        // When
        try {
            GwtAssertions.assertThat(a).as("my anchor").htmlEquals("John Locke");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo(
                    "[my anchor HTML] expected:<\"[John Locke]\"> but was:<\"[<h3>Ben Linus</h3>]\">");
        }
    }

    @Test
    public void htmlEqualsWithCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setHTML("<h3>Ben Linus</h3>");

        // When
        try {
            GwtAssertions.assertThat(a).overridingErrorMessage("custom error message").htmlEquals(
                    "John Locke");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[Anchor's HTML] custom error message");
        }
    }

    @Test
    public void htmlEqualsIgnoreCase() {
        // Given
        Anchor a = new Anchor();
        a.setHTML("<h3>Ben Linus</h3>");

        // When & Then
        try {
            GwtAssertions.assertThat(a).htmlEqualsIgnoreCase("<h3>ben LInus</h3>");
        } catch (AssertionError e) {
            fail("assertion should pass but failed with message : " + e.getMessage());
        }
    }

    @Test
    public void htmlEqualsIgnoreCaseEmpty() {
        // Given
        Anchor a = new Anchor();
        a.setHTML("");

        // When & Then
        try {
            GwtAssertions.assertThat(a).htmlEqualsIgnoreCase("");
            GwtAssertions.assertThat(a).htmlEqualsIgnoreCase(null);
        } catch (AssertionError e) {
            fail("assertion should pass but failed with message : " + e.getMessage());
        }
    }

    @Test
    public void htmlEqualsIgnoreCaseNull() {
        // Given
        String valueHtml = null;
        Anchor a = new Anchor();
        a.setHTML(valueHtml);

        // When & Then
        try {
            GwtAssertions.assertThat(a).htmlEqualsIgnoreCase("");
            GwtAssertions.assertThat(a).htmlEqualsIgnoreCase(null);
        } catch (AssertionError e) {
            fail("assertion should pass but failed with message : " + e.getMessage());
        }
    }

    @Test
    public void htmlIgnoreCaseWithAsAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setHTML("<h3>Ada Byron</h3>");

        // When
        try {
            GwtAssertions.assertThat(a).as("my anchor").htmlEqualsIgnoreCase("<h3>Ada Lovelace</h3>");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo(
                    "[(ignore case mode)  my anchor HTML] expected:<\"<h3>Ada [Lovelace]</h3>\"> but was:<\"<h3>Ada [Byron]</h3>\">");
        }
    }

    @Test
    public void isAttached() {
        // Given
        Anchor a = new Anchor();
        RootPanel.get().add(a);

        // When & Then
        try {
            assertThat(a).isAttached();
        } catch (AssertionError e) {
            fail("assertion should pass but failed with message : " + e.getMessage());
        }
    }

    @Test
    public void isAttachedAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();

        // When
        try {
            GwtAssertions.assertThat(a).isAttached();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[Anchor] should be attached");
        }
    }

    @Test
    public void isAttachedWithAsAndCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();

        // When
        try {
            GwtAssertions.assertThat(a).as("my anchor").overridingErrorMessage("custom failure").isAttached();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[my anchor] custom failure");
        }
    }

    @Test
    public void isAttachedWithAsAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();

        // When
        try {
            GwtAssertions.assertThat(a).as("my anchor").isAttached();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[my anchor] should be attached");
        }
    }

    @Test
    public void isAttachedWithCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();

        // When
        try {
            GwtAssertions.assertThat(a).overridingErrorMessage("custom failure").isAttached();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[Anchor] custom failure");
        }
    }

    @Test
    public void isNotAttached() {
        // Given
        Anchor a = new Anchor();

        // When & Then
        try {
            GwtAssertions.assertThat(a).isNotAttached();
        } catch (AssertionError e) {
            fail("assertion should pass but failed with message : " + e.getMessage());
        }
    }

    @Test
    public void isNotAttachedAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        RootPanel.get().add(a);

        // When
        try {
            GwtAssertions.assertThat(a).isNotAttached();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[Anchor] should not be attached");
        }
    }

    @Test
    public void isNotAttachedWithAsAndCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        RootPanel.get().add(a);

        // When
        try {
            GwtAssertions.assertThat(a).as("my anchor").overridingErrorMessage("custom failure").isNotAttached();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[my anchor] custom failure");
        }
    }

    @Test
    public void isNotAttachedWithAsAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        RootPanel.get().add(a);

        // When
        try {
            GwtAssertions.assertThat(a).as("my anchor").isNotAttached();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[my anchor] should not be attached");
        }
    }

    @Test
    public void isNotAttachedWithCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        RootPanel.get().add(a);

        // When
        try {
            GwtAssertions.assertThat(a).overridingErrorMessage("custom failure").isNotAttached();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[Anchor] custom failure");
        }
    }

    @Test
    public void isNotVisible() {
        // Given
        Anchor a = new Anchor();
        a.setVisible(false);

        // When & Then
        try {
            GwtAssertions.assertThat(a).isNotVisible();
        } catch (AssertionError e) {
            fail("assertion should pass but failed with message : " + e.getMessage());
        }
    }

    @Test
    public void isNotVisibleAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();

        // When
        try {
            GwtAssertions.assertThat(a).isNotVisible();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[Anchor] should not be visible");
        }
    }

    @Test
    public void isNotVisibleWithAsAndCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();

        // When
        try {
            GwtAssertions.assertThat(a).as("my anchor").overridingErrorMessage("custom failure").isNotVisible();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[my anchor] custom failure");
        }
    }

    @Test
    public void isNotVisibleWithAsAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();

        // When
        try {
            GwtAssertions.assertThat(a).as("my anchor").isNotVisible();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[my anchor] should not be visible");
        }
    }

    @Test
    public void isNotVisibleWithCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();

        // When
        try {
            GwtAssertions.assertThat(a).overridingErrorMessage("custom failure").isNotVisible();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[Anchor] custom failure");
        }
    }

    @Test
    public void isVisible() {
        // Given
        Anchor a = new Anchor();

        // When & Then
        try {
            GwtAssertions.assertThat(a).isVisible();
        } catch (AssertionError e) {
            fail("assertion should pass but failed with message : " + e.getMessage());
        }
    }

    @Test
    public void isVisibleAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setVisible(false);

        // When
        try {
            GwtAssertions.assertThat(a).isVisible();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[Anchor] should be visible");
        }
    }

    @Test
    public void isVisibleWithAsAndCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setVisible(false);

        // When
        try {
            GwtAssertions.assertThat(a).as("my anchor").overridingErrorMessage("custom failure").isVisible();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[my anchor] custom failure");
        }
    }

    @Test
    public void isVisibleWithAsAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setVisible(false);

        // When
        try {
            GwtAssertions.assertThat(a).as("my anchor").isVisible();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[my anchor] should be visible");
        }
    }

    @Test
    public void isVisibleWithCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setVisible(false);

        // When
        try {
            GwtAssertions.assertThat(a).overridingErrorMessage("custom failure").isVisible();
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[Anchor] custom failure");
        }
    }

    @Test
    public void styleNameEquals() {
        // Given
        Anchor a = new Anchor();
        a.setStyleName("first");
        a.addStyleName("second");

        // When & Then
        try {
            assertThat(a).styleNameEquals("first second");
        } catch (AssertionError e) {
            fail("assertion should pass but failed with message : " + e.getMessage());
        }
    }

    @Test
    public void styleNameEqualsAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setStyleName("first");
        a.addStyleName("second");

        // When
        try {
            assertThat(a).styleNameEquals("not-my-style");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo(
                    "[Anchor's styleName] expected:<\"[not-my-style]\"> but was:<\"[first second]\">");
        }
    }

    @Test
    public void styleNameEqualsWithAsAndCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setStyleName("first");
        a.addStyleName("second");

        // When
        try {
            assertThat(a).as("my anchor").overridingErrorMessage("custom error message").styleNameEquals(
                    "not-my-style");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[my anchor styleName] custom error message");
        }
    }

    @Test
    public void styleNameEqualsWithAsAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setStyleName("first");
        a.addStyleName("second");

        // When
        try {
            assertThat(a).as("my anchor").styleNameEquals("not-my-style");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo(
                    "[my anchor styleName] expected:<\"[not-my-style]\"> but was:<\"[first second]\">");
        }
    }

    @Test
    public void styleNameEqualsWithCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setStyleName("first");
        a.addStyleName("second");

        // When
        try {
            assertThat(a).overridingErrorMessage("custom error message").styleNameEquals(
                    "not-my-style");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[Anchor's styleName] custom error message");
        }
    }

    @Test
    public void stylePrimaryNameEquals() {
        // Given
        Anchor a = new Anchor();
        a.setStylePrimaryName("primary");
        a.addStyleName("second");

        // When & Then
        try {
            assertThat(a).stylePrimaryNameEquals("primary");
        } catch (AssertionError e) {
            fail("assertion should pass but failed with message : " + e.getMessage());
        }
    }

    @Test
    public void stylePrimaryNameEqualsAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setStylePrimaryName("primary");
        a.addStyleName("second");

        // When
        try {
            assertThat(a).stylePrimaryNameEquals("not-my-style");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo(
                    "[Anchor's stylePrimaryName] expected:<\"[not-my-style]\"> but was:<\"[primary]\">");
        }
    }

    @Test
    public void stylePrimaryNameEqualsWithAsAndCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setStylePrimaryName("primary");
        a.addStyleName("second");

        // When
        try {
            assertThat(a).as("my anchor").overridingErrorMessage("my custom error").stylePrimaryNameEquals(
                    "not-my-style");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[my anchor stylePrimaryName] my custom error");
        }
    }

    @Test
    public void stylePrimaryNameEqualsWithAsAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setStylePrimaryName("primary");
        a.addStyleName("second");

        // When
        try {
            assertThat(a).as("my anchor").stylePrimaryNameEquals("not-my-style");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo(
                    "[my anchor stylePrimaryName] expected:<\"[not-my-style]\"> but was:<\"[primary]\">");
        }
    }

    @Test
    public void stylePrimaryNameEqualsWithCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setStylePrimaryName("primary");
        a.addStyleName("second");

        // When
        try {
            assertThat(a).overridingErrorMessage("my custom error").stylePrimaryNameEquals(
                    "not-my-style");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[Anchor's stylePrimaryName] my custom error");
        }
    }

    @Test
    public void textEquals() {
        // Given
        Anchor a = new Anchor();
        a.setText("Ben Linus");

        // When & Then
        try {
            GwtAssertions.assertThat(a).textEquals("Ben Linus");
        } catch (AssertionError e) {
            fail("assertion should pass but failed with message : " + e.getMessage());
        }
    }

    @Test
    public void textEqualsAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setText("Ben Linus");

        // When
        try {
            GwtAssertions.assertThat(a).textEquals("John Locke");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo(
                    "[Anchor's text] expected:<\"[John Locke]\"> but was:<\"[Ben Linus]\">");
        }
    }

    @Test
    public void textEqualsWithAsAndCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setText("Ben Linus");

        // When
        try {
            GwtAssertions.assertThat(a).overridingErrorMessage("custom error message").as("my anchor").textEquals(
                    "John Locke");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[my anchor text] custom error message");
        }
    }

    @Test
    public void textEqualsWithAsAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setText("Ben Linus");

        // When
        try {
            GwtAssertions.assertThat(a).as("my anchor").textEquals("John Locke");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo(
                    "[my anchor text] expected:<\"[John Locke]\"> but was:<\"[Ben Linus]\">");
        }
    }

    @Test
    public void textEqualsWithCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setText("Ben Linus");

        // When
        try {
            GwtAssertions.assertThat(a).overridingErrorMessage("custom error message").textEquals(
                    "John Locke");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[Anchor's text] custom error message");
        }
    }

    @Test
    public void titleEquals() {
        // Given
        Anchor a = new Anchor();
        a.setTitle("Lost 108");

        // When & Then
        try {
            GwtAssertions.assertThat(a).titleEquals("Lost 108");
        } catch (AssertionError e) {
            fail("assertion should pass but failed with message : " + e.getMessage());
        }
    }

    @Test
    public void titleEqualsAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setTitle("Lost 108");

        // When
        try {
            GwtAssertions.assertThat(a).titleEquals("John Locke");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo(
                    "[Anchor's title] expected:<\"[John Locke]\"> but was:<\"[Lost 108]\">");
        }
    }

    @Test
    public void titleEqualsAssertionErrorMessageWithPrefix() {
        // Given
        Anchor a = new Anchor();
        a.setTitle("Lost 108");

        // When
        try {
            GwtAssertions.assertThat(a).withPrefix("my prefix").titleEquals("John Locke");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo(
                    "[my prefix Anchor's title] expected:<\"[John Locke]\"> but was:<\"[Lost 108]\">");
        }
    }

    @Test
    public void titleEqualsWithAsAndCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setTitle("Lost 108");

        // When
        try {
            GwtAssertions.assertThat(a).overridingErrorMessage("custom error message").as("my anchor").titleEquals(
                    "John Locke");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[my anchor title] custom error message");
        }
    }

    @Test
    public void titleEqualsWithAsAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setTitle("Lost 108");

        // When
        try {
            GwtAssertions.assertThat(a).as("my anchor").titleEquals("John Locke");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo(
                    "[my anchor title] expected:<\"[John Locke]\"> but was:<\"[Lost 108]\">");
        }
    }

    @Test
    public void titleEqualsWithCustomAssertionErrorMessage() {
        // Given
        Anchor a = new Anchor();
        a.setTitle("Lost 108");

        // When
        try {
            GwtAssertions.assertThat(a).overridingErrorMessage("custom error message").titleEquals("John Locke");
            fail("AssertionError should be thrown");
        } catch (AssertionError e) {
            // Then
            assertThat(e.getMessage()).isEqualTo("[Anchor's title] custom error message");
        }
    }

}
