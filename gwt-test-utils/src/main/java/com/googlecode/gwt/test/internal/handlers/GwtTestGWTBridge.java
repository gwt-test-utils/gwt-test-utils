package com.googlecode.gwt.test.internal.handlers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWTBridge;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.GwtLogHandler;
import com.googlecode.gwt.test.GwtTreeLogger;
import com.googlecode.gwt.test.Mock;
import com.googlecode.gwt.test.exceptions.GwtTestDeferredBindingException;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.internal.GwtFactory;
import com.googlecode.gwt.test.internal.i18n.LocalizableResourceCreateHandler;
import com.googlecode.gwt.test.internal.resources.ClientBundleCreateHandler;
import com.googlecode.gwt.test.internal.resources.ImageBundleCreateHandler;
import com.googlecode.gwt.test.uibinder.UiBinderCreateHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * gwt-test-utils {@link GWTBridge} implementation, which manages an ordered list of
 * GwtCreateHandler where {@link GWT#create(Class)} instructions are delegated. <strong>For internal
 * use only.</strong>
 *
 * @author Gael Lazzari
 */
public class GwtTestGWTBridge extends GWTBridge implements AfterTestCallback {

    private static final GwtTestGWTBridge INSTANCE = new GwtTestGWTBridge();

    public static GwtTestGWTBridge get() {
        return INSTANCE;
    }

    private final List<GwtCreateHandler> addedHandlers;
    private final GwtCreateHandler animationSchedulerCreateHandler;
    private final GwtCreateHandler autoBeanCreateHandler;
    private final GwtCreateHandler cellBasedWidgetImplCreateHandler;
    private final GwtCreateHandler clientBundleCreateHander;
    private final GwtCreateHandler defaultGwtCreateHandler;
    private final GwtCreateHandler deferredReplaceWithCreateHandler;
    private final GwtCreateHandler generatorCreateHandler;
    private final GwtCreateHandler htmlTableImplCreateHandler;
    private final GwtCreateHandler imageBundleCreateHandler;
    private final GwtCreateHandler localizableResourceCreateHandler;
    private GwtCreateHandler mockCreateHandler;
    private final GwtCreateHandler placeHistoryMapperCreateHandler;
    private final GwtCreateHandler resizeLayoutPanelImplCreateHandler;
    private final TestRemoteServiceCreateHandler testRemoteServiceCreateHandler;
    private final GwtCreateHandler uiBinderCreateHandler;
    private final WebXmlRemoteServiceCreateHandler webXmlRemoteServiceCreateHandler;
    private boolean isClient = true;

    private GwtTestGWTBridge() {
        // TODO : all createHandler should be singleton ?
        generatorCreateHandler = new GeneratorCreateHandler(GwtFactory.get().getCompilationState(),
                GwtFactory.get().getModuleDef());
        addedHandlers = new ArrayList<>();
        animationSchedulerCreateHandler = new AnimationSchedulerCreateHandler();
        autoBeanCreateHandler = new AutoBeanCreateHandler();
        cellBasedWidgetImplCreateHandler = new CellBasedWidgetImplCreateHandler();
        clientBundleCreateHander = new ClientBundleCreateHandler();
        defaultGwtCreateHandler = new DefaultGwtCreateHandler();
        deferredReplaceWithCreateHandler = new DeferredReplaceWithCreateHandler();
        htmlTableImplCreateHandler = new HTMLTableImplCreateHandler();
        imageBundleCreateHandler = new ImageBundleCreateHandler();
        localizableResourceCreateHandler = new LocalizableResourceCreateHandler();
        placeHistoryMapperCreateHandler = new PlaceHistoryMapperCreateHandler();
        resizeLayoutPanelImplCreateHandler = new ResizeLayoutPanelImplCreateHandler();
        uiBinderCreateHandler = UiBinderCreateHandler.get();
        testRemoteServiceCreateHandler = TestRemoteServiceCreateHandler.get();
        webXmlRemoteServiceCreateHandler = new WebXmlRemoteServiceCreateHandler();

        AfterTestCallbackManager.get().registerCallback(this);
    }

    public void addGwtCreateHandler(GwtCreateHandler gwtCreateHandler) {
        addedHandlers.add(0, gwtCreateHandler);
    }

    public void afterTest() {
        addedHandlers.clear();
        testRemoteServiceCreateHandler.reset();
        mockCreateHandler = null;
        isClient = true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(Class<?> classLiteral) {
        for (GwtCreateHandler gwtCreateHandler : getGwtCreateHandlers()) {
            try {
                Object o = gwtCreateHandler.create(classLiteral);
                if (o != null) {
                    return (T) o;
                }
            } catch (GwtTestException e) {
                throw e;
            } catch (Exception e) {
                if (e instanceof UnableToCompleteException) {
                    GwtTreeLogger.get().onUnableToCompleteError();
                }

                throw new GwtTestPatchException("Error while creating instance of '"
                        + classLiteral.getName() + "' through '"
                        + gwtCreateHandler.getClass().getName() + "' instance", e);
            }
        }

        throw new GwtTestDeferredBindingException("No declared "
                + GwtCreateHandler.class.getSimpleName()
                + " has been able to create an instance of '" + classLiteral.getName()
                + "'. You should add our own with "
                + GwtConfig.get().getModuleRunner().getClass().getSimpleName()
                + ".addGwtCreateHandler(..) method or declared your tested object with @"
                + Mock.class.getSimpleName());
    }

    @Override
    public String getVersion() {
        return "GWT by gwt-test-utils";
    }

    @Override
    public boolean isClient() {
        return isClient;
    }

    public void setClient(Boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    public void log(String message, Throwable e) {
        GwtLogHandler logHandler = GwtConfig.get().getModuleRunner().getLogHandler();
        if (logHandler != null) {
            logHandler.log(message, e);
        }
    }

    public void setMockCreateHandler(GwtCreateHandler mockCreateHandler) {
        this.mockCreateHandler = mockCreateHandler;
    }

    private List<GwtCreateHandler> getGwtCreateHandlers() {
        List<GwtCreateHandler> list = new ArrayList<>();

        // declared @Mock objects creation
        if (mockCreateHandler != null) {
            list.add(mockCreateHandler);
        }

        // than, add all user custom createHandlers
        list.addAll(addedHandlers);

        // than, add custom deferred 'replace-with' bindings
        list.add(deferredReplaceWithCreateHandler);

        // finally, add all default gwt-test-utils createHandlers
        list.add(localizableResourceCreateHandler);
        list.add(clientBundleCreateHander);
        list.add(imageBundleCreateHandler);
        list.add(htmlTableImplCreateHandler);
        list.add(resizeLayoutPanelImplCreateHandler);
        list.add(uiBinderCreateHandler);
        list.add(testRemoteServiceCreateHandler);
        list.add(webXmlRemoteServiceCreateHandler);
        list.add(cellBasedWidgetImplCreateHandler);
        list.add(animationSchedulerCreateHandler);
        list.add(placeHistoryMapperCreateHandler);
        list.add(autoBeanCreateHandler);

        list.add(defaultGwtCreateHandler);

        list.add(generatorCreateHandler);

        return list;
    }

}
