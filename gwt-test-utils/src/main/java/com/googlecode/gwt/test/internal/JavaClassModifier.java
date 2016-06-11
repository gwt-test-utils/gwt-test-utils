package com.googlecode.gwt.test.internal;

import javassist.CtClass;

interface JavaClassModifier {

    public void modify(CtClass classToModify) throws Exception;

}
