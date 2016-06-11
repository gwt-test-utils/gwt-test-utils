package com.googlecode.gwt.test.internal.resources;

import com.google.gwt.resources.client.ResourcePrototype;

import java.lang.reflect.Method;

/**
 * Callback interface where {@link ResourcePrototype } methods calls are redirected. <strong>For
 * internal use only.</strong>
 *
 * @author Gael Lazzari
 */
interface ResourcePrototypeCallback {

    Object call(Method method, Object[] args) throws Exception;

}
