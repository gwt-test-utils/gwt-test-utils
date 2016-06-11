package com.googlecode.gwt.test.internal.resources;

import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.resources.client.CssResource;
import com.googlecode.gwt.test.internal.resources.CssResourceReader.CssParsingResult;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

/**
 * Callback interface where {@link CssResource } methods calls are redirected. <strong>For internal
 * use only.</strong>
 *
 * @author Gael Lazzari
 */
class CssResourceCallback implements ResourcePrototypeCallback {

    private static interface CssReader {

        CssParsingResult readCss() throws Exception;

        String readCssText() throws Exception;
    }

    private boolean alreadyInjected = false;

    private final CssReader cssReader;

    CssResourceCallback(final List<URL> resourceURLs) {

        cssReader = new CssReader() {

            public CssParsingResult readCss() throws Exception {
                return CssResourceReader.get().readCss(resourceURLs);
            }

            public String readCssText() throws Exception {
                return TextResourceReader.get().readFiles(resourceURLs);
            }

        };
    }

    CssResourceCallback(final String text) {

        cssReader = new CssReader() {

            public CssParsingResult readCss() throws Exception {
                return CssResourceReader.get().readCss(text);
            }

            public String readCssText() throws Exception {
                return text;
            }

        };
    }

    public Object call(Method method, Object[] args) throws Exception {
        if (method.getName().equals("getText")) {
            return cssReader.readCssText();
        } else if (method.getName().equals("ensureInjected")) {
            return ensureInjected();
        } else {
            return handleCustomMethod(method.getName());
        }

    }

    private boolean ensureInjected() throws Exception {
        if (!alreadyInjected) {
            StyleInjector.inject(cssReader.readCssText());
            alreadyInjected = true;
            return true;
        }
        return false;
    }

    private String handleCustomMethod(String methodName) throws Exception {
        CssParsingResult result = cssReader.readCss();
        String constant = result.getConstantValue(methodName);
        if (constant != null) {
            return constant;
        } else {
            return methodName;
        }
    }
}
