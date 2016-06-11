package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.user.client.ui.RadioButton;
import com.googlecode.gwt.test.internal.utils.RadioButtonManager;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

@PatchClass(RadioButton.class)
class RadioButtonPatcher {

    @InitMethod
    static void initClass(CtClass c) throws CannotCompileException, NotFoundException {

        // add overrided RadioButton.setValue method
        CtMethod setValue = CtMethod.make(
                "public void setValue(Boolean value, boolean fireEvents) { super.setValue($1, $2); "
                        + RadioButtonManager.class.getName()
                        + ".onRadioGroupChanged(this, $1, $2); }", c);
        c.addMethod(setValue);

        // add behavior to RadioButton.setName method
        CtMethod setName = c.getMethod("setName", "(Ljava/lang/String;)V");
        setName.insertBefore(RadioButtonManager.class.getName() + ".beforeSetName(this, $1);");

        // Add overrided RadioButton.onLoad method
        CtMethod onLoad = CtMethod.make("protected void onLoad() { super.onLoad(); "
                + RadioButtonManager.class.getName() + ".onLoad(this); }", c);
        c.addMethod(onLoad);

        // Add overrided RadioButton.onUnLoad method
        CtMethod onUnload = CtMethod.make("protected void onUnLoad() { super.onUnload(); "
                + RadioButtonManager.class.getName() + ".onUnload(this); }", c);
        c.addMethod(onUnload);
    }

}
