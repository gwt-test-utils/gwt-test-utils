package com.googlecode.gwt.test.uibinder;

import static com.googlecode.gwt.test.assertions.GwtAssertions.assertThat;

import org.junit.Test;

import com.googlecode.gwt.test.GwtTestTest;

public class UiBinderUiTextTest extends GwtTestTest {

   @Test
   public void checkUiText() {
      // Arrange
      String expectedText = "Hello gwt-test-utils !\r\nThis is a test with a simple text file";

      // Act
      UiBinderUiText widget = new UiBinderUiText();

      // Assert
      assertThat(widget.label).textEquals(expectedText);
   }

}
