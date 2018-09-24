package com.googlecode.gwt.test.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class responsible for triggering {@link AfterTestCallback#afterTest()} callback methods from
 * registered callbacks. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public class AfterTestCallbackManager {

    private static final AfterTestCallbackManager INSTANCE = new AfterTestCallbackManager();

    public static AfterTestCallbackManager get() {
        return INSTANCE;
    }

    private final Set<AfterTestCallback> callbacks;
    private final Set<AfterTestCallback> finalCallbacks;
    private final Set<AfterTestCallback> removeableCallbacks;

    private AfterTestCallbackManager() {
        callbacks = new HashSet<>();
        finalCallbacks = new HashSet<>();
        removeableCallbacks = new HashSet<>();
    }

    /**
     * Register a callback to be triggered after a test execution.
     *
     * @param callback The callback to register
     * @return <tt>true</tt> if the callback was not already registered
     */
    public boolean registerCallback(AfterTestCallback callback) {
        return callbacks.add(callback);
    }

    public boolean registerFinalCallback(AfterTestCallback finalCallback) {
        return finalCallbacks.add(finalCallback);
    }

    /**
     * Register a callback to triggered after a test execution. This callback will be removed at the
     * end of the test execution.
     *
     * @param removeableCallback The callback to register
     * @return <tt>true</tt> if the callback was not already registered.
     */
    public boolean registerRemoveableCallback(AfterTestCallback removeableCallback) {
        return removeableCallbacks.add(removeableCallback);
    }

    /**
     * Trigger all the registered callbacks and collect all the exception that may be thrown.
     *
     * @return A list of exceptions that has been thrown when triggering the different callbacks.
     */
    public List<Throwable> triggerCallbacks() {
        List<Throwable> throwables = new ArrayList<>();

        for (AfterTestCallback callback : callbacks) {
            executeCallback(callback, throwables);
        }

        for (AfterTestCallback callback : removeableCallbacks) {
            executeCallback(callback, throwables);
        }

        for (AfterTestCallback callback : finalCallbacks) {
            executeCallback(callback, throwables);
        }

        removeableCallbacks.clear();

        return throwables;
    }

    /**
     * Unregister a callback so it will not be triggered anymore.
     *
     * @param callback The callback to unregister.
     * @return <tt>true</tt> if this callback was registered.
     */
    public boolean unregisterCallback(AfterTestCallback callback) {
        return callbacks.remove(callback);
    }

    private void executeCallback(AfterTestCallback callback, List<Throwable> throwables) {
        try {
            callback.afterTest();
        } catch (Throwable t) {
            throwables.add(t);
        }
    }

}
