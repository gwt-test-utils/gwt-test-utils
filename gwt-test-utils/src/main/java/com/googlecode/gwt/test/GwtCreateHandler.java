package com.googlecode.gwt.test;

import com.google.gwt.core.client.GWT;
import com.googlecode.gwt.test.internal.handlers.GwtTestGWTBridge;

/**
 * <p>
 * Interface for an object capable of handling object creation, which is delegated by the patched
 * version of {@link GWT#create(Class)}.
 * </p>
 * <p>
 * <p>
 * All GwtCreateHandler instances are managed by a {@link GwtTestGWTBridge} which is responsible for
 * chaining those instances in a logical order.
 * </p>
 *
 * @author Gael Lazzari
 */
public interface GwtCreateHandler {

    /**
     * <p>
     * Instantiates an object of the given class.
     * </p>
     * <p>
     * <p>
     * This handler may be able to instantiate objects of certain types only. If the class passed as
     * parameter is not handled, the method should return null.
     * </p>
     *
     * @param classLiteral the class to instantiate
     * @return an object of this class if this GwtCreateHandler is capable of handling it, null
     * otherwise
     * @throws Exception if an error occurred when trying to create the object
     */
    Object create(Class<?> classLiteral) throws Exception;

}
