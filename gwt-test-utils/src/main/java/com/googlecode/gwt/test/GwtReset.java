package com.googlecode.gwt.test;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.util.List;

/**
 * Class in charge of reseting all necessary GWT internal objects after the execution of a unit
 * test. <strong>For internal use only.</strong>
 *
 * @author Bertrand Paquet
 * @author Gael Lazzari
 */
public class GwtReset {

    private static final GwtReset INSTANCE = new GwtReset();

    public static GwtReset get() {
        return INSTANCE;
    }

    private static void getStaticAndCallClear(Class<?> clazz, String fieldName) {
        GwtReflectionUtils.callPrivateMethod(
                GwtReflectionUtils.getStaticFieldValue(clazz, fieldName), "clear");
    }

    private GwtReset() {
    }

    public void reset() throws Exception {
        GwtTreeLogger.reset();
        //com.google.gwt.user.client.ui.impl.FocusImpl.implPanel
        //com.google.gwt.user.client.ui.impl.FocusImpl.implWidget
        //com.google.gwt.user.client.ui.UIObject.numberRegex
        //com.google.gwt.user.client.ui.UIObject.debugIdImpl
        //com.google.gwt.user.client.ui.ValueBoxBase.impl
        Object itemAnimation = GwtReflectionUtils.getStaticFieldValue(TreeItem.class, "itemAnimation");
        GwtReflectionUtils.setPrivateFieldValue(itemAnimation, "curItem", null);
        GwtReflectionUtils.setPrivateFieldValue(itemAnimation, "opening", true);
        GwtReflectionUtils.setPrivateFieldValue(itemAnimation, "scrollHeight", 0);
        //com.google.gwt.user.client.ui.TreeItem.BASE_INTERNAL_ELEM
        //com.google.gwt.user.client.ui.TreeItem.BASE_BARE_ELEM
        //com.google.gwt.user.client.ui.TreeItem.impl
        //com.google.gwt.user.client.ui.FormPanel.impl
        //com.google.gwt.user.cellview.client.CellList.DEFAULT_RESOURCES
        GwtReflectionUtils.setStaticField(AbstractHasData.class, "tmpElem", null);
        //com.google.gwt.user.cellview.client.CellTable.DEFAULT_RESOURCES
        GwtReflectionUtils.setStaticField(AbstractCellTable.class, "TABLE_IMPL", null);
        //com.google.gwt.user.cellview.client.AbstractCellTable.template
        //com.google.gwt.core.client.GWT.uncaughtExceptionHandler
        //com.google.gwt.core.shared.GWT.sGWTBridge
        Object animationSchedulerImpl = GwtReflectionUtils.getStaticFieldValue(AnimationScheduler.class, "instance");
        if (animationSchedulerImpl != null
                && animationSchedulerImpl.getClass().getName().equals("com.google.gwt.animation.client.AnimationSchedulerImplTimer")) {
            List<?> animationRequests = (List<?>) GwtReflectionUtils.getPrivateFieldValue(animationSchedulerImpl, "animationRequests");
            animationRequests.clear();
        }
        getStaticAndCallClear(Dictionary.class, "cache");
        getStaticAndCallClear(RootPanel.class, "rootPanels");
        getStaticAndCallClear(RootPanel.class, "widgetsToDetach");
        GwtReflectionUtils.setStaticField(RootLayoutPanel.class, "singleton", null);
        // FIXME : com.google.gwt.i18n.client.NumberFormat.defaultNumberConstants
        GwtReflectionUtils.setStaticField(NumberFormat.class, "cachedCurrencyFormat", null);
        GwtReflectionUtils.setStaticField(NumberFormat.class, "cachedDecimalFormat", null);
        GwtReflectionUtils.setStaticField(NumberFormat.class, "cachedPercentFormat", null);
        GwtReflectionUtils.setStaticField(NumberFormat.class, "cachedScientificFormat", null);
        GwtReflectionUtils.setStaticField(NumberFormat.class, "latinNumberConstants", null);
        //com.google.gwt.i18n.client.LocaleInfo.instance
        GwtReflectionUtils.setPrivateFieldValue(LocaleInfo.getCurrentLocale(), "dateTimeConstants", null);
        GwtReflectionUtils.setPrivateFieldValue(LocaleInfo.getCurrentLocale(), "dateTimeFormatInfo", null);
        GwtReflectionUtils.setPrivateFieldValue(LocaleInfo.getCurrentLocale(), "numberConstants", null);
        GwtReflectionUtils.setStaticField(Window.class, "handlers", null);
        GwtReflectionUtils.setStaticField(DisclosurePanel.class, "contentAnimation", null);
        GwtReflectionUtils.setStaticField(DeckPanel.class, "slideAnimation", null);
        //com.google.gwt.dom.builder.shared.HtmlBuilderFactory.instance
        //com.google.gwt.dom.builder.shared.ElementBuilderFactory.instance
        //com.google.gwt.dom.builder.shared.HtmlStylesBuilder.camelCaseMap
        //com.google.gwt.dom.builder.shared.HtmlStylesBuilder.camelCaseWord
        //com.google.gwt.dom.builder.shared.HtmlStylesBuilder.caseWord
        //com.google.gwt.safecss.shared.SafeStylesUtils.impl
        //com.google.gwt.user.client.ui.impl.FocusImplStandard.focusHandler
        GwtReflectionUtils.setStaticField(DOM.class, "currentEvent", null);
        GwtReflectionUtils.setStaticField(DOM.class, "sCaptureElem", null);
        GwtReflectionUtils.setStaticField(NativePreviewEvent.class, "singleton", null);
        //com.google.gwt.event.dom.client.DomEvent.registered
        //com.google.gwt.debug.client.DebugInfo.impl
        //com.google.gwt.core.client.impl.Impl.uncaughtExceptionHandlerForTest
        //com.google.gwt.user.client.ui.impl.ClippedImageImpl.template
        //com.google.gwt.user.client.ui.impl.ClippedImageImpl.draggableTemplate
        getStaticAndCallClear(Image.class, "prefetchImages");
        GwtReflectionUtils.setStaticField(Cookies.class, "cachedCookies", null);
        //com.google.gwt.user.client.ui.Hyperlink.impl
        //com.google.gwt.user.client.History.impl
        //com.google.gwt.user.client.History.historyEventSource
        GwtReflectionUtils.setStaticField(Window.Location.class, "listParamMap", null);
        //com.google.gwt.text.shared.testing.PassthroughRenderer.INSTANCE
        //com.google.gwt.text.shared.testing.PassthroughParser.INSTANCE
        //com.google.gwt.i18n.client.BidiPolicy.impl
        //com.google.gwt.thirdparty.streamhtmlparser.impl.InternalState.htmlStates
        //com.google.gwt.thirdparty.streamhtmlparser.impl.InternalState.javascriptStates
        GwtReflectionUtils.setStaticField(HTMLPanel.class, "hiddenDiv", null);
        //com.google.gwt.layout.client.LayoutImpl.fixedRuler
        //com.google.gwt.text.shared.SimpleSafeHtmlRenderer.instance
        //com.google.gwt.user.cellview.client.CellBasedWidgetImpl.impl
        //com.google.gwt.user.cellview.client.CellBasedWidgetImplStandard.dispatchNonBubblingEvent
        //com.google.gwt.dom.client.StyleInjector.flusher
        //com.google.gwt.i18n.client.CurrencyList$CurrencyListInstance.instance
        //com.google.gwt.user.client.ui.SplitLayoutPanel.glassElem
        //com.google.gwt.xml.client.impl.XMLParserImpl.impl
        //com.google.gwt.resources.client.CommonResources.instance

        Object commandExecutor = GwtReflectionUtils.getStaticFieldValue(Class.forName("com.google.gwt.user.client.DeferredCommand"),
                "commandExecutor");
        GwtReflectionUtils.callPrivateMethod(GwtReflectionUtils.getPrivateFieldValue(commandExecutor, "commands"), "clear");


        try {
            GwtReflectionUtils.setStaticField(Class.forName("com.google.gwt.user.client.Event$"), "handlers", null);
            GwtReflectionUtils.setStaticField(Class.forName("com.google.gwt.dom.client.Document$"), "doc", null);
        } catch (GwtTestException e) {
            // something goes wrong with Overlay types support, just ignore the reset
        }

        getStaticAndCallClear(DateTimeFormat.class, "cache");
    }
}
