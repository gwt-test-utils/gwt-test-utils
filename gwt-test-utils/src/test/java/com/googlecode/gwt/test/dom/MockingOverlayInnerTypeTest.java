package com.googlecode.gwt.test.dom;

import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.googlecode.gwt.test.GwtTestTest;
import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class MockingOverlayInnerTypeTest extends GwtTestTest {

    @Test
    public void withEasyMock() {
        // When
        NativePreviewEvent event = EasyMock.createMock(NativePreviewEvent.class);

        // Then
        assertThat(event).isNotNull();
    }

    // FIXME : enable this test
    @Ignore
    @Test
    public void withMockito() {
        // Given
        // see MockNamePatcher

        // When
        NativePreviewEvent event = Mockito.mock(NativePreviewEvent.class);

        // Then
        assertThat(event).isNotNull();
    }

}
