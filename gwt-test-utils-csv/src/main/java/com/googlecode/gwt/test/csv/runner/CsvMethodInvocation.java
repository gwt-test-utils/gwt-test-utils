package com.googlecode.gwt.test.csv.runner;

import com.googlecode.gwt.test.csv.CsvMethod;

import java.lang.reflect.Method;

/**
 * A DTO to hold a {@link CsvMethod} annotated method invocation data.
 *
 * @author Gael Lazzari
 */
public class CsvMethodInvocation {

    private final Method csvMethod;
    private final int lineNumber;
    private final String macroExecutionInfo;
    private final String[] paramIdentifiers;
    private final Object[] paramValues;

    CsvMethodInvocation(int lineNumber, String macroExecutionInfo, Method csvMethod,
                        String[] paramIdentifiers, Object[] paramValues) {
        this.lineNumber = lineNumber;
        this.macroExecutionInfo = macroExecutionInfo;
        this.csvMethod = csvMethod;
        this.paramIdentifiers = paramIdentifiers;
        this.paramValues = paramValues;
    }

    public Method getCsvMethod() {
        return csvMethod;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getMacroExecutionInfo() {
        return macroExecutionInfo;
    }

    public String[] getParamIdentifiers() {
        return paramIdentifiers;
    }

    public Object[] getParamValues() {
        return paramValues;
    }

}
