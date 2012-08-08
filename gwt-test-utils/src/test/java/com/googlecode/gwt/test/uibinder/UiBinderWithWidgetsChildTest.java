package com.googlecode.gwt.test.uibinder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.utils.events.Browser;

public class UiBinderWithWidgetsChildTest extends GwtTestTest {

   @Test
   public void click_UiHandler() {
      // Arrange
      UiBinderWithWidgetsChild w = new UiBinderWithWidgetsChild("gael", "eric");

      // Pre-Assert
      assertEquals(1, w.listBox.getVisibleItemCount());

      // Act
      Browser.click(w.button);

      // Assert
      assertEquals(2, w.listBox.getVisibleItemCount());
   }

   @Test
   public void uiObjectTag() {
      // Arrange
      UiBinderWithWidgetsChild w = new UiBinderWithWidgetsChild("gael", "eric");

      // Act
      RootPanel.get().add(w);

      // Assert
      assertEquals(1, w.listBox.getVisibleItemCount());

      assertEquals("my provided label", w.providedLabel.getText());
      assertEquals("custom text setup in ui.xml", w.providedLabel.getCustomText());
      assertEquals("my provided string", w.providedLabel.providedString);
      assertEquals("disabled", w.providedLabel.getStyleName());

      assertEquals("my UiFactory label", w.uiFactoryLabel.getText());
      assertEquals("custom text setup in ui.xml", w.uiFactoryLabel.getCustomText());
      assertEquals("gael", w.uiFactoryLabel.uiFactoryString);

      assertEquals("my UiConstructor label", w.uiConstructorLabel.getText());
      assertEquals("custom text setup in ui.xml", w.uiConstructorLabel.getCustomText());
      assertEquals("uiConstructor property", w.uiConstructorLabel.uiConstructorLabel);

      // Assertion on inner style
      assertEquals("style", w.style.getName());
      assertEquals(
               ".redBox {background-color: pink;border: 1px solid red;}.enabled {color: black;}.disabled {color: gray;}",
               w.style.getText());

      // override by child assertion
      assertEquals("override by child", w.pushButton.getText());

   }

}
