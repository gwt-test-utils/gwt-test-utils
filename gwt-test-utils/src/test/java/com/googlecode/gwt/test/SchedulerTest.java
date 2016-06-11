package com.googlecode.gwt.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.test.rpc.RemoteServiceCreateHandler;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class SchedulerTest extends GwtTestTest {

    private int i;

    private int j;

    @Before
    public void before() {
        addGwtCreateHandler(new RemoteServiceCreateHandler() {

            @Override
            protected Object findService(Class<?> remoteServiceClass, String remoteServiceRelativePath) {
                if (remoteServiceClass == MyRemoteService.class) {
                    return new MyRemoteService() {

                        public String myMethod(String param1) {
                            return "mock " + param1;
                        }
                    };
                }

                return null;
            }
        });
    }

    @Test
    public void scheduledCommandOrder() {
        // Arrange
        final StringBuilder sb = new StringBuilder();

        Scheduler.get().scheduleEntry(new ScheduledCommand() {

            public void execute() {
                sb.append("scheduleEntry1 ");

            }
        });

        Scheduler.get().scheduleFinally(new ScheduledCommand() {

            public void execute() {
                sb.append("scheduleFinally1 ");

            }
        });

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            public void execute() {
                sb.append("scheduleDeferred1 ");

                Scheduler.get().scheduleEntry(new ScheduledCommand() {

                    public void execute() {
                        sb.append("scheduleEntry2 ");

                        Scheduler.get().scheduleEntry(new ScheduledCommand() {

                            public void execute() {
                                sb.append("scheduleEntry3 ");

                            }
                        });

                    }
                });

                Scheduler.get().scheduleFinally(new ScheduledCommand() {

                    public void execute() {
                        sb.append("scheduleFinally2 ");

                        Scheduler.get().scheduleFinally(new ScheduledCommand() {

                            public void execute() {
                                sb.append("scheduleFinally3 ");

                            }
                        });

                    }
                });

                Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                    public void execute() {
                        sb.append("scheduleDeferred2 ");

                    }
                });

            }
        });

        // Act
        getBrowserSimulator().fireLoopEnd();

        // Assert
        assertThat(sb.toString()).isEqualTo(
                "scheduleFinally1 scheduleEntry1 scheduleDeferred1 scheduleFinally2 scheduleFinally3 scheduleEntry2 scheduleEntry3 scheduleDeferred2 ");

    }

    @Test
    public void scheduledCommandOrderWithRpcCall() {
        // Arrange
        final StringBuilder sb = new StringBuilder();
        final MyRemoteServiceAsync service = GWT.create(MyRemoteService.class);

        Scheduler.get().scheduleEntry(new ScheduledCommand() {

            public void execute() {
                sb.append("scheduleEntry1 ");

            }
        });

        Scheduler.get().scheduleFinally(new ScheduledCommand() {

            public void execute() {
                sb.append("scheduleFinally1 ");

            }
        });

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

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            public void execute() {
                sb.append("scheduleDeferred1 ");

                service.myMethod("service3", new AsyncCallback<String>() {

                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess(String result) {
                        sb.append("onSuccess3 ");
                    }
                });

                Scheduler.get().scheduleEntry(new ScheduledCommand() {

                    public void execute() {
                        sb.append("scheduleEntry2 ");

                        Scheduler.get().scheduleEntry(new ScheduledCommand() {

                            public void execute() {
                                sb.append("scheduleEntry3 ");

                            }
                        });

                    }
                });

                Scheduler.get().scheduleFinally(new ScheduledCommand() {

                    public void execute() {
                        sb.append("scheduleFinally2 ");

                        Scheduler.get().scheduleFinally(new ScheduledCommand() {

                            public void execute() {
                                sb.append("scheduleFinally3 ");

                            }
                        });

                    }
                });

                Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                    public void execute() {
                        sb.append("scheduleDeferred2 ");

                    }
                });

            }
        });

        // Act
        getBrowserSimulator().fireLoopEnd();

        // Assert
        assertThat(sb.toString()).isEqualTo(
                "scheduleFinally1 scheduleEntry1 scheduleDeferred1 onSuccess1 onSuccess2 scheduleFinally2 scheduleFinally3 scheduleEntry2 scheduleEntry3 scheduleDeferred2 onSuccess3 ");
    }

    @Test
    public void scheduleDeferred() {
        // Arrange
        final StringBuilder sb = new StringBuilder();

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            public void execute() {
                sb.append("scheduleDeferred");

            }
        });

        // Pre-Assert
        assertThat(sb.toString()).isEmpty();

        // Act
        getBrowserSimulator().fireLoopEnd();

        // Assert
        assertThat(sb.toString()).isEqualTo("scheduleDeferred");
    }

    @Test
    public void scheduledRepeatingCommandOrder() {
        // Arrange
        i = j = 0;
        final StringBuilder sb = new StringBuilder();

        Scheduler.get().scheduleEntry(new RepeatingCommand() {

            public boolean execute() {
                sb.append("entry").append(i).append(" ");
                return 3 > i++;
            }
        });

        Scheduler.get().scheduleFinally(new RepeatingCommand() {

            public boolean execute() {
                sb.append("finally").append(j).append(" ");

                Scheduler.get().scheduleEntry(new RepeatingCommand() {

                    public boolean execute() {
                        sb.append("subentry").append(j).append(" ");
                        return false;
                    }
                });

                Scheduler.get().scheduleFinally(new RepeatingCommand() {

                    public boolean execute() {
                        sb.append("subfinally").append(j).append(" ");
                        return false;
                    }
                });

                return 3 > j++;
            }
        });

        // Act
        getBrowserSimulator().fireLoopEnd();

        // Assert
        assertThat(sb.toString()).isEqualTo(
                "finally0 subfinally1 entry0 subentry1 finally1 subfinally2 entry1 subentry2 finally2 subfinally3 entry2 subentry3 finally3 subfinally4 entry3 subentry4 ");
    }

    @Test
    public void scheduleEntry() {
        // Arrange
        final StringBuilder sb = new StringBuilder();

        Scheduler.get().scheduleEntry(new ScheduledCommand() {

            public void execute() {
                sb.append("scheduleEntry");

            }
        });

        // Pre-Assert
        assertThat(sb.toString()).isEmpty();

        // Act
        getBrowserSimulator().fireLoopEnd();

        // Assert
        assertThat(sb.toString()).isEqualTo("scheduleEntry");
    }

    @Test
    public void scheduleFinally() {
        // Arrange
        final StringBuilder sb = new StringBuilder();

        Scheduler.get().scheduleFinally(new ScheduledCommand() {

            public void execute() {
                sb.append("scheduleFinally");

            }
        });

        // Pre-Assert
        assertThat(sb.toString()).isEmpty();

        // Act
        getBrowserSimulator().fireLoopEnd();

        // Assert
        assertThat(sb.toString()).isEqualTo("scheduleFinally");
    }

    @Test
    public void scheduleIncremental() {
        // Arrange
        final StringBuilder sb = new StringBuilder();
        final int COUNT = 2;

        RepeatingCommand command = new RepeatingCommand() {

            private int index = 0;

            public boolean execute() {
                sb.append(index++);
                return index <= COUNT;
            }
        };

        // Act
        Scheduler.get().scheduleIncremental(command);

        // Assert
        assertThat(sb.toString()).isEqualTo("012");
    }
}
