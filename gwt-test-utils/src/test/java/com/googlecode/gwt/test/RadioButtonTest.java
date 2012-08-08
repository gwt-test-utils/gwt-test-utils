package com.googlecode.gwt.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.MockValueChangeHandler;
import com.googlecode.gwt.test.utils.events.Browser;

public class RadioButtonTest extends GwtTestTest {

   private boolean tested;

   @Test
   public void changeName() {
      // Arrange
      RadioButton rb0 = new RadioButton("myRadioGroup", "foo");
      RootPanel.get().add(rb0);
      MockValueChangeHandler<Boolean> rb0MockChangeHandler = new MockValueChangeHandler<Boolean>();
      rb0.addValueChangeHandler(rb0MockChangeHandler);

      RadioButton rb1 = new RadioButton("myRadioGroup", "bar");
      RootPanel.get().add(rb1);
      MockValueChangeHandler<Boolean> rb1MockChangeHandler = new MockValueChangeHandler<Boolean>();
      rb1.addValueChangeHandler(rb1MockChangeHandler);

      // Act 1
      rb0.setName("changedGroup");
      Browser.click(rb0);

      // Assert 1
      assertTrue(rb0.getValue());
      assertEquals(1, rb0MockChangeHandler.getCallCount());

      // Act 2
      rb1.setName("changedGroup");
      Browser.click(rb1);

      assertFalse(rb0.getValue());
      assertEquals(2, rb0MockChangeHandler.getCallCount());
      assertFalse(rb0MockChangeHandler.getLast());

      assertTrue(rb1.getValue());
      assertEquals(1, rb1MockChangeHandler.getCallCount());
      assertTrue(rb1MockChangeHandler.getLast());
   }

   @Test
   public void click_ClickHandler() {
      // Arrange
      tested = false;
      RadioButton r = new RadioButton("myRadioGroup", "foo");
      r.addClickHandler(new ClickHandler() {

         public void onClick(ClickEvent event) {
            tested = !tested;
         }

      });
      // Pre-Assert
      assertEquals(false, tested);

      // Act
      Browser.click(r);

      // Assert
      assertEquals(true, tested);
      assertEquals(true, r.getValue());
   }

   @Test
   public void click_Twice_ClickHandler() {
      // Arrange
      tested = false;
      RadioButton r1 = new RadioButton("myRadioGroup", "r1");
      RadioButton r2 = new RadioButton("myRadioGroup", "r2");
      r1.addClickHandler(new ClickHandler() {

         public void onClick(ClickEvent event) {
            tested = !tested;
         }

      });

      r2.addClickHandler(new ClickHandler() {

         public void onClick(ClickEvent event) {
            tested = !tested;
         }

      });
      // Pre-Assert
      assertEquals(false, tested);

      // Act
      Browser.click(r1);
      Browser.click(r1);

      // Assert
      assertEquals(false, tested);
      assertEquals(true, r1.getValue());
   }

   @Test
   public void clickNotDetachedRadioButton() {
      // Arrange
      RadioButton rb0 = new RadioButton("myRadioGroup", "foo");
      MockValueChangeHandler<Boolean> rb0MockChangeHandler = new MockValueChangeHandler<Boolean>();
      rb0.addValueChangeHandler(rb0MockChangeHandler);

      RadioButton rb1 = new RadioButton("myRadioGroup", "bar");
      MockValueChangeHandler<Boolean> rb1MockChangeHandler = new MockValueChangeHandler<Boolean>();
      rb1.addValueChangeHandler(rb1MockChangeHandler);

      // Act
      Browser.click(rb0);
      Browser.click(rb1);

      // Assert
      assertTrue(rb0.getValue());
      assertEquals(1, rb0MockChangeHandler.getCallCount());
      assertTrue(rb1.getValue());
      assertEquals(1, rb1MockChangeHandler.getCallCount());
   }

   @Test
   public void clickOnDetachedRadionButton() {
      // Arrange
      RadioButton rb0 = new RadioButton("myRadioGroup", "foo");
      rb0.setValue(true);
      RootPanel.get().add(rb0);
      MockValueChangeHandler<Boolean> rb0MockChangeHandler = new MockValueChangeHandler<Boolean>();
      rb0.addValueChangeHandler(rb0MockChangeHandler);

      RadioButton rb1 = new RadioButton("myRadioGroup", "bar");
      RootPanel.get().add(rb1);
      MockValueChangeHandler<Boolean> rb1MockChangeHandler = new MockValueChangeHandler<Boolean>();
      rb1.addValueChangeHandler(rb1MockChangeHandler);

      // Pre-Assert
      assertTrue(rb0.getValue());
      assertEquals(0, rb0MockChangeHandler.getCallCount());
      assertFalse(rb1.getValue());
      assertEquals(0, rb1MockChangeHandler.getCallCount());

      // Act
      RootPanel.get().remove(rb1);
      Browser.click(rb1);

      // Assert
      assertTrue(rb1.getValue());
      assertEquals(1, rb1MockChangeHandler.getCallCount());
      assertTrue(rb0.getValue());
      assertEquals(0, rb0MockChangeHandler.getCallCount());
   }

