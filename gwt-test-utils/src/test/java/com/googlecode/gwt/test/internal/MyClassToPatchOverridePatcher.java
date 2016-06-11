package com.googlecode.gwt.test.internal;

import com.googlecode.gwt.test.internal.MyClassToPatch.MyInnerClass;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;

@PatchClass(MyClassToPatch.class)
public class MyClassToPatchOverridePatcher {

    @PatchClass(MyInnerClass.class)
    public static class MyInnerClassOverridePatcher {

        @PatchMethod(override = true)
        public static String getInnerString(MyInnerClass innerObject) {
            String value = GwtReflectionUtils.getPrivateFieldValue(innerObject, "new_string_attr");
            return "patched by " + MyInnerClassOverridePatcher.class.getSimpleName() + " : " + value;
        }

        @InitMethod(override = true)
        public static void initMyInnerClass(CtClass c) throws CannotCompileException {
            CtField field = CtField.make(
                    "private String new_string_attr = \"new field added in overrided init\";", c);
            c.addField(field);
        }
    }

    @PatchMethod(override = true)
    public static String myStringMethod(MyClassToPatch myClassToPatch, MyInnerClass innerObject) {
        return "myStringMethod has been patched by override patcher : "
                + innerObject.getInnerString();
    }
}
