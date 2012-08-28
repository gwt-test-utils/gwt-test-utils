package com.googlecode.gwt.test;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.server.rpc.AbstractRemoteServiceServlet;
import com.googlecode.gwt.test.exceptions.GwtTestConfigurationException;
import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;
import com.googlecode.gwt.test.internal.BrowserEventLoopSimulatorImpl;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.internal.handlers.GwtTestGWTBridge;
import com.googlecode.gwt.test.internal.i18n.DictionaryUtils;
import com.googlecode.gwt.test.rpc.ServletMockProvider;
import com.googlecode.gwt.test.uibinder.UiObjectTagFactory;
import com.googlecode.gwt.test.utils.events.Browser;
import com.googlecode.gwt.test.utils.events.Browser.BrowserErrorHandler;

public abstract class GwtModuleRunnerAdapter implements GwtModuleRunner, AfterTestCallback {

   private final class BrowserErrorHandlerDelegate implements BrowserErrorHandler {

      private BrowserErrorHandler customHandler;
      private BrowserErrorHandler defaultHandler;

      public void onError(String errorMessage) {
         // remove pending tasks, no need to execute
         browserEventLoopSimulator.clearPendingCommands();

         if (customHandler != null) {
            customHandler.onError(errorMessage);
         } else {
            ensureDefault().onError(errorMessage);
         }
      }

      private BrowserErrorHandler ensureDefault() {
         if (defaultHandler == null) {
            defaultHandler = GwtModuleRunnerAdapter.this.getDefaultBrowserErrorHandler();
            if (defaultHandler == null) {
               throw new GwtTestConfigurationException(
                        "You have to provide a non null instance of "
                                 + BrowserErrorHandler.class.getSimpleName() + " when overriding "
                                 + GwtModuleRunnerAdapter.class.getName()
                                 + ".getDefaultBrowserErrorHandler()");
            }
         }
         return defaultHandler;
      }

   }

   private static final String DEFAULT_WAR_DIR = "war/";
   private static final String MAVEN_DEFAULT_RES_DIR = "src/main/resources/";
   private static final String MAVEN_DEFAULT_WEB_DIR = "src/main/webapp/";

   private final BrowserErrorHandlerDelegate browserErrorHandlerDelegate;
   private final BrowserEventLoopSimulatorImpl browserEventLoopSimulator;
   private boolean canDispatchEventsOnDetachedWidgets;
   private final Map<String, String> clientProperties;
   private Locale locale;
   private GwtLogHandler logHandler;
   private ServletMockProvider servletMockProvider;
   private WindowOperationsHandler windowOperationsHandler;

