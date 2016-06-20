package com.googlecode.gwt.test.dom;

import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.googlecode.gwt.test.GwtTestTest;
import org.easymock.EasyMock;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class MockingOverlayInnerTypeTest extends GwtTestTest {

    @Test
    public void withEasyMock() {
        // Act
        NativePreviewEvent event = EasyMock.createMock(NativePreviewEvent.class);

        // Assert
        assertThat(event).isNotNull();
    }

    @Test
    public void withMockito() {
        // Arrange
        // see MockNamePatcher

        // Act
        NativePreviewEvent event = Mockito.mock(NativePreviewEvent.class);

        // Assert
        assertThat(event).isNotNull();
    }

}
