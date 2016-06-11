package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window.Location;
import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

@PatchClass(Location.class)
class LocationPatcher {

    /**
     * Holder for a resetable URL
     */
    private static class UrlHolder implements AfterTestCallback {

        private URL url;

        private UrlHolder() {
            AfterTestCallbackManager.get().registerCallback(this);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.googlecode.gwt.test.internal.AfterTestCallback#afterTest()
         */
        public void afterTest() throws Throwable {
            url = null;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationPatcher.class);

    private static UrlHolder urlHolder = new UrlHolder();

    @PatchMethod
    static void assign(String newURL) {
        try {
            urlHolder.url = new URL(urlHolder.url, newURL);
        } catch (MalformedURLException e) {
            GWT.log("Failed to assign new URL '" + newURL + "'", e);
        }
    }

    @PatchMethod
    static String getHost() {
        URL urlToUse = getURLToUse();
        return urlToUse.getHost() + ":" + getPort(urlToUse);
    }

    @PatchMethod
    static String getHostName() {
        return getURLToUse().getHost();
    }

    @PatchMethod
    static String getHref() {
        return getURLToUse().toString();
    }

    @PatchMethod
    static String getPath() {
        return getURLToUse().getPath();
    }

    @PatchMethod
    static String getPort() {
        return getPort(getURLToUse());
    }

    @PatchMethod
    static String getProtocol() {
        return getURLToUse().getProtocol() + ":";
    }

    @PatchMethod
    static void replace(String newURL) {
        assign(newURL);
    }

    private static String computePath() {
        String absolutePath = GwtConfig.get().getModuleRunner().getHostPagePath();
        if (absolutePath == null) {
            LOGGER.warn("The host page path for '"
                    + GwtConfig.get().getModuleAlias()
                    + "' is null, fallback to an empty HTML document instead. You may want to override "
                    + GwtConfig.get().getModuleRunner().getClass().getSimpleName()
                    + ".getHostPagePath(String moduleFullQualifiedName) method to specify the relative path of the your HTML file from the root directory of your java project");

            absolutePath = "index.html";
        }

        int token = absolutePath.lastIndexOf("/") + 1;

        return (token > 0) ? absolutePath.substring(token) : absolutePath;
    }

    private static String getPort(URL url) {
        int port = url.getPort();

        return port != -1 ? String.valueOf(getURLToUse().getPort()) : "80";
    }

    private static URL getURLToUse() {
        if (urlHolder.url == null) {
            try {
                urlHolder.url = new URL(GWT.getHostPageBaseURL() + computePath() + Location.getHash());
            } catch (MalformedURLException e) {
                throw new GwtTestConfigurationException(
                        "GWT.getHostPageBaseURL() has failed to be parsed in a " + URL.class.getName()
                                + " instance", e);
            }
        }

        return urlHolder.url;

    }

}
