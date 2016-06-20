package com.google.inject.servlet;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Internal reader for Guice Servlet definitions. <strong>For internal use only.</strong>
 *
 * @author Alex Dobjanschi
 * @author Gael Lazzari
 */
public class ServletDefinitionReader {
    private static final TypeLiteral<ServletDefinition> SERVLET_DEFS = TypeLiteral.get(ServletDefinition.class);

    private final Injector injector;

    private final List<ServletDefinition> servletDefinitions;

    private final Map<ServletDefinition, HttpServlet> mapUriToServlets = new HashMap<>();

    public ServletDefinitionReader(Injector injector) {
        this.injector = injector;
        this.servletDefinitions = collectServletDefinitions(injector);
    }

    public HttpServlet getServletForPath(String uri) {
        for (ServletDefinition def : servletDefinitions) {
            if (def.shouldServe(uri)) {

                // This is the entry, check if we have an instance ready
                // If not, then cache it. Most likely, a test will only be serviced
                // by a few servlets, so it's a waste of resources to instantiate them all.
                if (!mapUriToServlets.containsKey(def)) {
                    Key<? extends HttpServlet> key = GwtReflectionUtils.getPrivateFieldValue(def, "servletKey");
                    try {
                        mapUriToServlets.put(def, injector.getInstance(key));
                    } catch (Throwable t) {
                        throw new GwtTestConfigurationException("cannot instantiate servlet", t);
                    }
                }

                return mapUriToServlets.get(def);
            }
        }

        throw new GwtTestConfigurationException("Cannot find servlet mapped to: " + uri);
    }

    private List<ServletDefinition> collectServletDefinitions(Injector injector) {
        List<ServletDefinition> servletDefinitions = new ArrayList<>();
        for (Binding<ServletDefinition> entry : injector.findBindingsByType(SERVLET_DEFS)) {
            // Cache each definition here. Subsequent calls to the same url will
            // not trigger multiple instances (Guice servlets are Singleton anyway).
            servletDefinitions.add(entry.getProvider().get());
        }

        return servletDefinitions;
    }

}
