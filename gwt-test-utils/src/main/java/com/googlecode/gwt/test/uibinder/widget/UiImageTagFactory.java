package com.googlecode.gwt.test.uibinder.widget;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.googlecode.gwt.test.uibinder.UiObjectTag;
import com.googlecode.gwt.test.uibinder.UiObjectTagFactory;

import java.util.Map;

/**
 * Handles &lt;g:Image> tags.
 *
 * @author Gael Lazzari
 */
public class UiImageTagFactory implements UiObjectTagFactory<Image> {

    private static class UiImageTag extends UiObjectTag<Image> {

        @Override
        protected void finalizeObject(Image widget) {
            // noting to do
        }

        @Override
        protected void initializeObject(Image wrapped, Map<String, Object> attributes, Object owner) {
            // nothing to do
        }

        @Override
        protected Image instanciate(Class<? extends Image> clazz, Map<String, Object> attributes,
                                    Object owner) {

            if (clazz == Image.class) {

                ImageResource imageResource = (ImageResource) attributes.get("resource");
                if (imageResource != null) {
                    return new Image(imageResource);
                }

                String url = (String) attributes.get("url");
                if (url != null) {
                    return new Image(url);
                }

            }

            // unable to use custom constructor or is a subclass of Image, so use
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
    public UiObjectTag<Image> createUiObjectTag(Class<?> clazz, Map<String, Object> attributes) {

        if (Image.class.isAssignableFrom(clazz)) {
            return new UiImageTag();
        }

        return null;
    }

}
