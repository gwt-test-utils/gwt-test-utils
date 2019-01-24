package com.googlecode.gwt.test.internal;

import javassist.CtClass;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.ClassFile;
import javassist.bytecode.InnerClassesAttribute;

/**
 * A {@link JavaClassModifier} which makes all classes public. This can be usefull for mocking
 * purpose.
 *
 * @author Gael Lazzari
 */
class ClassVisibilityModifier implements JavaClassModifier {

    public void modify(CtClass classToModify) throws Exception {
    	setPublic(classToModify, false);
    }
    
    public static void setPublic(CtClass classToModify, boolean replaceAll) {
    	
    	int modifiers = classToModify.getModifiers();
        if ((replaceAll && Modifier.PUBLIC != modifiers) || !Modifier.isPublic(modifiers)) {
        	
        	CtClass outerClass = getDeclaringClass(classToModify);
        	
        	boolean wasFrozen = outerClass != null && outerClass.isFrozen();
        	if (wasFrozen) {
        		outerClass.defrost();
        	}
        	
        	try {
        		classToModify.setModifiers(replaceAll ? Modifier.PUBLIC : modifiers + Modifier.PUBLIC);
        	} finally {
        		if (wasFrozen) {
        			outerClass.freeze();
        		}
        	}
        }
    }
    
    public static CtClass getDeclaringClass(CtClass clazz) {
         try {
             return clazz.getDeclaringClass();
         }
         catch (NotFoundException e) {
             return null;
         }
    }

}
