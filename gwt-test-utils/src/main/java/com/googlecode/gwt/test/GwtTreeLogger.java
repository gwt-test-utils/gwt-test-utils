package com.googlecode.gwt.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.util.log.AbstractTreeLogger;

/**
 * The holder for the shared {@link TreeLogger} instance. If no custom logger is set, a default one
 * which prints in {@link System#out} is used. <strong>For internal use only.</strong>
 * 
 * @author Gael Lazzari
 * 
 */
public class GwtTreeLogger extends AbstractTreeLogger {

   private static final GwtTreeLogger INSTANCE = new GwtTreeLogger("");

   private static final Logger LOGGER = LoggerFactory.getLogger(GwtTreeLogger.class);

   public static GwtTreeLogger get() {
      return INSTANCE;
   }

   private final String indent;

   private GwtTreeLogger(String indent) {
      this.indent = indent;
   }

   @Override
   protected AbstractTreeLogger doBranch() {
      return new GwtTreeLogger(indent + "   ");
   }

   @Override
   protected void doCommitBranch(AbstractTreeLogger childBeingCommitted, Type type, String msg,
            Throwable caught, HelpInfo helpInfo) {
      doLog(childBeingCommitted.getBranchedIndex(), type, msg, caught, helpInfo);
   }

   @Override
   protected void doLog(int indexOfLogEntryWithinParentLogger, Type type, String msg,
            Throwable caught, HelpInfo helpInfo) {

      switch (type) {
         case DEBUG:
            if (LOGGER.isDebugEnabled()) {
               LOGGER.debug(msg, caught);
            }
            break;
         case ERROR:
            if (LOGGER.isErrorEnabled()) {
               LOGGER.error(msg, caught);
            }
            break;
         case INFO:
            if (LOGGER.isInfoEnabled()) {
               LOGGER.info(msg, caught);
            }
            break;
         case WARN:
            if (LOGGER.isWarnEnabled()) {
               LOGGER.warn(msg, caught);
            }
            break;
         default:
            break;

      }

   }

}
