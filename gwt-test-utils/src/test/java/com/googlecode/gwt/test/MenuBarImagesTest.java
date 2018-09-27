package com.googlecode.gwt.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.AbstractImagePrototype.ImagePrototypeElement;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar.MenuBarImages;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("deprecation")
public class MenuBarImagesTest extends GwtTestTest {

    private final MenuBarImages menuBarImages = GWT.create(MenuBarImages.class);

    @Test
    public void checkToString() {
        // Given
        AbstractImagePrototype proto = menuBarImages.menuBarSubMenuIcon();

        // When & Then
        assertThat(proto.toString()).isNotNull();
    }

    @Test
    public void createElement() {
        // Given
        AbstractImagePrototype proto = menuBarImages.menuBarSubMenuIcon();

        // When
        ImagePrototypeElement element = proto.createElement();

        // Then
        assertThat(element.getTagName()).isEqualTo("IMG");
        assertThat(element.toString()).isEqualTo("<img src=\"http://127.0.0.1:8888/gwt_test_utils_module/clear.cache.gif\" style=\"width: 0px; height: 0px; background:url(http://127.0.0.1: 8888/gwt_test_utils_module/menuBarSubMenuIcon.gif) no-repeat 0px 0px; \" border=\"0\" onload=\"null\"></img>");
    }

    @Test
    public void createImage() {
        // Given
        AbstractImagePrototype proto = menuBarImages.menuBarSubMenuIcon();

        // When
        Image image = proto.createImage();

        // Then
        assertThat(image.getUrl()).isEqualTo("http://127.0.0.1:8888/gwt_test_utils_module/menuBarSubMenuIcon.gif");
    }

    @Test
    public void getHTML() {
        // Given
        AbstractImagePrototype proto = menuBarImages.menuBarSubMenuIcon();

        // When
        String html = proto.getHTML();

        // Then
        assertThat(html).isEqualTo("<img src='http://127.0.0.1:8888/gwt_test_utils_module/clear.cache.gif' style='width:0.0px;height:0.0px;background:url(http://127.0.0.1:8888/gwt_test_utils_module/menuBarSubMenuIcon.gif) no-repeat 0px 0px;' border='0'>");
    }

}
