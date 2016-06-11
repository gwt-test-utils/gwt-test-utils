package com.googlecode.gwt.test;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WindowTest extends GwtTestTest {

    private final WindowOperationsHandler mockedHandler = EasyMock.createStrictMock(WindowOperationsHandler.class);
    ;

    @Test
    public void alert() {
        // Arrange
        mockedHandler.alert(EasyMock.eq("this is an alert"));
        EasyMock.expectLastCall();
        EasyMock.replay(mockedHandler);

        // Act
        Window.alert("this is an alert");

        // Assert
        EasyMock.verify(mockedHandler);
    }

    @Before
    public void beforeWindowTest() {
        EasyMock.reset(mockedHandler);

        setWindowOperationsHandler(mockedHandler);
    }

    @Test
    public void confirm() {
        // Arrange
        mockedHandler.confirm(EasyMock.eq("this is a confirmation"));
        EasyMock.expectLastCall().andReturn(true);
        EasyMock.replay(mockedHandler);

        // Act
        boolean result = Window.confirm("this is a confirmation");

        // Assert
        EasyMock.verify(mockedHandler);
        assertTrue(result);
    }

    @Test
    public void emptyMethods() {
        // Act & Assert
        Window.enableScrolling(true);
        Window.moveBy(1, 2);
        Window.moveTo(3, 4);
        Window.resizeBy(3, 6);
        Window.resizeTo(8, 9);
        Window.scrollTo(2, 4);
    }

    @Test
    public void margin() {
        // Arrange
        Document.get().getBody().setAttribute("style", "");

        // Act
        Window.setMargin("13px");

        // Assert
        assertEquals("13px", Document.get().getBody().getStyle().getMargin());
        assertEquals("margin: 13px; ", Document.get().getBody().getAttribute("style"));
    }

    @Test
    public void open() {
        // Arrange
        mockedHandler.open(EasyMock.eq("url"), EasyMock.eq("name"), EasyMock.eq("features"));
        EasyMock.expectLastCall();
        EasyMock.replay(mockedHandler);

        // Act
        Window.open("url", "name", "features");

        // Assert
        EasyMock.verify(mockedHandler);
    }

    @Test
    public void print() {
        // Arrange
        mockedHandler.print();
        EasyMock.expectLastCall();
        EasyMock.replay(mockedHandler);

        // Act
        Window.print();

        // Assert
        EasyMock.verify(mockedHandler);
    }

    @Test
    public void prompt() {
        // Arrange
        mockedHandler.prompt(EasyMock.eq("prompt message"), EasyMock.eq("initial value"));
        EasyMock.expectLastCall().andReturn("mocked message");
        EasyMock.replay(mockedHandler);

        // Act
        String result = Window.prompt("prompt message", "initial value");

        // Assert
        EasyMock.verify(mockedHandler);
        assertEquals("mocked message", result);
    }

    @Test
    public void title() {
        // Arrange
        Document.get().setTitle("arranged title");

        // Act
        Window.setTitle("my title");

        // Assert
        assertEquals("my title", Window.getTitle());
        assertEquals("my title", Document.get().getTitle());
    }

}
