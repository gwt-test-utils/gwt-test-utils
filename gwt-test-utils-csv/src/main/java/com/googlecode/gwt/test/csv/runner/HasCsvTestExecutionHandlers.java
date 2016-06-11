package com.googlecode.gwt.test.csv.runner;

import com.googlecode.gwt.test.csv.CsvMethod;

import java.util.List;

/**
 * Interface used by {@link CsvRunner} to trigger {@link CsvTestExecutionHandler}.
 *
 * @author Gael Lazzari
 */
public interface HasCsvTestExecutionHandlers {

    /**
     * Get the list of handlers to trigger while invoking {@link CsvMethod} annotated method.
     *
     * @return The list of handlers to trigger. <strong>Cannot be null</strong>
     */
    List<CsvTestExecutionHandler> getCsvTestExecutionHandlers();

}
