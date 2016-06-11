package com.googlecode.gwt.test.internal;

import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.googlecode.gwt.test.finder.GwtFinder;
import javassist.*;

class HasHTMLModifier implements JavaClassModifier {

    private final CtClass hasHTMLCtClass;
    private final CtClass hasTextCtClass;
    private final CtClass[] setArgs;

    HasHTMLModifier() {
        hasTextCtClass = GwtClassPool.getCtClass(HasText.class);
        hasHTMLCtClass = GwtClassPool.getCtClass(HasHTML.class);
        setArgs = new CtClass[]{GwtClassPool.getCtClass(String.class)};
    }

    public void modify(CtClass classToModify) throws Exception {

        if (classToModify.isInterface()) {
            return;
        }

        if (classToModify.subtypeOf(hasHTMLCtClass)) {
            instrumentSetText(classToModify);
            instrumentSetHTML(classToModify);
        } else if (classToModify.subtypeOf(hasTextCtClass)) {
            instrumentSetText(classToModify);
        }
    }

    private void addHTMLField(CtClass c) throws CannotCompileException {
        CtField oldHTMLField = CtField.make("protected String instrument_oldHTML;", c);
        c.addField(oldHTMLField);
    }

    private void addTextField(CtClass c) throws CannotCompileException {
        CtField oldTextField = CtField.make("protected String instrument_oldText;", c);
        c.addField(oldTextField);
    }

    private void instrumentSetHTML(CtClass c) throws CannotCompileException {
        // add behavior to HasHTML.setHTML method
        try {
            CtMethod setHTML = c.getDeclaredMethod("setHTML", setArgs);
            addHTMLField(c);
            setHTML.insertBefore("this.instrument_oldHTML = this.getHTML();");
            setHTML.insertAfter(GwtFinder.class.getName()
                    + ".onSetHTML(this, $1, this.instrument_oldHTML);");
        } catch (NotFoundException e) {
            // don't instrument method if not existing
        }
    }

    private void instrumentSetText(CtClass c) throws CannotCompileException {
        // add behavior to HasHTML.setHTML method
        try {
            CtMethod setText = c.getDeclaredMethod("setText", setArgs);
            addTextField(c);
            setText.insertBefore("this.instrument_oldText = this.getText();");
            setText.insertAfter(GwtFinder.class.getName()
                    + ".onSetText(this, $1, this.instrument_oldText);");
        } catch (NotFoundException e) {
            // don't instrument method if not existing
        }
    }

}
