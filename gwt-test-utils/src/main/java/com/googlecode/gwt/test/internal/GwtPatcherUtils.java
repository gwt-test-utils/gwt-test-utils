package com.googlecode.gwt.test.internal;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

/**
 * Some patching utility methods. Bytecode manipulation relies on javassist API. <strong>For
 * internal use only.</strong>
 *
 * @author Bertrand Paquet
 * @author Gael Lazzari
 */
public class GwtPatcherUtils {

    public static void patch(CtClass c, Patcher patcher) throws Exception {
        ClassVisibilityModifier.setPublic(c, true);

        if (patcher != null) {
            patcher.initClass(c);
        }

        for (CtMethod m : c.getDeclaredMethods()) {
            boolean wasAbstract = false;
            String newBody = null;
            if (Modifier.isAbstract(m.getModifiers())) {
                m.setModifiers(m.getModifiers() - Modifier.ABSTRACT);
                wasAbstract = true;
            }
            if (patcher != null) {
                newBody = patcher.getNewBody(m);
            }
            if (newBody != null) {

                if (Modifier.isNative(m.getModifiers())) {
                    m.setModifiers(m.getModifiers() - Modifier.NATIVE);
                }

                replaceImplementation(m, newBody);

            } else if (wasAbstract) {
                if (patcher != null) {
                    m.setBody("{ throw new " + UnsupportedOperationException.class.getName()
                            + "(\"Abstract method '" + c.getSimpleName() + "." + m.getName()
                            + "()' is not patched by " + patcher.getClass().getName() + "\"); }");
                } else {
                    m.setBody("{ throw new " + UnsupportedOperationException.class.getName()
                            + "(\"Abstract method '" + c.getSimpleName() + "." + m.getName()
                            + "()' is not patched by any declared " + Patcher.class.getSimpleName()
                            + " instance\"); }");
                }
            }
        }

        if (patcher != null) {
            patcher.finalizeClass(c);
        }
    }

    private static void replaceImplementation(CtMethod m, String src) throws Exception {

        if (src == null || src.trim().length() == 0) {
            m.setBody(null);
        } else {
            src = src.trim();
            if (!src.startsWith("{")) {
                if (!m.getReturnType().equals(CtClass.voidType) && !src.startsWith("return")) {
                    src = "{ return ($r)($w) " + src;
                } else {
                    src = "{ " + src;
                }
            }

            if (!src.endsWith("}")) {
                if (!src.endsWith(";")) {
                    src = src + "; }";
                } else {
                    src = src + " }";
                }
            }
            try {
                m.setBody(src);
            } catch (CannotCompileException e) {
                throw new CannotCompileException("Unable to compile new body for method '"
                        + m.getLongName() + "' : " + src, e);
            }
        }
    }

    private GwtPatcherUtils() {

    }

}
