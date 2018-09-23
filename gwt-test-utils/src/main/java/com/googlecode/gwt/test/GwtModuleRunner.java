package com.googlecode.gwt.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.server.rpc.AbstractRemoteServiceServlet;
import com.googlecode.gwt.test.rpc.ServletMockProvider;
import com.googlecode.gwt.test.uibinder.UiObjectTagFactory;
import com.googlecode.gwt.test.utils.events.Browser;
import com.googlecode.gwt.test.utils.events.Browser.BrowserErrorHandler;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Base interface for gwt-test-utils tests. It provides every necessary information to the framework
 * internals to emulate GWT without any browser, regardless of which unit test framework is used.
 * <strong>Every implementation must be annotated with {@link GwtModule} to provide the fully
 * qualified name of GWT module under test.</strong>
 * </p>
 *
 * @author Gael Lazzari
 */
public interface GwtModuleRunner {

    /**
     * Returns <code>true</code> if the <code>localStorage</code> part of the simulated Storage API
     * is expected to be supported for the running test. <strong><code>true</code> by
     * default.</strong>
     */
    boolean isLocalStorageSupported();

    /**
     * Returns <code>true</code> if the <code>sessionStorage</code> part of the Storage API is
     * expected to be supported for the running test. <strong><code>true</code> by default.</strong>
     */
    boolean isSessionStorageSupported();

    /**
     * Setup <code>GWT.isClient()</code> for one unit test. Default value is <code>true</code>.
     *
     * @param isClient <code>GWT.isClient()</code> value.
     */
    void setIsClient(boolean isClient);

    /**
     * Add a client's property, such as the browser 'user-agent' which could be use to simulate the
     * 'replace-with' deferred binding mechanism.
     *
     * @param propertyName The name of the client's property
     * @param value        The value of the client's property
     */
    void addClientProperty(String propertyName, String value);

    /**
     * Add String key/value pairs to a GWT {@link Dictionary}.
     *
     * @param dictionaryName The name of the {@link Dictionary} on which the entries should be added
     * @param entries        The Dictionary's entries to add
     */
    void addDictionaryEntries(String dictionaryName, Map<String, String> entries);

    /**
     * Declare a GwtCreateHandler to be a candidate for GWT deferred binding calls (
     * {@link GWT#create(Class)}).
     *
     * @param gwtCreateHandler The deferred binding candidate.
     */
    void addGwtCreateHandler(GwtCreateHandler gwtCreateHandler);

    /**
     * Declare a UiWidgetTagFactory to be a candidate to handle some widget declaration in a .ui.xml
     * UiBinder file.
     *
     * @param factory The UiBinder Widget factory candidate.
     */
    void addUiObjectTagFactory(UiObjectTagFactory<? extends IsWidget> factory);

    /**
     * Specifies if The {@link Browser} helper methods can target not attached widgets or not.
     *
     * @return True if {@link DomEvent} can be dispatched on detached widgets, false otherwise.
     * @see Widget#isAttached()
     */
    boolean canDispatchEventsOnDetachedWidgets();

    /**
     * Specifies if the module runner is allowed the setup of debug id.
     *
     * @return true if setting debug id should be enabled, false otherwise.
     * @see UIObject#ensureDebugId(com.google.gwt.dom.client.Element, String)
     * @see UIObject#ensureDebugId(String)
     */
    boolean ensureDebugId();

    /**
     * Specifies the callback to use when a simulated {@link Browser} action throws an error.
     *
     * @return The custom browser error handler callback.
     */
    BrowserErrorHandler getBrowserErrorHandler();

    /**
     * Retrieve a custom client's property, added through
     * {@link GwtModuleRunner#addClientProperty(String, String)} method
     *
     * @param propertyName The name of the client's property
     * @return The value of the client's property, or null if it does not exist
     */
    String getClientProperty(String propertyName);

    /**
     * Return the set of client's property added through
     * {@link GwtModuleRunner#addClientProperty(String, String)} method
     *
     * @return A set which contains all added client's property names
     */
    Set<String> getClientPropertyNames();

    /**
     * Specifies the relative path in the project of the HTML file which is used by the GWT module to
     * run.
     *
     * @return The relative path of the HTML file used.
     */
    String getHostPagePath();

    /**
     * Specifies the locale to use when running the GWT module.
     *
     * @return The specific locale to use for this module execution.
     */
    Locale getLocale();

    /**
     * Specifies the callback to use to handle {@link GWT#log(String)} and
     * {@link GWT#log(String, Throwable)} calls.
     *
     * @return The callback to use to handle GWT log method calls.
     */
    GwtLogHandler getLogHandler();

    /**
     * Get the list of handlers to trigger while invoking a RPC method.
     *
     * @return The list of handlers to trigger. <strong>Cannot be null</strong>
     */
    List<RemoteServiceExecutionHandler> getRemoteServiceExecutionHandlers();

    /**
     * Specifies the servlet mocks provider to use whenever a method from
     * {@link AbstractRemoteServiceServlet} is invoked.
     *
     * @return The servlet mock provider to use in the running test.
     */
    ServletMockProvider getServletMockProvider();

    /**
     * Specifies the callback to use to handle {@link Window} method calls.
     *
     * @return The callback to use to handle {@link Window} method calls.
     */
    WindowOperationsHandler getWindowOperationsHandler();

}
