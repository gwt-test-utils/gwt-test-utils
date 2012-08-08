package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.events.Browser;

public class TextBoxTest extends GwtTestTest {

   private class KeyPressEventData {

      public char charCode;
      public int keyCode;
   }

   @Test
   public void getCursorPos() {
      // Arrange
      TextBox t = new TextBox();
      t.setText("myText");
      GwtReflectionUtils.setPrivateFieldValue(t, "attached", true);

      // Act
      t.setCursorPos(2);

      // Assert
      assertEquals(2, t.getCursorPos());
   }

   @Test
   public void maxLength() {
      // Arrange
      TextBox t = new TextBox();
      // Pre-Assert
      assertEquals(0, t.getMaxLength());

      // Act
      t.setMaxLength(10);

      // Assert
      assertEquals(10, t.getMaxLength());
   }

   @Test
   public void name() {
      // Arrange
      TextBox t = new TextBox();
      // Pre-Assert
      assertEquals("", t.getName());

      // Act
      t.setName("name");

      // Assert
      assertEquals("name", t.getName());
   }

   @Test
   public void pressKey() {
      // Arrange
      final List<KeyPressEventData> events = new ArrayList<KeyPressEventData>();
      TextBox tb = new TextBox();

      tb.addKeyPressHandler(new KeyPressHandler() {

         public void onKeyPress(KeyPressEvent event) {
            KeyPressEventData data = new KeyPressEventData();
            data.keyCode = event.getNativeEvent().getKeyCode();
            data.charCode = event.getCharCode();
            events.add(data);
         }
      });

      // Act
      Browser.fillText(tb, "gael");

      // Assert
      assertEquals("gael", tb.getValue());
      assertEquals(4, events.size());
      assertEquals('g', events.get(0).charCode);
      assertEquals(103, events.get(0).keyCode);
      assertEquals('a', events.get(1).charCode);
      assertEquals(97, events.get(1).keyCode);
      assertEquals('e', events.get(2).charCode);
      assertEquals(101, events.get(2).keyCode);
      assertEquals('l', events.get(3).charCode);
      assertEquals(108, events.get(3).keyCode);
   }

   @Test
   public void selectAll() {
      // Arrange
      TextBox t = new TextBox();
      t.setValue("0123456789");
      RootPanel.get().add(t);

      // Act
      t.selectAll();

      // Assert
      assertEquals(10, t.getSelectionLength());
      assertEquals("0123456789", t.getSelectedText());
   }

   @Test
   public void selectionRange() {
      // Arrange
      TextBox t = new TextBox();
      t.setValue("0123456789");
      RootPanel.get().add(t);

      // Act
      t.setSelectionRange(4, 3);

      // Assert
      assertEquals(3, t.getSelectionLength());
      assertEquals("456", t.getSelectedText());
   }

   @Test
   public void text() {
      // Arrange
      TextBox t = new TextBox();
      // Pre-Assert
      assertEquals("", t.getText());

      // Act
      t.setText("text");

      // Assert
      assertEquals("text", t.getText());
   }

   @Test
   public void title() {
      // Arrange
      TextBox t = new TextBox();
      // Pre-Assert
      assertEquals("", t.getTitle());

      // Act
      t.setTitle("title");

      // Assert
      assertEquals("title", t.getTitle());
   }

   @Test
   public void value() {
      // Arrange
      TextBox t = new TextBox();
      // Pre-Assert
      assertEquals("", t.getValue());

      // Act
      t.setValue("value");

      // Assert
      assertEquals("value", t.getValue());
   }

   @Test
   public void visible() {
      // Arrange
      TextBox t = new TextBox();
      // Pre-Assert
      assertEquals(true, t.isVisible());

      // Act
      t.setVisible(false);

      // Assert
      assertEquals(false, t.isVisible());
   }
}
