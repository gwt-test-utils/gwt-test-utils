package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.CustomButton;
import com.google.gwt.user.client.ui.PushButton;

@GwtModule("com.googlecode.gwt.test.GwtTestUtils")
public class CompositeWithMockitoTest extends GwtTestWithMockito {

   public class MyComposite extends Composite {

      public MyComposite(CustomButton button) {
         initWidget(button);
      }
   }

   @com.googlecode.gwt.test.Mock
   private Element element;

   @Mock
   private PushButton injectedButton;

   @Test
   public void testComposite() {
      // Arrange
      Mockito.when(injectedButton.getElement()).thenReturn(element);

      // Act
      MyComposite composite = new MyComposite(injectedButton);

      // Assert
      assertEquals(element, composite.getElement());
   }
}
