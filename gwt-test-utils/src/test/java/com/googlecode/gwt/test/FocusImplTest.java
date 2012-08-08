package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.impl.FocusImpl;

public class FocusImplTest extends GwtTestTest {

   private Element e;
   private final FocusImpl focusImpl = FocusImpl.getFocusImplForWidget();

   @Before
   public void beforeFocusImplTest() {
      e = Document.get().createAnchorElement().cast();
   }

   @Test
   public void blur() {
      // just check blur(element) does not throw any exception
      focusImpl.blur(e);
   }

   @Test
   public void createFocusable() {
      // Act
      Element elem = focusImpl.createFocusable();

      // Assert
      assertEquals("div", elem.getTagName());
   }

   @Test
   public void focus() {
      // just check focus(element) does not throw any exception
      focusImpl.focus(e);
   }

   @Test
   public void tabIndex() {
      // Act
      focusImpl.setTabIndex(e, 3);

      // Assert
      assertEquals(3, focusImpl.getTabIndex(e));
   }

}
