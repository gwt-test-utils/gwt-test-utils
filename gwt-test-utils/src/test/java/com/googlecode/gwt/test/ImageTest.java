package com.googlecode.gwt.test;

import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.googlecode.gwt.test.resources.MyClientBundle;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageTest extends GwtTestTest {

    @Test
    public void constructor() {
        // Given
        ImageResource imageRessource = MyClientBundle.INSTANCE.imageResource();

        // When
        Image i = new Image(imageRessource);

        // Then
        assertThat(i.getUrl()).isEqualTo("http://127.0.0.1:8888/gwt_test_utils_module/imageResource.gif");
        assertThat(i.getOriginLeft()).isEqualTo(0);
        assertThat(i.getOriginTop()).isEqualTo(0);
        assertThat(i.getWidth()).isEqualTo(0);
        assertThat(i.getHeight()).isEqualTo(0);
    }

    @Test
    public void getElement() {
        // Then
        Image i = new Image();

        // When
        Element e = i.getElement();

        assertThat(e).isNotNull();
        assertThat(e.getTagName()).isEqualTo("img");
    }

    @Test
    public void height() {
        // Given
        Image i = new Image();

        // When
        i.setHeight("20");

        // Then
        assertThat(i.getHeight()).isEqualTo(20);
    }

    @Test
    public void height_Em() {
        // Given
        Image i = new Image();

        // When
        i.setHeight("20em");

        // Then
        assertThat(i.getHeight()).isEqualTo(20);
    }

    @Test
    public void height_Px() {
        // Given
        Image i = new Image();
        // Preconditions
        assertThat(i.getHeight()).isEqualTo(0);

        // When
        i.setHeight("20px");

        // Then
        assertThat(i.getHeight()).isEqualTo(20);
    }

    @Test
    public void title() {
        // Given
        Image i = new Image();

        // When
        i.setTitle("title");

        // Then
        assertThat(i.getTitle()).isEqualTo("title");
    }

    @Test
    public void url() {
        // Given
        Image i = new Image("http://my-url");
        // Preconditions
        assertThat(i.getUrl()).isEqualTo("http://my-url");

        // When
        i.setUrl("newURL");

        // Then
        assertThat(i.getUrl()).isEqualTo("newURL");
    }

    @Test
    public void visible() {
        // Given
        Image i = new Image();
        // Preconditions
        assertThat(i.isVisible()).isEqualTo(true);

        // When
        i.setVisible(false);

        // Then
        assertThat(i.isVisible()).isEqualTo(false);
    }

    @Test
    public void width() {
        // Given
        Image i = new Image();
        // Preconditions
        assertThat(i.getWidth()).isEqualTo(0);

        // When
        i.setWidth("20");

        // Then
        assertThat(i.getWidth()).isEqualTo(20);
    }

    @Test
    public void width_Em() {
        Image i = new Image();
        // Preconditions
        assertThat(i.getWidth()).isEqualTo(0);

        // When
        i.setWidth("20em");

        // Then
        assertThat(i.getWidth()).isEqualTo(20);
    }

    @Test
    public void width_Px() {
        // Given
        Image i = new Image();
        // Preconditions
        assertThat(i.getWidth()).isEqualTo(0);

        // When
        i.setWidth("20px");

        // Then
        assertThat(i.getWidth()).isEqualTo(20);
    }

}
