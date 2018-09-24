package com.googlecode.gwt.test.internal;

import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.UIObject.DebugIdImpl;
import com.google.gwt.user.client.ui.UIObject.DebugIdImplEnabled;
import com.googlecode.gwt.test.GwtModule;
import com.googlecode.gwt.test.GwtModuleRunner;
import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.uibinder.UiObjectTagFactory;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Internal configuration of gwt-test-utils. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public class GwtConfig implements AfterTestCallback {

    private static final GwtConfig INSTANCE = new GwtConfig();

    public static GwtConfig get() {
        return INSTANCE;
    }

    private final DebugIdImpl disabledInstance = new DebugIdImpl();
    private final DebugIdImpl enabledInstance = new DebugIdImplEnabled();
    private GwtModuleRunner gwtModuleRunner;
    private String moduleAlias;

    private String testedModuleName;

    private final List<UiObjectTagFactory<?>> uiObjectTagFactories = new ArrayList<>();

    private GwtConfig() {
        AfterTestCallbackManager.get().registerFinalCallback(this);
    }

    @Override
    public void afterTest() {
        gwtModuleRunner = null;
        uiObjectTagFactories.clear();
    }

    public String getModuleAlias() {
        return moduleAlias;
    }

    public GwtModuleRunner getModuleRunner() {
        return gwtModuleRunner;
    }

    public String getTestedModuleName() {
        return testedModuleName;
    }

    public List<UiObjectTagFactory<?>> getUiObjectTagFactories() {
        return uiObjectTagFactories;
    }

    public void setupGwtModule(Class<?> testClass) {
        GwtModule gwtModule = testClass.getAnnotation(GwtModule.class);

        if (gwtModule == null) {
            throw new GwtTestConfigurationException("The test class " + testClass.getName()
                    + " must be annotated with @" + GwtModule.class.getSimpleName()
                    + " to specify the fully qualified name of the GWT module to test");
        }

        String moduleName = gwtModule.value();

        if (moduleName == null || "".equals(moduleName.trim())) {
            throw new GwtTestConfigurationException("Incorrect value for @"
                    + GwtModule.class.getSimpleName() + " on " + testClass.getName() + ": "
                    + moduleName);
        }

        if (!GwtFactory.get().getConfigurationLoader().getGwtModules().contains(moduleName)) {
            throw new GwtTestConfigurationException(
                    "The tested @GwtModule '"
                            + moduleName
                            + "' configured in "
                            + testClass.getName()
                            + " has not been found. Did you forget to declare a 'gwt-module' property in your 'META-INF/gwt-test-utils.properties' configuration file ?");
        }

        this.testedModuleName = moduleName;
    }

    /**
     * Setup a GWT module to be run. <strong>This method must be run only once, at the very beginning
     * of the GWT module emulation.</strong>
     *
     * @param gwtModuleRunner The configuration of the module to be run.
     */
    public void setupInstance(GwtModuleRunner gwtModuleRunner) {
        if (this.gwtModuleRunner != null) {
            throw new GwtTestException(
                    "Because of the single-threaded nature of the GWT environment, gwt-test-utils tests can not be run in multiple thread at the same time");
        }

        this.gwtModuleRunner = gwtModuleRunner;
        this.moduleAlias = ModuleData.get(testedModuleName).getAlias();
        setupDebugIdImpl(gwtModuleRunner.ensureDebugId());
    }

    private void setupDebugIdImpl(boolean ensureDebugId) {
        DebugIdImpl debugIdImplToUse = ensureDebugId ? enabledInstance : disabledInstance;

        GwtReflectionUtils.setStaticField(UIObject.class, "debugIdImpl", debugIdImplToUse);
    }

}
