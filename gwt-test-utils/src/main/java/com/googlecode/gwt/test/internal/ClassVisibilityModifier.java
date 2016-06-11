package com.googlecode.gwt.test.internal;

import javassist.CtClass;
import javassist.Modifier;

/**
 * A {@link JavaClassModifier} which makes all classes public. This can be usefull for mocking
 * purpose.
 *
 * @author Gael Lazzari
 */
class ClassVisibilityModifier implements JavaClassModifier {

    public void modify(CtClass classToModify) throws Exception {
        int modifiers = classToModify.getModifiers();
        if (!Modifier.isPublic(modifiers)) {
            classToModify.setModifiers(modifiers + Modifier.PUBLIC);
        }

    }

}
