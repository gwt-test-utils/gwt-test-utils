package com.googlecode.gwt.test;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@GwtModule("com.googlecode.gwt.test.GwtTestUtils")
public class WindowTest extends GwtTestWithMockito {

    @Mock
    private WindowOperationsHandler mockedHandler;

    @Before
    public void beforeWindowTest() {
        setWindowOperationsHandler(mockedHandler);
    }

    @Test
    public void alert() {
        // When
        Window.alert("this is an alert");

        // Then
        verify(mockedHandler).alert("this is an alert");
    }

    @Test
    public void confirm() {
        // Given
        when(mockedHandler.confirm("this is a confirmation")).thenReturn(true);

        // When
        boolean result = Window.confirm("this is a confirmation");

        // Then
        assertThat(result).isTrue();
    }

    @Test
    public void emptyMethods() {
        // When & Then
        Window.enableScrolling(true);
        Window.moveBy(1, 2);
        Window.moveTo(3, 4);
        Window.resizeBy(3, 6);
        Window.resizeTo(8, 9);
        Window.scrollTo(2, 4);
    }

    @Test
    public void margin() {
        // Given
        Document.get().getBody().setAttribute("style", "");

        // When
        Window.setMargin("13px");

        // Then
        assertThat(Document.get().getBody().getStyle().getMargin()).isEqualTo("13px");
        assertThat(Document.get().getBody().getAttribute("style")).isEqualTo("margin: 13px; ");
    }

    @Test
    public void open() {
        // When
        Window.open("url", "name", "features");

        // Then
        verify(mockedHandler).open("url", "name", "features");
    }

    @Test
    public void print() {
        // When
        Window.print();

        // Then
        verify(mockedHandler).print();
    }

    @Test
    public void prompt() {
        // Given
        when(mockedHandler.prompt("prompt message", "initial value")).thenReturn("mocked message");

        // When
        String result = Window.prompt("prompt message", "initial value");

        // Then
        assertThat(result).isEqualTo("mocked message");
    }

    @Test
    public void title() {
        // Given
        Document.get().setTitle("arranged title");

        // When
        Window.setTitle("my title");

        // Then
        assertThat(Window.getTitle()).isEqualTo("my title");
        assertThat(Document.get().getTitle()).isEqualTo("my title");
    }

}
