package com.googlecode.gwt.test;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.googlecode.gwt.MockValueChangeHandler;
import org.junit.Test;

import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("deprecation")
public class HistoryTest extends GwtTestTest {

    @Test
    public void back_forward_AfterNewItem() {
        // Given
        History.newItem("init");
        History.newItem("myToken");
        History.back();
        History.newItem("newToken");
        MockValueChangeHandler<String> changeListener = new MockValueChangeHandler<String>();
        History.addValueChangeHandler(changeListener);

        // When 1
        History.back();

        // Then 2
        assertThat(changeListener.getCallCount()).isEqualTo(1);
        assertThat(changeListener.getLast()).isEqualTo("init");

        // When 2
        History.forward();

        // Then 2
        assertThat(changeListener.getCallCount()).isEqualTo(2);
        assertThat(changeListener.getLast()).isEqualTo("newToken");

        // When 3 : can't forward anymore
        History.forward();

        // Then 3
        assertThat(changeListener.getCallCount()).isEqualTo(2);
        assertThat(changeListener.getLast()).isEqualTo("newToken");

        // When 4
        History.back();

        // Then 4
        assertThat(changeListener.getCallCount()).isEqualTo(3);
        assertThat(changeListener.getLast()).isEqualTo("init");
    }

    @Test
    public void back_forward_HistoryListener() {
        // Given
        History.newItem("init");
        History.newItem("myToken");
        MockHistoryListener listener = new MockHistoryListener();
        History.addHistoryListener(listener);

        // When : first back
        History.back();

        // Then
        assertThat(listener.getCallCount()).isEqualTo(1);
        assertThat(listener.getLast()).isEqualTo("init");

        // When 2 : second back : no more token
        History.back();

        // Then 2
        assertThat(listener.getCallCount()).isEqualTo(2);
        assertThat(listener.getLast()).isEqualTo("");

        // When 3 : third back : no ValueChangeEvent
        History.back();

        // Then 3
        assertThat(listener.getCallCount()).isEqualTo(2);
        assertThat(listener.getLast()).isEqualTo("");

        // When 4
        History.forward();

        // Then 4
        assertThat(listener.getCallCount()).isEqualTo(3);
        assertThat(listener.getLast()).isEqualTo("init");

        // When 5
        History.forward();

        // Then 5
        assertThat(listener.getCallCount()).isEqualTo(4);
        assertThat(listener.getLast()).isEqualTo("myToken");

        // When 6 : can't go forward
        History.forward();

        // Then 5
        assertThat(listener.getCallCount()).isEqualTo(4);
        assertThat(listener.getLast()).isEqualTo("myToken");
    }

    @Test
    public void back_forward_ValueChangeHandler() {
        // Given
        History.newItem("init");
        History.newItem("myToken");
        MockValueChangeHandler<String> changeHandler = new MockValueChangeHandler<String>();
        History.addValueChangeHandler(changeHandler);

        // When : first back
        History.back();

        // Then
        assertThat(changeHandler.getCallCount()).isEqualTo(1);
        assertThat(changeHandler.getLast()).isEqualTo("init");

        // When 2 : second back : no more token
        History.back();

        // Then 2
        assertThat(changeHandler.getCallCount()).isEqualTo(2);
        assertThat(changeHandler.getLast()).isEqualTo("");

        // When 3 : third back : no ValueChangeEvent
        History.back();

        // Then 3
        assertThat(changeHandler.getCallCount()).isEqualTo(2);
        assertThat(changeHandler.getLast()).isEqualTo("");

        // When 4
        History.forward();

        // Then 4
        assertThat(changeHandler.getCallCount()).isEqualTo(3);
        assertThat(changeHandler.getLast()).isEqualTo("init");

        // When 5
        History.forward();

        // Then 5
        assertThat(changeHandler.getCallCount()).isEqualTo(4);
        assertThat(changeHandler.getLast()).isEqualTo("myToken");

        // When 6 : can't go forward
        History.forward();

        // Then 5
        assertThat(changeHandler.getCallCount()).isEqualTo(4);
        assertThat(changeHandler.getLast()).isEqualTo("myToken");
    }

    @Test
    public void newItem_HistoryListener() {
        // Given
        MockHistoryListener history = new MockHistoryListener();
        History.addHistoryListener(history);

        // When
        History.newItem("init");

        // Then
        assertThat(history.getCallCount()).isEqualTo(1);
        assertThat(history.getLast()).isEqualTo("init");

        // When 2
        History.newItem("myToken");

        // Then 2
        assertThat(history.getCallCount()).isEqualTo(2);
        assertThat(history.getLast()).isEqualTo("myToken");
    }

    @Test
    public void newItem_ValueChangeHandler() {
        // Given
        MockValueChangeHandler<String> changeHandler = new MockValueChangeHandler<String>();
        History.addValueChangeHandler(changeHandler);

        // When
        History.newItem("init");

        // Then
        assertThat(changeHandler.getCallCount()).isEqualTo(1);
        assertThat(changeHandler.getLast()).isEqualTo("init");

        // When 2
        History.newItem("myToken");

        // Then 2
        assertThat(changeHandler.getCallCount()).isEqualTo(2);
        assertThat(changeHandler.getLast()).isEqualTo("myToken");
    }

    @Override
    protected String getHostPagePath(String moduleFullQualifiedName) {
        return "test.html";
    }

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

}
