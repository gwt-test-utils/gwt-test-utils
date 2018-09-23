package com.googlecode.gwt.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.test.rpc.RemoteServiceCreateHandler;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SchedulerTest extends GwtTestTest {

    private int i;

    private int j;

    @Before
    public void before() {
        addGwtCreateHandler(new RemoteServiceCreateHandler() {

            @Override
            protected Object findService(Class<?> remoteServiceClass, String remoteServiceRelativePath) {
                if (remoteServiceClass == MyRemoteService.class) {
                    return (MyRemoteService) param1 -> "mock " + param1;
                }

                return null;
            }
        });
    }

    @Test
    public void scheduledCommandOrder() {
        // Given
        final StringBuilder sb = new StringBuilder();

        Scheduler.get().scheduleEntry(() -> sb.append("scheduleEntry1 "));

        Scheduler.get().scheduleFinally(() -> sb.append("scheduleFinally1 "));

        Scheduler.get().scheduleDeferred(() -> {
            sb.append("scheduleDeferred1 ");

            Scheduler.get().scheduleEntry(() -> {
                sb.append("scheduleEntry2 ");

                Scheduler.get().scheduleEntry(() -> sb.append("scheduleEntry3 "));

            });

            Scheduler.get().scheduleFinally(() -> {
                sb.append("scheduleFinally2 ");
                Scheduler.get().scheduleFinally(() -> sb.append("scheduleFinally3 "));
            });

            Scheduler.get().scheduleDeferred(() -> sb.append("scheduleDeferred2 "));

        });

        // When
        getBrowserSimulator().fireLoopEnd();

        // Then
        assertThat(sb.toString()).isEqualTo(
                "scheduleFinally1 scheduleEntry1 scheduleDeferred1 scheduleFinally2 scheduleFinally3 scheduleEntry2 scheduleEntry3 scheduleDeferred2 ");

    }

    @Test
    public void scheduledCommandOrderWithRpcCall() {
        // Given
        final StringBuilder sb = new StringBuilder();
        final MyRemoteServiceAsync service = GWT.create(MyRemoteService.class);

        Scheduler.get().scheduleEntry(() -> sb.append("scheduleEntry1 "));

        Scheduler.get().scheduleFinally(() -> sb.append("scheduleFinally1 "));

        service.myMethod("service1", new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(String result) {
                sb.append("onSuccess1 ");
            }
        });

        service.myMethod("service2", new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(String result) {
                sb.append("onSuccess2 ");
            }
        });

        Scheduler.get().scheduleDeferred(() -> {
            sb.append("scheduleDeferred1 ");

            service.myMethod("service3", new AsyncCallback<String>() {

                public void onFailure(Throwable caught) {
                }

                public void onSuccess(String result) {
                    sb.append("onSuccess3 ");
                }
            });

            Scheduler.get().scheduleEntry(() -> {
                sb.append("scheduleEntry2 ");

                Scheduler.get().scheduleEntry(() -> sb.append("scheduleEntry3 "));

            });

            Scheduler.get().scheduleFinally(() -> {
                sb.append("scheduleFinally2 ");

                Scheduler.get().scheduleFinally(() -> sb.append("scheduleFinally3 "));

            });

            Scheduler.get().scheduleDeferred(() -> sb.append("scheduleDeferred2 "));

        });

        // When
        getBrowserSimulator().fireLoopEnd();

        // Then
        assertThat(sb.toString()).isEqualTo(
                "scheduleFinally1 scheduleEntry1 scheduleDeferred1 onSuccess1 onSuccess2 scheduleFinally2 scheduleFinally3 scheduleEntry2 scheduleEntry3 scheduleDeferred2 onSuccess3 ");
    }

    @Test
    public void scheduleDeferred() {
        // Given
        final StringBuilder sb = new StringBuilder();

        Scheduler.get().scheduleDeferred(() -> sb.append("scheduleDeferred"));

        // Preconditions
        assertThat(sb.toString()).isEmpty();

        // When
        getBrowserSimulator().fireLoopEnd();

        // Then
        assertThat(sb.toString()).isEqualTo("scheduleDeferred");
    }

    @Test
    public void scheduledRepeatingCommandOrder() {
        // Given
        i = j = 0;
        final StringBuilder sb = new StringBuilder();

        Scheduler.get().scheduleEntry(() -> {
            sb.append("entry").append(i).append(" ");
            return 3 > i++;
        });

        Scheduler.get().scheduleFinally(() -> {
            sb.append("finally").append(j).append(" ");

            Scheduler.get().scheduleEntry(() -> {
                sb.append("subentry").append(j).append(" ");
                return false;
            });

            Scheduler.get().scheduleFinally(() -> {
                sb.append("subfinally").append(j).append(" ");
                return false;
            });

            return 3 > j++;
        });

        // When
        getBrowserSimulator().fireLoopEnd();

        // Then
        assertThat(sb.toString()).isEqualTo(
                "finally0 subfinally1 entry0 subentry1 finally1 subfinally2 entry1 subentry2 finally2 subfinally3 entry2 subentry3 finally3 subfinally4 entry3 subentry4 ");
    }

    @Test
    public void scheduleEntry() {
        // Given
        final StringBuilder sb = new StringBuilder();

        Scheduler.get().scheduleEntry(() -> sb.append("scheduleEntry"));

        // Preconditions
        assertThat(sb.toString()).isEmpty();

        // When
        getBrowserSimulator().fireLoopEnd();

        // Then
        assertThat(sb.toString()).isEqualTo("scheduleEntry");
    }

    @Test
    public void scheduleFinally() {
        // Given
        final StringBuilder sb = new StringBuilder();

        Scheduler.get().scheduleFinally(() -> sb.append("scheduleFinally"));

        // Preconditions
        assertThat(sb.toString()).isEmpty();

        // When
        getBrowserSimulator().fireLoopEnd();

        // Then
        assertThat(sb.toString()).isEqualTo("scheduleFinally");
    }

    @Test
    public void scheduleIncremental() {
        // Given
        final StringBuilder sb = new StringBuilder();
        final int COUNT = 2;

        RepeatingCommand command = new RepeatingCommand() {

            private int index = 0;

            public boolean execute() {
                sb.append(index++);
                return index <= COUNT;
            }
        };

        // When
        Scheduler.get().scheduleIncremental(command);

        // Then
        assertThat(sb.toString()).isEqualTo("012");
    }
}
