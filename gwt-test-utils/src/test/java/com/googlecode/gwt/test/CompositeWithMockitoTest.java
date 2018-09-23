package com.googlecode.gwt.test;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.CustomButton;
import com.google.gwt.user.client.ui.PushButton;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

@GwtModule("com.googlecode.gwt.test.GwtTestUtils")
public class CompositeWithMockitoTest extends GwtTestWithMockito {

    @com.googlecode.gwt.test.Mock
    private Element element;
    @Mock
    private PushButton injectedButton;

    @Test
    public void testComposite() {
        // Given
        Mockito.when(injectedButton.getElement()).thenReturn(element.cast());

        // When
        MyComposite composite = new MyComposite(injectedButton);

        // Then
        assertThat(composite.getElement()).isEqualTo(element);
    }

    public class MyComposite extends Composite {

        public MyComposite(CustomButton button) {
            initWidget(button);
        }
    }
}
