package com.googlecode.gwt.test.uibinder;

import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static com.googlecode.gwt.test.assertions.GwtAssertions.assertThat;

public class UiBinderUiTextTest extends GwtTestTest {

    @Test
    public void checkUiText() {
        // Given
        String expectedText = "Hello gwt-test-utils !\r\nThis is a test with a simple text file";
        String expectedTextFromMessages = "orange";

        // When
        UiBinderUiText widget = new UiBinderUiText();

        // Then
        assertThat(widget.label).textEquals(expectedText);
        assertThat(widget.msgLabel).textEquals(expectedTextFromMessages);
    }

}
