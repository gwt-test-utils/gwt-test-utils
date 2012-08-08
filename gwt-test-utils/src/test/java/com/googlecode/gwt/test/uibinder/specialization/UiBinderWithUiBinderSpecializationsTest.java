package com.googlecode.gwt.test.uibinder.specialization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.test.GwtTestTest;

public class UiBinderWithUiBinderSpecializationsTest extends GwtTestTest {

   @Test
   public void uiBinderWithDockLayoutPanel() {
      // Arrange
      UiBinderWithMoreThanOneUiBinderFactoriesForSameType w = new UiBinderWithMoreThanOneUiBinderFactoriesForSameType();

      // Act
      RootPanel.get().add(w);

      // Assert
      assertEquals("item created by @UiFactory", w.itemWidget.genericLabel.getText());
      assertEquals("person created by @UiFactory", w.personWidget.genericLabel.getText());
   }
}