   @Test
   public void html() {
      // Arrange
      RadioButton rb = new RadioButton("myRadioGroup", "<h1>foo</h1>", true);
      // Pre-Assert
      assertEquals("<h1>foo</h1>", rb.getHTML());

      // Act
      rb.setHTML("<h1>test</h1>");

      // Assert
      assertEquals("<h1>test</h1>", rb.getHTML());
      assertEquals(1, rb.getElement().getChild(1).getChildCount());
      HeadingElement h1 = rb.getElement().getChild(1).getChild(0).cast();
      assertEquals("H1", h1.getTagName());
      assertEquals("test", h1.getInnerText());
   }

   @Test
   public void name() {
      // Arrange
      RadioButton rb = new RadioButton("myRadioGroup", "foo");
      // Pre-Assert
      assertEquals("myRadioGroup", rb.getName());

      // Act
      rb.setName("name");

      // Assert
      assertEquals("name", rb.getName());
   }

   @Test
   public void radioButton_Group() {
      // Arrange
      RadioButton rb0 = new RadioButton("myRadioGroup", "foo");
      RootPanel.get().add(rb0);
      MockValueChangeHandler<Boolean> rb0MockChangeHandler = new MockValueChangeHandler<Boolean>();
      rb0.addValueChangeHandler(rb0MockChangeHandler);

      RadioButton rb1 = new RadioButton("myRadioGroup", "bar");
      RootPanel.get().add(rb1);
      MockValueChangeHandler<Boolean> rb1MockChangeHandler = new MockValueChangeHandler<Boolean>();
      rb1.addValueChangeHandler(rb1MockChangeHandler);

      RadioButton rb2 = new RadioButton("myRadioGroup", "baz");
      RootPanel.get().add(rb2);
      MockValueChangeHandler<Boolean> rb2MockChangeHandler = new MockValueChangeHandler<Boolean>();
      rb2.addValueChangeHandler(rb2MockChangeHandler);

      // Act 1
      Browser.click(rb1);

      // Assert 1
      assertEquals(false, rb0.getValue());
      assertEquals(0, rb0MockChangeHandler.getCallCount());

      assertEquals(true, rb1.getValue());
      assertEquals(1, rb1MockChangeHandler.getCallCount());
      assertTrue(rb1MockChangeHandler.getLast());

      assertEquals(false, rb2.getValue());
      assertEquals(0, rb2MockChangeHandler.getCallCount());

      // Act 2
      Browser.click(rb2);

      // Assert 2
      assertEquals(false, rb0.getValue());
      assertEquals(0, rb0MockChangeHandler.getCallCount());

      assertEquals(false, rb1.getValue());
      assertEquals(2, rb1MockChangeHandler.getCallCount());
      assertFalse(rb1MockChangeHandler.getLast());

      assertEquals(true, rb2.getValue());
      assertEquals(1, rb2MockChangeHandler.getCallCount());
      assertTrue(rb2MockChangeHandler.getLast());
   }

   @Test
   public void text() {
      // Arrange
      RadioButton rb = new RadioButton("myRadioGroup", "foo");
      // Pre-Assert
      assertEquals("foo", rb.getText());

      // Act
      rb.setText("text");

      // Assert
      assertEquals("text", rb.getText());
   }

   @Test
   public void title() {
      // Arrange
      RadioButton rb = new RadioButton("myRadioGroup", "foo");
      // Pre-Assert
      assertEquals("", rb.getTitle());

      // Act
      rb.setTitle("title");

      // Assert
      assertEquals("title", rb.getTitle());
   }

   @Test
   public void visible() {
      // Arrange
      RadioButton rb = new RadioButton("myRadioGroup", "foo");
      // Pre-Assert
      assertEquals(true, rb.isVisible());

      // Act
      rb.setVisible(false);

      // Assert
      assertEquals(false, rb.isVisible());
   }

}
