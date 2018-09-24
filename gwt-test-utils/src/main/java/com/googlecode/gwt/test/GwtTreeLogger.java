package com.googlecode.gwt.test;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.util.log.AbstractTreeLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The holder for the shared {@link TreeLogger} instance. If no custom logger is set, a default one
 * which prints in {@link System#out} is used. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public class GwtTreeLogger extends AbstractTreeLogger {

    private static final class ErrorDto {

        private final Throwable caught;
        private final HelpInfo helpInfo;
        private final String msg;

        private ErrorDto(String msg, Throwable caught, HelpInfo helpInfo) {
            this.msg = msg;
            this.caught = caught;
            this.helpInfo = helpInfo;
        }
    }

    private static final String DEFAULT_BRANCH_INDENT = "   ";

    private static final List<ErrorDto> ERROR_MSG_BUFFER = new ArrayList<>();

    private static final GwtTreeLogger INSTANCE = new GwtTreeLogger("");

    private static int lastIndexOfErrorWithinParentLogger = -1;

    private static final Logger LOGGER = LoggerFactory.getLogger(GwtTreeLogger.class);

    public static GwtTreeLogger get() {
        return INSTANCE;
    }

    public static void reset() {
        lastIndexOfErrorWithinParentLogger = -1;
    }

    private final String indent;

    private GwtTreeLogger(String indent) {
        this.indent = indent;
    }

    public void onUnableToCompleteError() {
        for (ErrorDto error : ERROR_MSG_BUFFER) {

            if (error.helpInfo == null || error.helpInfo.getURL() == null) {
                LOGGER.error(error.msg, error.caught);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(error.msg);
                sb.append(" (for additional info see:").append(error.helpInfo.getURL().toString());

                LOGGER.error(sb.toString(), error.caught);
            }
        }

        ERROR_MSG_BUFFER.clear();
        reset();
    }

    @Override
    protected AbstractTreeLogger doBranch() {
        return new GwtTreeLogger(indent + DEFAULT_BRANCH_INDENT);
    }

    @Override
    protected void doCommitBranch(AbstractTreeLogger childBeingCommitted, Type type, String msg,
                                  Throwable caught, HelpInfo helpInfo) {
        doLog(childBeingCommitted.getBranchedIndex(), type, msg, caught, helpInfo);
    }

    @Override
    protected void doLog(int indexOfLogEntryWithinParentLogger, Type type, String msg,
                         Throwable caught, HelpInfo helpInfo) {

        if (lastIndexOfErrorWithinParentLogger > -1
                && indexOfLogEntryWithinParentLogger < lastIndexOfErrorWithinParentLogger) {
            // new error
            ErrorDto baseError = ERROR_MSG_BUFFER.get(ERROR_MSG_BUFFER.size() - 1);
            ERROR_MSG_BUFFER.clear();
            ERROR_MSG_BUFFER.add(baseError);
        }

        switch (type) {
            case DEBUG:
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(indent + msg, caught);
                }
                break;
            case ERROR:
                if (LOGGER.isErrorEnabled()) {
                    ERROR_MSG_BUFFER.add(new ErrorDto(msg, caught, helpInfo));
                    lastIndexOfErrorWithinParentLogger = indexOfLogEntryWithinParentLogger;
                }
                break;
            case INFO:
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(indent + msg, caught);
                }
                break;
            case WARN:
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn(indent + msg, caught);
                }
                break;
            default:
                break;

        }

    }

}
