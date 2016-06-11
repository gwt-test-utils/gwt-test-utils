package com.googlecode.gwt.test.uibinder.widget;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.DateLabel;
import com.googlecode.gwt.test.uibinder.UiObjectTag;
import com.googlecode.gwt.test.uibinder.UiObjectTagFactory;

import java.util.Map;

/**
 * Handles &lt;g:DateLabel> tags.
 *
 * @author Gael Lazzari
 */
public class UiDateLabelTagFactory implements UiObjectTagFactory<DateLabel> {

    private static class UiDateLabelTag extends UiObjectTag<DateLabel> {

        @Override
        protected void finalizeObject(DateLabel widget) {
            // nothing to do
        }

        @Override
        protected void initializeObject(DateLabel wrapped, Map<String, Object> attributes,
                                        Object owner) {
            // nothing to do
        }

        @Override
        protected DateLabel instanciate(Class<? extends DateLabel> clazz,
                                        Map<String, Object> attributes, Object owner) {

            if (clazz == DateLabel.class) {
                DateTimeFormat format = (DateTimeFormat) attributes.get("format");

                if (format != null) {
                    return new DateLabel(format);
                }

                String predefinedFormat = (String) attributes.get("predefinedFormat");
                if (predefinedFormat != null) {
                    PredefinedFormat predef = PredefinedFormat.valueOf(predefinedFormat);
                    return new DateLabel(DateTimeFormat.getFormat(predef));
                }

                String customFormat = (String) attributes.get("customFormat");
                if (customFormat != null) {
                    return new DateLabel(DateTimeFormat.getFormat(customFormat));
                }

            }

            // unable to use custom constructor or is a subclass of DateLabel, so
            // use
            // default mechanism
            return super.instanciate(clazz, attributes, owner);
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.uibinder.UiObjectTagFactory#createUiObjectTag (java.lang.Class,
     * java.util.Map)
     */
    public UiObjectTag<DateLabel> createUiObjectTag(Class<?> clazz, Map<String, Object> attributes) {

        if (DateLabel.class.isAssignableFrom(clazz)) {
            return new UiDateLabelTag();
        }

        return null;
    }

}
