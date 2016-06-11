package com.googlecode.gwt.test.internal;

import com.googlecode.gwt.test.internal.MyClassToPatch.MyInnerClass;
import com.googlecode.gwt.test.internal.MyClassToPatchOverridePatcher.MyInnerClassOverridePatcher;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;

@PatchClass(MyClassToPatch.class)
class MyClassToPatchPatcher {

    @PatchClass(MyInnerClass.class)
    static class MyInnerClassPatcher {

        @PatchMethod
        static String getInnerString(MyInnerClass innerObject) {
            String value = GwtReflectionUtils.getPrivateFieldValue(innerObject, "new_string_attr");
            return "patched by " + MyInnerClassOverridePatcher.class.getSimpleName() + " : " + value;
        }

        @InitMethod
        static void initMyInnerClass(CtClass c) throws CannotCompileException {
            CtField field = CtField.make(
                    "private String new_string_attr = \"new field added in init\";", c);
            c.addField(field);
        }
    }

    @PatchMethod
    static String myStringMethod(MyClassToPatch myClassToPatch, MyInnerClass innerObject) {
        return "myStringMethod has been patched : " + innerObject.getInnerString();
    }

}
