package com.googlecode.gwt.test.internal;

import javassist.CtClass;
import javassist.CtMethod;

/**
 * <p>
 * Interface for a class patcher.
 * </p>
 * <p>
 * <p>
 * A patcher provides custom bodies (implementations) for the methods of a class. Patchers are used
 * to replace methods implementations which are not suitable for a JVM (typically, those containing
 * Javascript code) by valid Java ones.
 * </p>
 * <p>
 * <p>
 * It's based on javassist framework to make bytecode manipulation simple.
 * </p>
 * <p>
 * <p>
 * Although the framework provides some generic implementations of this interface (which should
 * permit to produce iso-functional, Java compliant code in the majority of cases), it is possible
 * for you to create your own patcher (specifically, if you're writing your own GWT component class)
 * if the default patching mechanisms doesn't suit your needs.
 * </p>
 * <p>
 * <p>
 * The custom patchers used to test an application are configured in the
 * META-INF\gwt-test-utils.properties file.
 * </p>
 *
 * @author Gael Lazzari
 */
interface Patcher {

    /**
     * Finalizes the patching mechanism for the specified class.
     *
     * @param c the javassist representation for the current class to patch.
     * @throws Exception If any error occurs during the finalization.
     */
    void finalizeClass(CtClass c) throws Exception;

    /**
     * Returns the new body for the specified method.
     *
     * @param m the method to patch
     * @return the new java code of the method, or null if the method should not be modified.
     * @throws Exception If any error occurs when getting the new java code.
     */
    String getNewBody(CtMethod m) throws Exception;

    /**
     * Initializes patching for the specified class. If you want to add some member to a class, or
     * modify any existing constructor, you should do it during this initialization phase.
     *
     * @param c the javassist representation for the current class to patch.
     * @throws Exception If any error occurs during the initialization.
     */
    void initClass(CtClass c) throws Exception;

}
