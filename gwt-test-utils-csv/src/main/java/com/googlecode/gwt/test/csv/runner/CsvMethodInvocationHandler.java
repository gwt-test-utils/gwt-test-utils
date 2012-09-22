package com.googlecode.gwt.test.csv.runner;

import com.googlecode.gwt.test.csv.CsvMethod;

/**
 * Callback interface to be notified <strong>before</strong> a {@link CsvMethod} annotated method
 * invocation.
 * 
 * @author Gael Lazzari
 * 
 */
public interface CsvMethodInvocationHandler {

   /**
    * The callback method to implement to be notified that a {@link CsvMethod} is being invoked.
    * 
    * @param data The invocation data which might be exploited
    */
   void onCsvMethodInvocation(CsvMethodInvocation data);

}
