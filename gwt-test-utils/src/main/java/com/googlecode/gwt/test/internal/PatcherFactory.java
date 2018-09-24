package com.googlecode.gwt.test.internal;

import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import javassist.CtClass;
import javassist.NotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class PatcherFactory {

    private static final CtClass jsoClass = GwtClassPool.getClass("com.google.gwt.core.client.JavaScriptObject");

    private final Map<String, Patcher> patchers;

    PatcherFactory(Map<String, Set<CtClass>> patchClassMap) {
        this.patchers = new HashMap<>();

        for (Map.Entry<String, Set<CtClass>> entry : patchClassMap.entrySet()) {
            Patcher patcher = createPatcher(entry.getValue());
            if (patcher != null) {
                patchers.put(entry.getKey(), patcher);
            }
        }
    }

    Patcher createPatcher(CtClass classToPatch) {
        Patcher patcher = patchers.get(classToPatch.getName());

        try {
            if (classToPatch.subtypeOf(jsoClass) && classToPatch != jsoClass) {
                patcher = new OverlayPatcher(patcher);
            }
        } catch (NotFoundException e) {
            // should never happen
            throw new GwtTestPatchException(e);
        }

        return patcher;

    }

    private Patcher createPatcher(Set<CtClass> patchClasses) {
        if (patchClasses != null && patchClasses.size() > 0) {
            return new AutomaticPatcher(patchClasses);
        } else {
            return null;
        }
    }

}
