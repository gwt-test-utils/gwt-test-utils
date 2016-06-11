package com.googlecode.gwt.test.internal.resources;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.UriUtils;

import java.lang.reflect.Method;

/**
 * Callback interface where {@link ImageResource } methods calls are redirected. <strong>For internal
 * use only.</strong>
 *
 * @author Gael Lazzari
 */
class ImageResourceCallback implements ResourcePrototypeCallback {

    private final String url;

    ImageResourceCallback(String url) {
        this.url = url;
    }

    public Object call(Method method, Object[] args) throws Exception {
        if (method.getName().equals("getURL")) {
            return url;
        } else if (method.getName().equals("getSafeUri")) {
            return UriUtils.fromTrustedString(url);
        } else if (method.getName().equals("getHeight")) {
            return 0;
        } else if (method.getName().equals("getLeft")) {
            return 0;
        } else if (method.getName().equals("getWidth")) {
            return 0;
        } else if (method.getName().equals("getTop")) {
            return 0;
        } else if (method.getName().equals("isAnimated")) {
            return false;
        }

        return null;

    }

}
