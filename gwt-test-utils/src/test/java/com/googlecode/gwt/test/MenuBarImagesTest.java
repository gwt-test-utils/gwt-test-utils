package com.googlecode.gwt.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.AbstractImagePrototype.ImagePrototypeElement;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar.MenuBarImages;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("deprecation")
public class MenuBarImagesTest extends GwtTestTest {

    private final MenuBarImages menuBarImages = GWT.create(MenuBarImages.class);

    @Test
    public void checkToString() {
        // Arrange
        AbstractImagePrototype proto = menuBarImages.menuBarSubMenuIcon();

        // Act & Assert
        assertNotNull(proto.toString());
    }

    @Test
    public void createElement() {
        // Arrange
        AbstractImagePrototype proto = menuBarImages.menuBarSubMenuIcon();

        // Act
        ImagePrototypeElement element = proto.createElement();

        // Assert
        assertEquals("IMG", element.getTagName());
        assertEquals(
                "<img onload=\"this.__gwtLastUnhandledEvent=\"load\";\" src=\"http://127.0.0.1:8888/gwt_test_utils_module/clear.cache.gif\" style=\"width: 0px; height: 0px; background:url(http://127.0.0.1: 8888/gwt_test_utils_module/menuBarSubMenuIcon.gif) no-repeat 0px 0px; \" border=\"0\"></img>",
                element.toString());
    }

    @Test
    public void createImage() {
        // Arrange
        AbstractImagePrototype proto = menuBarImages.menuBarSubMenuIcon();

        // Act
        Image image = proto.createImage();

        // Assert
        assertEquals("http://127.0.0.1:8888/gwt_test_utils_module/menuBarSubMenuIcon.gif",
                image.getUrl());
    }

    @Test
    public void getHTML() {
        // Arrange
        AbstractImagePrototype proto = menuBarImages.menuBarSubMenuIcon();

        // Act
        String html = proto.getHTML();

        // Assert
        assertEquals(
                "<img onload='this.__gwtLastUnhandledEvent=\"load\";' src='http://127.0.0.1:8888/gwt_test_utils_module/clear.cache.gif' style='width:0.0px;height:0.0px;background:url(http://127.0.0.1:8888/gwt_test_utils_module/menuBarSubMenuIcon.gif) no-repeat 0px 0px;' border='0'>",
                html);
    }

}
