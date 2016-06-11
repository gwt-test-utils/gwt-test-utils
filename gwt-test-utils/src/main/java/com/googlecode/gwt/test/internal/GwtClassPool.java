package com.googlecode.gwt.test.internal;

import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * Entry point to the javassist API. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public class GwtClassPool {

    private static ClassPool classPool = ClassPool.getDefault();

    public static ClassPool get() {
        return classPool;
    }

    public static CtClass getClass(String className) {
        try {
            return GwtClassPool.get().get(className);
        } catch (NotFoundException e) {
            throw new GwtTestPatchException("Cannot find class in the classpath : '" + className + "'");
        }
    }

    public static CtClass getCtClass(Class<?> clazz) {
        return getClass(clazz.getName());
    }
}
