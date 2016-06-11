package com.googlecode.gwt.test.internal.resources;

import com.google.gwt.resources.client.DataResource;
import com.google.gwt.safehtml.shared.UriUtils;

import java.lang.reflect.Method;

/**
 * Callback interface where {@link DataResource } methods calls are redirected. <strong>For internal
 * use only.</strong>
 *
 * @author Gael Lazzari
 */
class DataResourceCallback implements ResourcePrototypeCallback {

    private final String url;

    DataResourceCallback(String url) {
        this.url = url;
    }

    public Object call(Method method, Object[] args) throws Exception {
        if (method.getName().equals("getUrl")) {
            return url;
        } else if (method.getName().equals("getSafeUri")) {
            return UriUtils.fromTrustedString(url);
        }

        return null;

    }

}