   public GwtModuleRunnerAdapter() {
      browserErrorHandlerDelegate = new BrowserErrorHandlerDelegate();
      browserEventLoopSimulator = BrowserEventLoopSimulatorImpl.get();
      clientProperties = new HashMap<String, String>();
      AfterTestCallbackManager.get().registerRemoveableCallback(this);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.GwtModuleRunner#addClientProperty(java.lang.String ,
    * java.lang.String)
    */
   public final void addClientProperty(String propertyName, String value) {
      clientProperties.put(propertyName, value);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.GwtModuleRunner#addDictionaryEntries(java.lang. String,
    * java.util.Map)
    */
   public final void addDictionaryEntries(String dictionaryName, Map<String, String> entries) {

      Dictionary dictionary = Dictionary.getDictionary(dictionaryName);
      DictionaryUtils.addEntries(dictionary, entries);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.GwtModuleRunner#addGwtCreateHandler(com.googlecode .gwt.test
    * .GwtCreateHandler)
    */
   public final void addGwtCreateHandler(GwtCreateHandler gwtCreateHandler) {
      GwtTestGWTBridge.get().addGwtCreateHandler(gwtCreateHandler);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.GwtModuleRunner#addUiObjectTagFactory(com.googlecode .gwt
    * .test.uibinder.UiObjectTagFactory)
    */
   public final void addUiObjectTagFactory(UiObjectTagFactory<? extends IsWidget> factory) {
      GwtConfig.get().getUiObjectTagFactories().add(factory);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.internal.AfterTestCallback#afterTest()
    */
   public final void afterTest() throws Throwable {
      this.locale = null;
      this.windowOperationsHandler = null;
      this.browserErrorHandlerDelegate.customHandler = null;
      this.servletMockProvider = null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.GwtModuleRunner#canDispatchDomEventsOnDetachedWidgets ()
    */
   public final boolean canDispatchEventsOnDetachedWidgets() {
      return canDispatchEventsOnDetachedWidgets;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.GwtModuleRunner#ensureDebugId()
    */
   public boolean ensureDebugId() {
      return false;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.GwtModuleRunner#getBrowserErrorHandler()
    */
   public final BrowserErrorHandler getBrowserErrorHandler() {
      return browserErrorHandlerDelegate;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.GwtModuleRunner#getClientProperty(java.lang.String )
    */
   public final String getClientProperty(String propertyName) {
      return clientProperties.get(propertyName);
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.GwtModuleRunner#getClientPropertyNames()
    */
   public final Set<String> getClientPropertyNames() {
      return clientProperties.keySet();
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.GwtModuleRunner#getHostPagePath()
    */
   public final String getHostPagePath() {
      return getHostPagePath(GwtConfig.get().getTestedModuleName());
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.GwtModuleRunner#getLocale()
    */
   public final Locale getLocale() {
      return locale;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.GwtModuleRunner#getLogHandler()
    */
   public final GwtLogHandler getLogHandler() {
      return logHandler;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.GwtModuleRunner#getServletMockProvider()
    */
   public ServletMockProvider getServletMockProvider() {
      return servletMockProvider;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.googlecode.gwt.test.GwtModuleRunner#getWindowOperationsHandler()
    */
   public final WindowOperationsHandler getWindowOperationsHandler() {
      return windowOperationsHandler;
   }

   protected BrowserEventLoopSimulator getBrowserEventLoopSimulator() {
      return browserEventLoopSimulator;
   }

   protected abstract BrowserErrorHandler getDefaultBrowserErrorHandler();

   /**
    * Specifies the relative path in the project of the HTML file which is used by the corresponding
    * GWT module.
    * 
    * @param moduleFullQualifiedName The full qualifed name of the corresponding GWT module.
    * @return The relative path of the HTML file used, or null if there is no HTML file
    */
   protected String getHostPagePath(String moduleFullQualifiedName) {
      // try with gwt default structure
      String fileSimpleName = moduleFullQualifiedName.substring(moduleFullQualifiedName.lastIndexOf('.') + 1)
               + ".html";

      String fileRelativePath = DEFAULT_WAR_DIR + fileSimpleName;
      if (new File(fileRelativePath).exists()) {
         return fileRelativePath;
      }

      // try with the new maven archetype default path
      fileRelativePath = MAVEN_DEFAULT_WEB_DIR + fileSimpleName;
      if (new File(fileRelativePath).exists()) {
         return fileRelativePath;
      }

      // try with the old maven archetype default path
      String packagePath = moduleFullQualifiedName.substring(0,
               moduleFullQualifiedName.lastIndexOf('.') + 1).replaceAll("\\.", "/");

      fileRelativePath = MAVEN_DEFAULT_RES_DIR + packagePath + "public/" + fileSimpleName;
      if (new File(fileRelativePath).exists()) {
         return fileRelativePath;
      }

      // no HTML hostpage found
      return null;
   }

   /**
    * Specifies the callback to use when a simulated {@link Browser} action throws an error.
    * 
    * @param browserErrorHandler The custom browser error handler callback.
    */
   protected final void setBrowserErrorHandler(BrowserErrorHandler browserErrorHandler) {
      this.browserErrorHandlerDelegate.customHandler = browserErrorHandler;
   }

   /**
    * Specifies if The {@link Browser} helper methods can target not attached widgets or not.
    * 
    * @param canDispatchEventsOnDetachedWidgets True if {@link DomEvent} can be dispatched on
    *           detached widgets, false otherwise.
    * 
    * @see Widget#isAttached()
    */
   protected final void setCanDispatchEventsOnDetachedWidgets(
            boolean canDispatchEventsOnDetachedWidgets) {
      this.canDispatchEventsOnDetachedWidgets = canDispatchEventsOnDetachedWidgets;
   }

   /**
    * Specifies the locale to use when running the GWT module.
    * 
    * @param locale The specific locale to use for this module execution.
    */
   protected final void setLocale(Locale locale) {
      this.locale = locale;
   }

   /**
    * Specifies the callback to use to handle {@link GWT#log(String)} and
    * {@link GWT#log(String, Throwable)} calls.
    * 
    * @param logHandler The callback to use to handle GWT log method calls.
    */
   protected final void setLogHandler(GwtLogHandler logHandler) {
      this.logHandler = logHandler;
   }

   /**
    * Specifies the servlet mocks provider to use whenever a method from
    * {@link AbstractRemoteServiceServlet} is invoked.
    * 
    * @param servletMockProvider The mock provider to use in the running test.
    */
   protected final void setServletMockProvider(ServletMockProvider servletMockProvider) {
      this.servletMockProvider = servletMockProvider;
   }

   /**
    * Specifies the callback to use to handle {@link Window} method calls.
    * 
    * @param windowOperationsHandler The callback to use to handle {@link Window} method calls.
    */
   protected final void setWindowOperationsHandler(WindowOperationsHandler windowOperationsHandler) {
      this.windowOperationsHandler = windowOperationsHandler;
   }

}
