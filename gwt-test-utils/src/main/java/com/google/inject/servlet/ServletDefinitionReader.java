package com.google.inject.servlet;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Internal reader for Guice Servlet definitions. <strong>For internal use only.</strong>
 *
 * @author Alex Dobjanschi
 * @author Gael Lazzari
 */
public class ServletDefinitionReader {
    private static final TypeLiteral<ServletDefinition> SERVLET_DEFS = TypeLiteral.get(ServletDefinition.class);

    private final Injector injector;

    private final Map<String, Key<? extends HttpServlet>> mapUriToServletKeys = new HashMap<String, Key<? extends HttpServlet>>();

    private final Map<String, HttpServlet> mapUriToServlets = new HashMap<String, HttpServlet>();

    public ServletDefinitionReader(Injector injector) {
        this.injector = injector;
        this.collectServletDefinitions(injector);
    }

    public HttpServlet getServletForPath(String uri) {
        for (Entry<String, Key<? extends HttpServlet>> entry : mapUriToServletKeys.entrySet()) {
            if (entry.getKey().matches(uri)) {
                // This is the entry, check if we have an instance ready
                // If not, then cache it. Most likely, a test will only be serviced
                // by a
                // few servlets, so it's a waste of resources to instantiate them
                // all.
                if (!mapUriToServlets.containsKey(entry.getKey())) {
                    try {
                        mapUriToServlets.put(entry.getKey(), injector.getInstance(entry.getValue()));
                    } catch (Throwable t) {
                        throw new GwtTestConfigurationException("cannot instantiate servlet", t);
                    }
                }

                return mapUriToServlets.get(entry.getKey());
            }
        }

        throw new GwtTestConfigurationException("Cannot find servlet mapped to: " + uri);
    }

    private void collectServletDefinitions(Injector injector) {
        for (Binding<ServletDefinition> entry : injector.findBindingsByType(SERVLET_DEFS)) {

            // Cache each definition here. Subsequent calls to the same url will
            // not
            // trigger multiple instances (Guice servlets are Singleton anyway).
            ServletDefinition def = entry.getProvider().get();
            Key<? extends HttpServlet> key = GwtReflectionUtils.getPrivateFieldValue(def, "servletKey");
            mapUriToServletKeys.put(def.getPattern(), key);
        }
    }

}
