package com.googlecode.gwt.test.uibinder;

import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.uibinder.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Default UiWidgetTagFactory, which try to delegate {@link UiObjectTag} instanciation to
 * UiWidgetTagFactories added by users before using those implemented in gwt-test-utils.
 *
 * @author Gael Lazzari
 */
class DefaultUiWidgetTagFactory implements UiObjectTagFactory<Object> {

    private static final DefaultUiWidgetTagFactory INSTANCE = new DefaultUiWidgetTagFactory();

    public static DefaultUiWidgetTagFactory get() {
        return INSTANCE;
    }

    private final List<UiObjectTagFactory<?>> gwtTestUtilsFactories;

    private DefaultUiWidgetTagFactory() {
        gwtTestUtilsFactories = new ArrayList<>();

        gwtTestUtilsFactories.add(new UiHTMLPanelTagFactory());
        gwtTestUtilsFactories.add(new UiCellPanelTagFactory());
        gwtTestUtilsFactories.add(new UiGridTagFactory());
        gwtTestUtilsFactories.add(new UiListBoxTagFactory());
        gwtTestUtilsFactories.add(new UiDateLabelTagFactory());
        gwtTestUtilsFactories.add(new UiDockLayoutPanelTagFactory());
        gwtTestUtilsFactories.add(new UiImageTagFactory());
        gwtTestUtilsFactories.add(new UiLayoutPanelTagFactory());
        gwtTestUtilsFactories.add(new UiMenuBarTagFactory());
        gwtTestUtilsFactories.add(new UiMenuItemTagFactory());
        gwtTestUtilsFactories.add(new UiStackLayoutPanelTagFactory());
        gwtTestUtilsFactories.add(new UiTabLayoutPanelTagFactory());
        gwtTestUtilsFactories.add(new UiDisclosurePanelTagFactory());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.uibinder.UiObjectTagFactory#createUiObjectTag(java .lang .Class,
     * java.util.Map)
     */
    public UiObjectTag<Object> createUiObjectTag(Class<?> clazz, Map<String, Object> attributes) {

        // try with user's custom UiObjectTagFactories
        UiObjectTag<Object> result = tryInstanciateUiObjectTag(clazz, attributes,
                GwtConfig.get().getUiObjectTagFactories());

        if (result != null) {
            return result;
        }

        // try with gwt-test-utils custom UiObjectTagFactories
        result = tryInstanciateUiObjectTag(clazz, attributes, gwtTestUtilsFactories);
        if (result != null) {
            return result;
        }

        // default
        return new UiObjectTag<Object>() {

            @Override
            protected void finalizeObject(Object widget) {
                // nothing to do
            }

            @Override
            protected void initializeObject(Object wrapped, Map<String, Object> attributes,
                                            Object owner) {
                // nothing to do

            }

        };
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private UiObjectTag<Object> tryInstanciateUiObjectTag(Class<?> clazz,
                                                          Map<String, Object> attributes, List<UiObjectTagFactory<?>> uiObjectTagFactories) {
        for (UiObjectTagFactory factory : uiObjectTagFactories) {

            UiObjectTag<Object> result = factory.createUiObjectTag(clazz, attributes);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

}
