package com.googlecode.gwt.test.csv.runner;

import java.util.List;

import com.googlecode.gwt.test.csv.CsvMethod;

/**
 * Interface used by {@link CsvRunner} to trigger {@link CsvMethodInvocationHandler}.
 * 
 * @author Gael Lazzari
 * 
 */
public interface HasCsvMethodInvocationHandlers {

   /**
    * Get the list of handlers to trigger before invoking a {@link CsvMethod} annotated method.
    * 
    * @return The list of handlers to trigger. <strong>Cannot be null</strong>
    */
   List<CsvMethodInvocationHandler> getCsvMethodInvocationHandlers();

}
