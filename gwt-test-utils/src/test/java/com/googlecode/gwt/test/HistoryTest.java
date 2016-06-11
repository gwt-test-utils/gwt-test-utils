package com.googlecode.gwt.test;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.googlecode.gwt.MockValueChangeHandler;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("deprecation")
public class HistoryTest extends GwtTestTest {

    private static class MockHistoryListener implements HistoryListener {

        LinkedList<String> reccorded = new LinkedList<String>();

        public int getCallCount() {
            return reccorded.size();
        }

        public String getLast() {
            return reccorded.getLast();
        }

        public void onHistoryChanged(String historyToken) {
            reccorded.add(historyToken);
        }

    }

    @Test
    public void back_forward_AfterNewItem() {
        // Arrange
        History.newItem("init");
        History.newItem("myToken");
        History.back();
        History.newItem("newToken");
        MockValueChangeHandler<String> changeListener = new MockValueChangeHandler<String>();
        History.addValueChangeHandler(changeListener);

        // Act 1
        History.back();

        // Assert 2
        assertEquals(1, changeListener.getCallCount());
        assertEquals("init", changeListener.getLast());

        // Act 2
        History.forward();

        // Assert 2
        assertEquals(2, changeListener.getCallCount());
        assertEquals("newToken", changeListener.getLast());

        // Act 3 : can't forward anymore
        History.forward();

        // Assert 3
        assertEquals(2, changeListener.getCallCount());
        assertEquals("newToken", changeListener.getLast());

        // Act 4
        History.back();

        // Assert 4
        assertEquals(3, changeListener.getCallCount());
        assertEquals("init", changeListener.getLast());
    }

    @Test
    public void back_forward_HistoryListener() {
        // Arrange
        History.newItem("init");
        History.newItem("myToken");
        MockHistoryListener listener = new MockHistoryListener();
        History.addHistoryListener(listener);

        // Act : first back
        History.back();

        // Assert
        assertEquals(1, listener.getCallCount());
        assertEquals("init", listener.getLast());

        // Act 2 : second back : no more token
        History.back();

        // Assert 2
        assertEquals(2, listener.getCallCount());
        assertEquals("", listener.getLast());

        // Act 3 : third back : no ValueChangeEvent
        History.back();

        // Assert 3
        assertEquals(2, listener.getCallCount());
        assertEquals("", listener.getLast());

        // Act 4
        History.forward();

        // Assert 4
        assertEquals(3, listener.getCallCount());
        assertEquals("init", listener.getLast());

        // Act 5
        History.forward();

        // Assert 5
        assertEquals(4, listener.getCallCount());
        assertEquals("myToken", listener.getLast());

        // Act 6 : can't go forward
        History.forward();

        // Assert 5
        assertEquals(4, listener.getCallCount());
        assertEquals("myToken", listener.getLast());
    }

    @Test
    public void back_forward_ValueChangeHandler() {
        // Arrange
        History.newItem("init");
        History.newItem("myToken");
        MockValueChangeHandler<String> changeHandler = new MockValueChangeHandler<String>();
        History.addValueChangeHandler(changeHandler);

        // Act : first back
        History.back();

        // Assert
        assertEquals(1, changeHandler.getCallCount());
        assertEquals("init", changeHandler.getLast());

        // Act 2 : second back : no more token
        History.back();

        // Assert 2
        assertEquals(2, changeHandler.getCallCount());
        assertEquals("", changeHandler.getLast());

        // Act 3 : third back : no ValueChangeEvent
        History.back();

        // Assert 3
        assertEquals(2, changeHandler.getCallCount());
        assertEquals("", changeHandler.getLast());

        // Act 4
        History.forward();

        // Assert 4
        assertEquals(3, changeHandler.getCallCount());
        assertEquals("init", changeHandler.getLast());

        // Act 5
        History.forward();

        // Assert 5
        assertEquals(4, changeHandler.getCallCount());
        assertEquals("myToken", changeHandler.getLast());

        // Act 6 : can't go forward
        History.forward();

        // Assert 5
        assertEquals(4, changeHandler.getCallCount());
        assertEquals("myToken", changeHandler.getLast());
    }

    @Test
    public void newItem_HistoryListener() {
        // Arrange
        MockHistoryListener history = new MockHistoryListener();
        History.addHistoryListener(history);

        // Act
        History.newItem("init");

        // Assert
        assertEquals(1, history.getCallCount());
        assertEquals("init", history.getLast());

        // Act 2
        History.newItem("myToken");

        // Assert 2
        assertEquals(2, history.getCallCount());
        assertEquals("myToken", history.getLast());
    }

    @Test
    public void newItem_ValueChangeHandler() {
        // Arrange
        MockValueChangeHandler<String> changeHandler = new MockValueChangeHandler<String>();
        History.addValueChangeHandler(changeHandler);

        // Act
        History.newItem("init");

        // Assert
        assertEquals(1, changeHandler.getCallCount());
        assertEquals("init", changeHandler.getLast());

        // Act 2
        History.newItem("myToken");

        // Assert 2
        assertEquals(2, changeHandler.getCallCount());
        assertEquals("myToken", changeHandler.getLast());
    }

    @Override
    protected String getHostPagePath(String moduleFullQualifiedName) {
        return "test.html";
    }

}
