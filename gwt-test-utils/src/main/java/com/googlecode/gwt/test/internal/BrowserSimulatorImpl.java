package com.googlecode.gwt.test.internal;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.googlecode.gwt.test.BrowserSimulator;
import com.googlecode.gwt.test.exceptions.GwtTestException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Trigger {@link ScheduledCommand}, {@link RepeatingCommand} and RPC callbacks which were scheduled
 * to be run before and after the Browser
 *
 * @author Gael Lazzari
 * @see Scheduler
 */
public class BrowserSimulatorImpl implements BrowserSimulator, AfterTestCallback {

    private static final BrowserSimulatorImpl INSTANCE = new BrowserSimulatorImpl();

    public static BrowserSimulatorImpl get() {
        return INSTANCE;
    }

    private final Queue<Command> asyncCallbackCommands = new LinkedList<>();
    private final Queue<ScheduledCommand> deferredScheduledCommands = new LinkedList<>();
    private final List<RepeatingCommand> entryRepeatingCommands = new LinkedList<>();
    private final Queue<ScheduledCommand> entryScheduledCommands = new LinkedList<>();
    private final List<RepeatingCommand> finallyRepeatingCommands = new LinkedList<>();
    private final Queue<ScheduledCommand> finallyScheduledCommands = new LinkedList<>();

    private Element currentFocusElement;

    private boolean isTriggering;

    private BrowserSimulatorImpl() {
        AfterTestCallbackManager.get().registerCallback(this);
    }

    /**
     * Check there is no pending command to execute. A {@link GwtTestException} would we thrown.
     */
    @Override
    public void afterTest() {

        setCurrentFocusElement(null);
        if (deferredScheduledCommands.size() == 0 //
                && finallyScheduledCommands.size() == 0 //
                && finallyRepeatingCommands.size() == 0 //
                && entryScheduledCommands.size() == 0 //
                && entryRepeatingCommands.size() == 0 //
                && asyncCallbackCommands.size() == 0) {
            return;
        }

        String testName = GwtConfig.get().getModuleRunner().getClass().getSimpleName();
        String format = "%s pending %s must be triggered manually by calling %s.getBrowserSimulator().fireLoopEnd() before making your test assertions";
        String errorMessage;

        if (deferredScheduledCommands.size() > 0) {
            errorMessage = String.format(format, deferredScheduledCommands.size(),
                    "scheduledDeferred ScheduledCommand(s)", testName);
        } else if (entryScheduledCommands.size() > 0) {
            errorMessage = String.format(format, entryScheduledCommands.size(),
                    "scheduledEntry ScheduledCommand(s)", testName);
        } else if (entryRepeatingCommands.size() > 0) {
            errorMessage = String.format(format, entryRepeatingCommands.size(),
                    "scheduledEntry RepeatingCommand(s)", testName);
        } else if (finallyScheduledCommands.size() > 0) {
            errorMessage = String.format(format, finallyRepeatingCommands.size(),
                    "scheduledFinally ScheduledCommand(s)", testName);
        } else if (finallyRepeatingCommands.size() > 0) {
            errorMessage = String.format(format, finallyRepeatingCommands.size(),
                    "scheduledFinally RepeatingCommand(s)", testName);
        } else {
            errorMessage = String.format(format, asyncCallbackCommands.size(), "AsyncCallback",
                    testName);
        }

        clearPendingCommands();

        throw new GwtTestException(errorMessage);

    }

    public void clearPendingCommands() {
        deferredScheduledCommands.clear();
        entryScheduledCommands.clear();
        entryRepeatingCommands.clear();
        finallyScheduledCommands.clear();
        finallyRepeatingCommands.clear();
        asyncCallbackCommands.clear();
    }

    public void fireLoopEnd() {
        if (isTriggering) {
            return;
        }

        try {
            isTriggering = true;

            do {
                Queue<ScheduledCommand> deferredToExecuteInLoop = new LinkedList<ScheduledCommand>(
                        deferredScheduledCommands);
                deferredScheduledCommands.clear();

                Queue<Command> asyncCallbackToExecuteInLoop = new LinkedList<Command>(
                        asyncCallbackCommands);
                asyncCallbackCommands.clear();

                fireEventLoop(deferredToExecuteInLoop, asyncCallbackToExecuteInLoop);
            } while (entryRepeatingCommands.size() > 0 //
                    || finallyRepeatingCommands.size() > 0 //
                    || deferredScheduledCommands.size() > 0 //
                    || asyncCallbackCommands.size() > 0);

        } finally {
            isTriggering = false;
        }
    }

    public void recordAsyncCall(Command asyncCallbackCommand) {
        asyncCallbackCommands.add(asyncCallbackCommand);
    }

    public void scheduleDeferred(ScheduledCommand scheduledCommand) {
        deferredScheduledCommands.add(scheduledCommand);
    }

    public void scheduleEntry(RepeatingCommand repeatingCommand) {
        entryRepeatingCommands.add(repeatingCommand);
    }

    public void scheduleEntry(ScheduledCommand scheduledCommand) {
        entryScheduledCommands.add(scheduledCommand);
    }

    public void scheduleFinally(RepeatingCommand repeatingCommand) {
        finallyRepeatingCommands.add(repeatingCommand);
    }

    public void scheduleFinally(ScheduledCommand scheduledCommand) {
        finallyScheduledCommands.add(scheduledCommand);
    }

    private void executeReaptingCommnand(List<RepeatingCommand> commands) {
        List<RepeatingCommand> toRemove = new ArrayList<>();
        int i = 0;

        while (i < commands.size()) {
            RepeatingCommand current = commands.get(i);
            if (!current.execute()) {
                toRemove.add(current);
            }

            i++;
        }

        for (RepeatingCommand rc : toRemove) {
            commands.remove(rc);
        }
    }

    private void fireEventLoop(Queue<ScheduledCommand> deferredToExecuteInLoop,
                               Queue<Command> asyncCallbackToExecuteInLoop) {

        while (!finallyScheduledCommands.isEmpty()) {
            finallyScheduledCommands.poll().execute();
        }

        executeReaptingCommnand(finallyRepeatingCommands);

        while (!entryScheduledCommands.isEmpty()) {
            entryScheduledCommands.poll().execute();
        }

        executeReaptingCommnand(entryRepeatingCommands);

        while (!deferredToExecuteInLoop.isEmpty()) {
            deferredToExecuteInLoop.poll().execute();
        }

        while (!asyncCallbackToExecuteInLoop.isEmpty()) {
            asyncCallbackToExecuteInLoop.poll().execute();
        }
    }

    public Element getCurrentFocusElement() {
        return currentFocusElement;
    }

    public void setCurrentFocusElement(Element currentFocusElement) {
        this.currentFocusElement = currentFocusElement;
    }
}
