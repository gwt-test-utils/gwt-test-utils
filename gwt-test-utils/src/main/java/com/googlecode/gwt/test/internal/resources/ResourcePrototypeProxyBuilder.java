package com.googlecode.gwt.test.internal.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.*;
import com.googlecode.gwt.test.exceptions.GwtTestResourcesException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder in charge of the creation of {@link ResourcePrototype} proxies. <strong>For internal use
 * only.</strong>
 *
 * @author Gael Lazzari
 */
public class ResourcePrototypeProxyBuilder {

    private static class ResourcePrototypeInvocationHandler implements InvocationHandler {

        private ResourcePrototypeCallback callback;
        private String name;
        private Class<?> ownerClass;
        private Class<?> proxiedClass;

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("getName".equals(method.getName())) {
                return name;
            } else if ("toString".equals(method.getName())) {
                return callback.getClass().getName() + " generated for '" + ownerClass.getName() + "."
                        + name + "()'";
            } else if ("hashCode".equals(method.getName())) {
                return callback.hashCode();
            } else if ("equals".equals(method.getName())) {
                return areEqual(callback, args[0]);
            } else {
                Object result = callback.call(method, args);
                if (result != null) {
                    return result;
                } else {
                    throw new GwtTestResourcesException("Not managed method '" + method.getName()
                            + "' for generated '" + proxiedClass.getName() + "' proxy");
                }
            }
        }

        private boolean areEqual(ResourcePrototypeCallback myCallback, Object object) {
            if (!Proxy.isProxyClass(object.getClass())) {
                return false;
            }

            InvocationHandler ih = Proxy.getInvocationHandler(object);

            if (ResourcePrototypeInvocationHandler.class != ih.getClass()) {
                return false;
            }

            ResourcePrototypeCallback itsCallback = ((ResourcePrototypeInvocationHandler) ih).callback;

            return myCallback.equals(itsCallback);
        }

    }

    public static ResourcePrototypeProxyBuilder createBuilder(Class<?> proxiedClass,
                                                              Class<?> ownerClass) {
        return new ResourcePrototypeProxyBuilder(proxiedClass, ownerClass);
    }

    private String name;
    private final Class<?> ownerClass;
    private final Class<?> proxiedClass;
    private String text;
    private List<URL> urls;

    private ResourcePrototypeProxyBuilder(Class<?> proxiedClass, Class<?> ownerClass) {
        if (!proxiedClass.isInterface()) {
            throw new GwtTestResourcesException("Cannot create a resource proxy instance for '"
                    + proxiedClass.getName() + "' because it is not an interface");
        }
        this.proxiedClass = proxiedClass;
        this.ownerClass = ownerClass;
    }

    public Object build() {

        ResourcePrototypeCallback callback;

        if (TextResource.class.isAssignableFrom(proxiedClass)) {
            callback = urls != null ? new TextResourceCallback(urls) : new TextResourceCallback(text);
        } else if (CssResource.class.isAssignableFrom(proxiedClass)) {
            callback = urls != null ? new CssResourceCallback(urls) : new CssResourceCallback(text);
        } else if (DataResource.class.isAssignableFrom(proxiedClass)) {
            callback = new DataResourceCallback(computeUrl(urls, ownerClass));
        } else if (ImageResource.class.isAssignableFrom(proxiedClass)) {
            callback = new ImageResourceCallback(computeImageUrl(urls, ownerClass));
        } else {
            throw new GwtTestResourcesException("Not managed return type for ClientBundle : "
                    + proxiedClass.getName());
        }

        InvocationHandler resourceInvocationHandler = generateInvocationHandler(callback);
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[]{proxiedClass},
                resourceInvocationHandler);
    }

    public ResourcePrototypeProxyBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ResourcePrototypeProxyBuilder resourceURL(URL url) {
        urls = new ArrayList<>(1);
        urls.add(url);
        return this;
    }

    public ResourcePrototypeProxyBuilder resourceURLs(List<URL> url) {
        this.urls = url;
        return this;
    }

    public ResourcePrototypeProxyBuilder text(String text) {
        this.text = text;
        return this;
    }

    private String computeImageUrl(List<URL> resourceURLs, Class<?> resourceClass) {
        if (resourceURLs.size() > 1) {
            throw new GwtTestResourcesException("Too many ImageResource files found for method '"
                    + ownerClass.getName() + "." + name + "()'");
        }

        return computeUrl(resourceURLs, resourceClass);

    }

    private String computeUrl(List<URL> resourceURLs, Class<?> resourceClass) {

        StringBuilder sb = new StringBuilder();

        for (URL url : resourceURLs) {
            sb.append(extractFileName(url, resourceClass)).append("_");
        }

        return GWT.getModuleBaseURL() + sb.substring(0, sb.length() - 1);
    }

    private String extractFileName(URL resourceURL, Class<?> resourceClass) {

        return resourceURL.getPath().substring(resourceURL.getPath().lastIndexOf('/') + 1);
    }

    private InvocationHandler generateInvocationHandler(final ResourcePrototypeCallback callback) {

        ResourcePrototypeInvocationHandler ih = new ResourcePrototypeInvocationHandler();
        ih.callback = callback;
        ih.name = name;
        ih.ownerClass = ownerClass;
        ih.proxiedClass = proxiedClass;

        return ih;
    }
}
