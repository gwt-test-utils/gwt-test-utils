package com.googlecode.gwt.test.uibinder;

import static com.googlecode.gwt.test.assertions.GwtAssertions.*;

import org.junit.Test;

import com.googlecode.gwt.test.GwtTestTest;

public class UiBinderUiTextTest extends GwtTestTest {

   @Test
   public void checkUiText() {
      // Arrange
      String expectedText = "Hello gwt-test-utils !\r\nThis is a test with a simple text file";
      String expectedTextFromMessages = "orange";

      // Act
      UiBinderUiText widget = new UiBinderUiText();

      // Assert
      assertThat(widget.label).textEquals(expectedText);
      assertThat(widget.msgLabel).textEquals(expectedTextFromMessages);
   }

}
