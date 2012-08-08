package com.googlecode.gwt.test.gxt2;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.MessageBox;

public class MessageBoxTest extends GwtGxtTest {

   @Test
   public void alertAndClick() {
      // Act
      MessageBox box = MessageBox.alert("title", "message", new Listener<MessageBoxEvent>() {

         public void handleEvent(MessageBoxEvent be) {
         }
      });

      // Assert
      assertThat(box.isVisible()).isTrue();
   }

   @Test
   public void waitAndClose() {
      // Act 1
      MessageBox box = MessageBox.wait("title", "message", null);

      // Assert 1
      assertThat(box.isVisible()).isTrue();

      // Act 2
      box.close();

      // Assert 2
      assertThat(box.isVisible()).isFalse();

   }

}
