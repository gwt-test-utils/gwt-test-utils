package com.googlecode.gwt.test.guice;

import com.google.gwt.core.client.GWT;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletDefinitionReader;
import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.internal.utils.WebXmlUtils;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

/**
 * Helper for gwt-test-utils / Guice integration. <strong>For internal use only.</strong>
 *
 * @author Alex Dobjanschi
 * @author Gael Lazzari
 */
class GwtGuiceHelper {

    private static final GwtGuiceHelper INSTANCE = new GwtGuiceHelper();
    private boolean hasSearchedInjector = false;
    private Injector injector;
    private ServletDefinitionReader servletDefinitionReader;

    private GwtGuiceHelper() {

    }

    public static GwtGuiceHelper get() {
        return INSTANCE;
    }

    Injector getInjector() {

        if (this.injector == null && !hasSearchedInjector) {
            for (String listenerClassName : WebXmlUtils.get().getListenerClasses()) {
                try {
                    Class<?> clazz = GwtReflectionUtils.getClass(listenerClassName);

                    if (GuiceServletContextListener.class.isAssignableFrom(clazz)) {
                        Object instance = GwtReflectionUtils.instantiateClass(clazz);
                        this.injector = GwtReflectionUtils.callPrivateMethod(instance, "getInjector");
                    }

                } catch (Exception e) {
                    throw new GwtTestConfigurationException(
                            "Error while parsing web.xml searching for a Guice Injector in a configured GuiceServletContextListener",
                            e);
                }
            }

            hasSearchedInjector = true;
        }
        // can be null
        return this.injector;
    }

    Object getRpcServiceFromInjector(Injector injector, Class<?> remoteServiceClass,
                                     String remoteServiceRelativePath) {

        if (servletDefinitionReader == null) {
            servletDefinitionReader = new ServletDefinitionReader(injector);
        }

        // Try from Guice injection.
        String moduleName = GWT.getModuleName();
        return servletDefinitionReader.getServletForPath("/" + moduleName + "/" + remoteServiceRelativePath);
    }

}
