package com.googlecode.gwt.test.internal.patchers;

import com.googlecode.gwt.test.finder.GwtFinder;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.lang.reflect.Modifier;

@PatchClass(GwtFinder.class)
class GwtFinderPatcher {

    @InitMethod
    static void initClass(CtClass c) throws Exception {
        makeMethodPublicStatic(c, "onAttach");
        makeMethodPublicStatic(c, "onDetach");
        makeMethodPublicStatic(c, "onSetHTML");
        makeMethodPublicStatic(c, "onSetId");
        makeMethodPublicStatic(c, "onSetName");
        makeMethodPublicStatic(c, "onSetText");

    }

    private static void makeMethodPublicStatic(CtClass c, String methodName)
            throws NotFoundException {
        CtMethod method = c.getDeclaredMethod(methodName);
        method.setModifiers(Modifier.PUBLIC + Modifier.STATIC);
    }
}
