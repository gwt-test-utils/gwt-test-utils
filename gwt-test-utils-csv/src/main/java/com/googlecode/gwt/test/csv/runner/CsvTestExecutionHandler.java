package com.googlecode.gwt.test.csv.runner;

import java.io.File;

import com.googlecode.gwt.test.csv.CsvMethod;

/**
 * Callback interface to be notified <strong>before</strong> a {@link CsvMethod} annotated method
 * invocation.
 * 
 * @author Gael Lazzari
 * 
 */
public interface CsvTestExecutionHandler {

   /**
    * The callback method to implement to be notified after a new CSV test is launched.
    * 
    * @param testFile The executed csv file.
    */
   void afterCsvTestExecution(File testFile);

   /**
    * The callback method to implement to be notified before a new CSV test is launched.
    * 
    * @param testFile The executed csv file.
    */
   void beforeCsvTestExecution(File testFile);

   /**
    * The callback method to implement to be notified that a {@link CsvMethod} is being invoked.
    * 
    * @param data The invocation data which might be exploited
    */
   void onCsvMethodInvocation(CsvMethodInvocation data);

}
