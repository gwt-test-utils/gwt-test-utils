package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.googlecode.gwt.test.resources.MyClientBundle;

public class ImageTest extends GwtTestTest {

   @Test
   public void constructor() {
      // Arrange
      ImageResource imageRessource = MyClientBundle.INSTANCE.imageResource();

      // Act
      Image i = new Image(imageRessource);

      // Assert
      assertEquals("http://127.0.0.1:8888/gwt_test_utils_module/imageResource.gif", i.getUrl());
      assertEquals(0, i.getOriginLeft());
      assertEquals(0, i.getOriginTop());
      assertEquals(0, i.getWidth());
      assertEquals(0, i.getHeight());
   }

   @Test
   public void getElement() {
      // Assert
      Image i = new Image();

      // Act
      Element e = i.getElement();

      assertNotNull(e);
      assertEquals("img", e.getTagName());
   }

   @Test
   public void height() {
      // Arrange
      Image i = new Image();

      // Act
      i.setHeight("20");

      // Assert
      assertEquals(20, i.getHeight());
   }

   @Test
   public void height_Em() {
      // Arrange
      Image i = new Image();

      // Act
      i.setHeight("20em");

      // Assert
      assertEquals(20, i.getHeight());
   }

   @Test
   public void height_Px() {
      // Arrange
      Image i = new Image();
      // Pre-Assert
      assertEquals(0, i.getHeight());

      // Act
      i.setHeight("20px");

      // Assert
      assertEquals(20, i.getHeight());
   }

   @Test
   public void title() {
      // Arrange
      Image i = new Image();

      // Act
      i.setTitle("title");

      // Assert
      assertEquals("title", i.getTitle());
   }

   @Test
   public void url() {
      // Arrange
      Image i = new Image("http://my-url");
      // Pre-Assert
      assertEquals("http://my-url", i.getUrl());

      // Act
      i.setUrl("newURL");

      // Assert
      assertEquals("newURL", i.getUrl());
   }

   @Test
   public void visible() {
      // Arrange
      Image i = new Image();
      // Pre-Assert
      assertEquals(true, i.isVisible());

      // Act
      i.setVisible(false);

      // Assert
      assertEquals(false, i.isVisible());
   }

   @Test
   public void width() {
      // Arrange
      Image i = new Image();
      // Pre-Assert
      assertEquals(0, i.getWidth());

      // Act
      i.setWidth("20");

      // Assert
      assertEquals(20, i.getWidth());
   }

   @Test
   public void width_Em() {
      Image i = new Image();
      // Pre-Assert
      assertEquals(0, i.getWidth());

      // Act
      i.setWidth("20em");

      // Assert
      assertEquals(20, i.getWidth());
   }

   @Test
   public void width_Px() {
      // Arrange
      Image i = new Image();
      // Pre-Assert
      assertEquals(0, i.getWidth());

      // Act
      i.setWidth("20px");

      // Assert
      assertEquals(20, i.getWidth());
   }

}
