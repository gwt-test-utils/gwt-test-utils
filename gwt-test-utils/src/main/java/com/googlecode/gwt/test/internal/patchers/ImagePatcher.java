package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.ui.Image;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@PatchClass(Image.class)
class ImagePatcher {

    private static final Pattern PATTERN = Pattern.compile("^(\\d+).*$");

    @PatchMethod
    static int getHeight(Image image) {
        return getDim(image, "height");
    }

    @PatchMethod
    static int getWidth(Image image) {
        return getDim(image, "width");
    }

    private static int getDim(Image image, String dim) {
        ImageElement elem = image.getElement().cast();
        String width = elem.getStyle().getProperty(dim);
        if (width == null) {
            return 0;
        }
        Matcher m = PATTERN.matcher(width);
        if (m.matches()) {
            return Integer.parseInt(m.group(1));
        }
        return 0;
    }

}
