package com.googlecode.gwt.test.uibinder;

import java.util.Map;

/**
 * Factory for {@link UiObjectTag}.
 *
 * @param <T>
 * @author Gael Lazzari
 */
public interface UiObjectTagFactory<T> {

    /**
     * Try to create a UiObjectTag which would match the current UiBinder tag declaration. The
     * resulting UiObjectTag would wrap an object instance in order to append its child Widgets and
     * elements according to its UiBinder declaration.
     *
     * @param clazz      The wrapped object class to instanciate.
     * @param attributes map of attributes of the wrapped widget, with attribute XML names as keys,
     *                   corresponding objects as values (includes ui:with and ui:import resources).
     * @return The UiObjectTag instance which wrapped the wrapped widget in order to enhance it, or
     * null if the factory was not able to instanciate the right UiObjectTag implementation.
     */
    UiObjectTag<T> createUiObjectTag(Class<?> clazz, Map<String, Object> attributes);
}
