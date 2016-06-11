package com.googlecode.gwt.test.internal;

import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.Translator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GwtTranslator implements Translator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GwtTranslator.class);

    private final ClassVisibilityModifier classVisibilityModifier;
    private final ConfigurationLoader configurationLoader;
    private final HasHTMLModifier hasHTMLModifier;
    private final HasNameModifier hasNameModifier;

    private final SerializableModifier serializableModifier;

    GwtTranslator(ConfigurationLoader configurationLoader) {
        this.configurationLoader = configurationLoader;
        this.hasHTMLModifier = new HasHTMLModifier();
        this.hasNameModifier = new HasNameModifier();
        this.classVisibilityModifier = new ClassVisibilityModifier();
        this.serializableModifier = new SerializableModifier();
    }

    public void onLoad(ClassPool pool, String className) throws NotFoundException {
        CtClass classToLoad = pool.get(className);
        patchClass(classToLoad);
    }

    public void start(ClassPool pool) throws NotFoundException {

    }

    private void applyJavaClassModifiers(CtClass ctClass) {
        try {
            // Apply internal modifiers
            serializableModifier.modify(ctClass);
            hasHTMLModifier.modify(ctClass);
            hasNameModifier.modify(ctClass);
            classVisibilityModifier.modify(ctClass);

        } catch (Exception e) {
            if (GwtTestException.class.isInstance(e)) {
                throw (GwtTestException) e;
            }

            throw new GwtTestPatchException(e);
        }

    }

    private void applyPatcher(CtClass classToPatch) {
        Patcher patcher = configurationLoader.getPatcherFactory().createPatcher(classToPatch);

        if (patcher != null) {
            LOGGER.debug("Apply '" + patcher.getClass().getName() + "'");
            try {
                GwtPatcherUtils.patch(classToPatch, patcher);
            } catch (Exception e) {
                if (GwtTestException.class.isInstance(e)) {
                    throw (GwtTestException) e;
                }

                throw new GwtTestPatchException("Error while patching class '" + classToPatch.getName()
                        + "'", e);
            }
        }
    }

    private void patchClass(CtClass classToModify) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Load class '" + classToModify.getName() + "'");
        }

        applyPatcher(classToModify);
        applyJavaClassModifiers(classToModify);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Class '" + classToModify.getName() + "' has been loaded");
        }
    }
}
