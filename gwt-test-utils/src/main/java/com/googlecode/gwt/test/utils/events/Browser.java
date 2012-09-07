package com.googlecode.gwt.test.utils.events;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.googlecode.gwt.test.internal.BrowserSimulatorImpl;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.internal.utils.JsoProperties;
import com.googlecode.gwt.test.internal.utils.RadioButtonManager;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.JavaScriptObjects;
import com.googlecode.gwt.test.utils.WidgetUtils;

/**
 * Provides several methods to simulate the occurring of browser events (onClick, onKeyDown,
 * onChange, etc.) caused by the interaction with a widget.
 * 
 * @author Gael Lazzari
 * 
 */
public class Browser {

   /**
    * A callback interface to handle error when dispatching a browser {@link Event}.
    * 
    * @author Gael Lazzari
    * 
    */
   public static interface BrowserErrorHandler {

      /**
       * The callback method called when an error occurs.
       * 
       * @param errorMessage The error's message.
       */
      void onError(String errorMessage);
   }

   /**
    * <p>
    * Add some text in a text widget, starting at {@link ValueBoxBase#getCursorPos()} index and
    * deleting {@link ValueBoxBase#getSelectedText()} if the targeted widget is a
    * {@link ValueBoxBase} instance.
    * </p>
    * <p>
    * <ul>
    * <li>Like {@link Browser#fillText(HasText, String)}, {@link KeyDownEvent},
    * {@link KeyPressEvent} and {@link KeyUpEvent} are triggered for each character in the value to
    * add. They can be prevented with normal effect.</li>
    * 
    * <li>Contrary to {@link Browser#fillText(HasText, String)}, neither {@link BlurEvent} nor
    * {@link ChangeEvent} are triggered.</li>
    * </ul>
    * </p>
    * 
    * @param widget The widget to fill. <strong>It has to be attached and visible</strong>
    * @param value The value to fill. Cannot be null or empty.
    * 
    * @throws IllegalArgumentException if the value to fill is null or empty.
    */
   public static <T extends IsWidget & HasText> void addText(T widget, String value)
            throws IllegalArgumentException {
      if (value == null || "".equals(value)) {
         throw new IllegalArgumentException(
                  "Cannot fill a null or empty text. If you intent to remove some text, use '"
                           + Browser.class.getSimpleName() + ".emptyText(..)' instead");
      }

      for (int i = 0; i < value.length(); i++) {
         pressKey(widget, value.charAt(i));
      }
   }

   /**
    * Simulates a blur event.
    * 
    * @param target The targeted widget.
    */
   public static void blur(IsWidget target) {
      dispatchEvent(target, EventBuilder.create(Event.ONBLUR).build());
   }

   /**
    * Simulates a change event.
    * 
    * @param target The targeted widget.
    */
   public static void change(IsWidget target) {
      dispatchEvent(target, EventBuilder.create(Event.ONCHANGE).build());
   }

   /**
    * Simulate a click on a specific element of a cell widget.
    * 
    * @param <T> The {@link HasData} generic type.
    * @param hasData The cell widget.
    * @param item The content of the row to click.
    */
   public static <T> void click(AbstractHasData<T> hasData, T item) {
      // trigger an event loop end first
      BrowserSimulatorImpl.get().fireLoopEnd();

      if (hasData.getSelectionModel() == null) {
         return;
      }

      // compute the key for the item to click
      Object itemKey = hasData.getKeyProvider() != null ? hasData.getKeyProvider().getKey(item)
               : item;

      Iterator<T> it = hasData.getVisibleItems().iterator();
      while (it.hasNext()) {
         // compute the key for the current visible item
         T visibleContent = it.next();
         Object visibleKey = hasData.getKeyProvider() != null ? hasData.getKeyProvider().getKey(
                  visibleContent) : visibleContent;

         if (visibleKey.equals(itemKey)) {
            hasData.getSelectionModel().setSelected(item,
                     !hasData.getSelectionModel().isSelected(item));

            // trigger an event loop end because some commands could have been scheduled when the
            // event was dispatched.
            BrowserSimulatorImpl.get().fireLoopEnd();

            return;
         }
      }

      GwtConfig.get().getModuleRunner().getBrowserErrorHandler().onError(
               "the item to click is now visible in the targeted "
                        + hasData.getClass().getSimpleName() + " instance");
   }

   /**
    * Simulates a click event on the Grid cell with the given indexes.
    * 
    * @param grid The targeted grid.
    * @param row The row index of the cell to click.
    * @param column The column index of the cell to click.
    */
   public static void click(Grid grid, int row, int column) {
      Widget target = grid.getWidget(row, column);
      clickInternal(grid, target);
   }

   /**
    * Simulates a click event.
    * 
    * @param target The targeted widget.
    */
   public static void click(IsWidget target) {
      clickInternal(target, target.asWidget());
   }

   /**
    * Simulates a click event on the item of a MenuBar with the given index.
    * 
    * @param parent The targeted menu bar.
    * @param clickedItemIndex The index of the child widget to click inside the menu bar.
    */
   public static void click(MenuBar parent, int clickedItemIndex) {
      click(parent, WidgetUtils.getMenuItems(parent).get(clickedItemIndex));
   }

   /**
    * Simulates a click event on a particular MenuItem of a MenuBar.
    * 
    * @param menuBar The targeted menu bar.
    * @param clickedItem The widget to click inside the menu bar.
    */
   public static void click(MenuBar menuBar, MenuItem clickedItem) {
      clickInternal(menuBar, clickedItem);
   }

   /**
    * Simulates a click event on the item of a SuggestBox with the given index.
    * 
    * @param suggestBox The targeted suggest box.
    * @param clickedItemIndex The index of the child widget to click inside the suggest box.
    */
   public static void click(SuggestBox suggestBox, int clickedItemIndex) {
      click(suggestBox, WidgetUtils.getMenuItems(suggestBox).get(clickedItemIndex));
   }

   /**
    * Simulates a click event on a particular MenuItem of a SuggestBox.
    * 
    * @param suggestBox The targeted suggest box.
    * @param clickedItem The child widget to click inside the suggest box.
    * 
    */
   public static void click(SuggestBox suggestBox, MenuItem clickedItem) {
      Event onClick = EventBuilder.create(Event.ONCLICK).setTarget(clickedItem).build();

      if (canApplyEvent(suggestBox, onClick)) {
         clickedItem.getCommand().execute();
      }
   }

   /**
    * Simulates a click event on the widget with the given index inside a ComplexPanel.
    * 
    * @param panel The targeted panel.
    * @param index The index of the child widget to click inside the panel.
    */
   public static <T extends IndexedPanel & IsWidget> void click(T panel, int index) {
      Widget target = panel.getWidget(index);
      clickInternal(panel, target);
   }

   /**
    * Simulate a click event on a pager "first page" button
    * 
    * @param simplePager The targeted pager
    */
   public static void clickFirstPage(SimplePager simplePager) {
      Image firstPage = GwtReflectionUtils.getPrivateFieldValue(simplePager, "firstPage");
      Browser.click(firstPage);
   }

   /**
    * Click on a specific header of a cell table
    * 
    * @param table The targeted cell table
    * @param index The targeted cell header index in the table
    */
   public static void clickHeader(AbstractCellTable<?> table, int index) {
      BrowserSimulatorImpl.get().fireLoopEnd();
      table.getColumnSortList().push(table.getColumn(index));
      ColumnSortEvent.fire(table, table.getColumnSortList());
      BrowserSimulatorImpl.get().fireLoopEnd();
   }

   /**
    * Simulate a click event on a pager "last page" button
    * 
    * @param simplePager The targeted pager
    */
   public static void clickLastPage(SimplePager simplePager) {
      Image lastPage = GwtReflectionUtils.getPrivateFieldValue(simplePager, "lastPage");
      Browser.click(lastPage);
   }

   /**
    * Simulate a click event on a pager "next page" button
    * 
    * @param simplePager The targeted pager
    */
   public static void clickNextPage(SimplePager simplePager) {
      Image nextPage = GwtReflectionUtils.getPrivateFieldValue(simplePager, "nextPage");
      Browser.click(nextPage);
   }

   /**
    * Simulate a click event on a pager "previous page" button
    * 
    * @param simplePager The targeted pager
    */
   public static void clickPreviousPage(SimplePager simplePager) {
      Image prevPage = GwtReflectionUtils.getPrivateFieldValue(simplePager, "prevPage");
      Browser.click(prevPage);
   }

   /**
    * Simulates a dblclick event.
    * 
    * @param target The targeted widget.
    */
   public static void dblClick(IsWidget target) {
      dispatchEvent(target, EventBuilder.create(Event.ONDBLCLICK).build());
   }

   /**
    * Simulates an occurring of the given event due to an interaction with the target widget.
    * 
    * @param target The targeted widget.
    * @param events Some events to dispatch.
    */
   public static void dispatchEvent(IsWidget target, Event... events) {
      dispatchEventsInternal(target, true, events);
   }

   /**
    * <p>
    * Remove the text within a widget which implements HasText interface by simulating a long
    * backspace key press.
    * </p>
    * <p>
    * <ul>
    * <li>For each character in the text value of the widget, a {@link KeyDownEvent} is triggered
    * with value {@link KeyCodes#KEY_BACKSPACE} . It can be prevented with normal effect.</li>
    * <li><strong>Only one</strong> {@link KeyUpEvent} is triggered with value
    * {@link KeyCodes#KEY_BACKSPACE}.</li>
    * <li>Than, a {@link BlurEvent} is triggered.</li>
    * <li>Finally, if at least one on the KeyDown events has not been prevented, a
    * {@link ChangeEvent} is triggered.</li>
    * </p>
    * <p>
    * Note that no {@link KeyPressEvent} would be triggered.
    * </p>
    * 
    * @param hasTextWidget The widget to fill. If this implementation actually isn't a
    *           {@link Widget} instance, nothing would be done.
    */
   public static void emptyText(HasText hasTextWidget) {
      boolean changed = false;

      int baseLength = hasTextWidget.getText().length();
      for (int i = 0; i < baseLength; i++) {
         Event keyDownEvent = EventBuilder.create(Event.ONKEYDOWN).setKeyCode(
                  KeyCodes.KEY_BACKSPACE).build();
         dispatchEvent((IsWidget) hasTextWidget, keyDownEvent);

         boolean keyDownEventPreventDefault = JavaScriptObjects.getBoolean(keyDownEvent,
                  JsoProperties.EVENT_PREVENTDEFAULT);

         if (!keyDownEventPreventDefault) {
            hasTextWidget.setText(hasTextWidget.getText().substring(0,
                     hasTextWidget.getText().length() - 1));
            changed = true;
         }

      }

      // don't have to check if the event can be dispatch since it's check
      // before
      Event keyUpEvent = EventBuilder.create(Event.ONKEYUP).setKeyCode(KeyCodes.KEY_BACKSPACE).build();
      dispatchEvent((IsWidget) hasTextWidget, keyUpEvent);

      dispatchEvent((IsWidget) hasTextWidget, EventBuilder.create(Event.ONBLUR).build());

      if (changed) {
         dispatchEvent((IsWidget) hasTextWidget, EventBuilder.create(Event.ONCHANGE).build());
      }
   }

   /**
    * <p>
    * Remove the text within a widget which implements HasText interface
    * </p>
    * <p>
    * <ul>
    * <li>For each character in the text value of the widget, a {@link KeyDownEvent} is triggered
    * with value {@link KeyCodes#KEY_BACKSPACE} . It can be prevented with normal effect.</li>
    * <li>Either one or x {@link KeyUpEvent} are triggered with value {@link KeyCodes#KEY_BACKSPACE}
    * , according to the chosen empty text simulation (with x the number of character in the text
    * value).</li>
    * <li>Than, a {@link BlurEvent} is triggered.</li>
    * <li>Finally, if at least one on the KeyDown events has not been prevented, a
    * {@link ChangeEvent} is triggered.</li>
    * </p>
    * <p>
    * Note that no {@link KeyPressEvent} would be triggered.
    * </p>
    * 
    * @param hasTextWidget The widget to fill. If this implementation actually isn't a
    *           {@link Widget} instance, nothing would be done.
    * @param longBackPress True if it should simulate a long backspace press or not.
    */
   public static void emptyText(HasText hasTextWidget, boolean longBackPress) {
      if (longBackPress) {
         emptyText(hasTextWidget);
      } else {
         removeText(hasTextWidget, hasTextWidget.getText().length());
      }
   }

   /**
    * <p>
    * Fill a widget which implements HasText interface.
    * </p>
    * <p>
    * <ul>
    * <li>For each character in the value to fill, {@link KeyDownEvent}, {@link KeyPressEvent} and
    * {@link KeyUpEvent} are triggered. They can be prevented with normal effect.</li>
    * <li>After typing, a {@link BlurEvent} is triggered.</li>
    * <li>Than, if at least one of the KeyDown or KeyPress events has not been prevented, a
    * {@link ChangeEvent} would be triggered.</li>
    * </ul>
    * </p>
    * 
    * <p>
    * <strong>Do not use this method to remove text by calling it with an empty string. Use
    * {@link Browser#emptyText(HasText, boolean)} instead.</strong>
    * </p>
    * 
    * @param hasTextWidget The widget to fill. If this implementation actually isn't a
    *           {@link Widget} instance, nothing would be done.
    * @param check Indicate if the method should check if the hasText Widget to fill is attached,
    *           visible and enabled before applying any event.
    * @param value The value to fill. Cannot be null or empty.
    * @throws IllegalArgumentException if the value to fill is null or empty.
    */
   public static void fillText(HasText hasTextWidget, boolean check, String value)
            throws IllegalArgumentException {
      fillText(hasTextWidget, check, value, true);
   }

   /**
    * <p>
    * Fill a widget which implements HasText interface.
    * </p>
    * <p>
    * <ul>
    * <li>For each character in the value to fill, {@link KeyDownEvent}, {@link KeyPressEvent} and
    * {@link KeyUpEvent} are triggered. They can be prevented with normal effect.</li>
    * </ul>
    * </p>
    * 
    * <p>
    * <strong>Do not use this method to remove text by calling it with an empty string. Use
    * {@link Browser#emptyText(HasText, boolean)} instead.</strong>
    * </p>
    * 
    * @param hasTextWidget The widget to fill. If this implementation actually isn't a
    *           {@link Widget} instance, nothing would be done.
    * @param check Indicate if the method should check if the hasText Widget to fill is attached,
    *           visible and enabled before applying any event.
    * @param value The value to fill. Cannot be null or empty.
    * @param blur Specify if a blur event must be triggered. If <strong>true</strong> and at least
    *           one on the KeyDown or KeyPress events has not been prevented, a {@link ChangeEvent}
    *           would be triggered too.
    * @throws IllegalArgumentException if the value to fill is null or empty.
    */
   public static void fillText(HasText hasTextWidget, boolean check, String value, boolean blur)
            throws IllegalArgumentException {
      if (value == null || "".equals(value)) {
         throw new IllegalArgumentException(
                  "Cannot fill a null or empty text. If you intent to remove some text, use '"
                           + Browser.class.getSimpleName() + ".emptyText(..)' instead");
      }
      if (!Widget.class.isInstance(hasTextWidget)) {
         return;
      }

      boolean changed = false;

      for (int i = 0; i < value.length(); i++) {

         int keyCode = value.charAt(i);

         // trigger keyDown and keyPress
         Event keyDownEvent = EventBuilder.create(Event.ONKEYDOWN).setKeyCode(keyCode).build();
         Event keyPressEvent = EventBuilder.create(Event.ONKEYPRESS).setKeyCode(keyCode).build();
         dispatchEventsInternal((IsWidget) hasTextWidget, check, keyDownEvent, keyPressEvent);

         // check if one on the events has been prevented
         boolean keyDownEventPreventDefault = JavaScriptObjects.getBoolean(keyDownEvent,
                  JsoProperties.EVENT_PREVENTDEFAULT);
         boolean keyPressEventPreventDefault = JavaScriptObjects.getBoolean(keyPressEvent,
                  JsoProperties.EVENT_PREVENTDEFAULT);

         if (!keyDownEventPreventDefault && !keyPressEventPreventDefault) {
            hasTextWidget.setText(value.substring(0, i + 1));
            changed = true;

            if (hasTextWidget instanceof ValueBoxBase) {
               JavaScriptObjects.setProperty(((Widget) hasTextWidget).getElement(),
                        JsoProperties.SELECTION_START, i + 1);
               JavaScriptObjects.setProperty(((Widget) hasTextWidget).getElement(),
                        JsoProperties.SELECTION_END, i + 1);
            }
         }

         // trigger keyUp
         Event keyUpEvent = EventBuilder.create(Event.ONKEYUP).setKeyCode(keyCode).build();
         dispatchEventsInternal((IsWidget) hasTextWidget, check, keyUpEvent);

      }

      if (blur) {
         // no need to check event anymore
         dispatchEventsInternal((IsWidget) hasTextWidget, false,
                  EventBuilder.create(Event.ONBLUR).build());

         if (changed) {
            dispatchEventsInternal((IsWidget) hasTextWidget, false,
                     EventBuilder.create(Event.ONCHANGE).build());
         }
      }
   }

   /**
    * <p>
    * Fill a widget which implements HasText interface.
    * </p>
    * <p>
    * <ul>
    * <li>For each character in the value to fill, {@link KeyDownEvent}, {@link KeyPressEvent} and
    * {@link KeyUpEvent} are triggered. They can be prevented with normal effect.</li>
    * <li>After typing, a {@link BlurEvent} is triggered.</li>
    * <li>Than, if at least one on the KeyDown or KeyPress events has not been prevented, a
    * {@link ChangeEvent} would be triggered.</li>
    * </ul>
    * </p>
    * 
    * <p>
    * <strong>Do not use this method to remove text by calling it with an empty string. Use
    * {@link Browser#emptyText(HasText, boolean)} instead.</strong>
    * </p>
    * 
    * @param hasTextWidget The widget to fill. If this implementation actually isn't a
    *           {@link Widget} instance, nothing would be done.
    * @param value The value to fill. Cannot be null or empty.
    * @throws IllegalArgumentException if the value to fill is null or empty.
    */
   public static void fillText(HasText hasTextWidget, String value) throws IllegalArgumentException {
      fillText(hasTextWidget, true, value, true);
   }

   /**
    * <p>
    * Fill a widget which implements HasText interface.
    * </p>
    * <p>
    * <ul>
    * <li>For each character in the value to fill, {@link KeyDownEvent}, {@link KeyPressEvent} and
    * {@link KeyUpEvent} are triggered. They can be prevented with normal effect.</li>
    * </ul>
    * </p>
    * 
    * <p>
    * <strong>Do not use this method to remove text by calling it with an empty string. Use
    * {@link Browser#emptyText(HasText, boolean)} instead.</strong>
    * </p>
    * 
    * @param hasTextWidget The widget to fill. If this implementation actually isn't a
    *           {@link Widget} instance, nothing would be done.
    * @param value The value to fill. Cannot be null or empty.
    * @param blur Specify if a blur event must be triggered. If <strong>true</strong> and at least
    *           one on the KeyDown or KeyPress events has not been prevented, a {@link ChangeEvent}
    *           would be triggered too.
    * @throws IllegalArgumentException if the value to fill is null or empty.
    */
   public static void fillText(HasText hasTextWidget, String value, boolean blur)
            throws IllegalArgumentException {
      fillText(hasTextWidget, true, value, blur);
   }

   /**
    * Simulates a focus event.
    * 
    * @param target The targeted widget.
    */
   public static void focus(IsWidget target) {
      dispatchEvent(target, EventBuilder.create(Event.ONFOCUS).build());
   }

   /**
    * Simulates a keydown event.
    * 
    * @param target The targeted widget.
    */
   public static void keyDown(IsWidget target, int keyCode) {
      dispatchEvent(target, EventBuilder.create(Event.ONKEYDOWN).setKeyCode(keyCode).build());
   }

   /**
    * Simulates a keypress event.
    * 
    * @param target The targeted widget.
    */
   public static void keyPress(IsWidget target, int keyCode) {
      dispatchEvent(target, EventBuilder.create(Event.ONKEYPRESS).setKeyCode(keyCode).build());
   }

   /**
    * Simulates a keyup event.
    * 
    * @param target The targeted widget.
    */
   public static void keyUp(IsWidget target, int keyCode) {
      dispatchEvent(target, EventBuilder.create(Event.ONKEYUP).setKeyCode(keyCode).build());
   }

   /**
    * Simulates a mousedown event.
    * 
    * @param target The targeted widget.
    */
   public static void mouseDown(IsWidget target) {
      dispatchEvent(target, EventBuilder.create(Event.ONMOUSEDOWN).build());
   }

   /**
    * Simulates a mousemove event.
    * 
    * @param target The targeted widget.
    */
   public static void mouseMove(IsWidget target) {
      dispatchEvent(target, EventBuilder.create(Event.ONMOUSEMOVE).build());
   }

   /**
    * Simulates a mouseout event.
    * 
    * @param target The targeted widget.
    */
   public static void mouseOut(IsWidget target) {
      dispatchEvent(target, EventBuilder.create(Event.ONMOUSEOUT).build());
   }

   /**
    * Simulates a mouseover event.
    * 
    * @param target The targeted widget.
    */
   public static void mouseOver(IsWidget target) {
      dispatchEvent(target, EventBuilder.create(Event.ONMOUSEOVER).build());
   }

   /**
    * Simulates a mouseup event.
    * 
    * @param target The targeted widget.
    */
   public static void mouseUp(IsWidget target) {
      dispatchEvent(target, EventBuilder.create(Event.ONMOUSEUP).build());
   }

   /**
    * Simulates a mousewheel event.
    * 
    * @param target The targeted widget.
    */
   public static void mouseWheel(IsWidget target) {
      dispatchEvent(target, EventBuilder.create(Event.ONMOUSEWHEEL).build());
   }

   /**
    * <p>
    * Simulate a user key press, adding some a character at {@link ValueBoxBase#getCursorPos()}
    * index and deleting {@link ValueBoxBase#getSelectedText()} if the targeted widget is a
    * {@link ValueBoxBase} instance.
    * </p>
    * <p>
    * {@link KeyDownEvent}, {@link KeyPressEvent} and {@link KeyUpEvent} are triggered with keyCode
    * of the pressed key. They can be prevented with normal effect.
    * </p>
    * 
    * @param widget The widget to fill. <strong>It has to be attached and visible</strong>
    * @param keyCode The code of the key to be pressed.
    * 
    */
   public static <T extends IsWidget & HasText> void pressKey(T widget, int keyCode) {
      if (widget == null) {
         return;
      }

      // trigger keyDown and keyPress
      Event keyDownEvent = EventBuilder.create(Event.ONKEYDOWN).setKeyCode(keyCode).build();
      Event keyPressEvent = EventBuilder.create(Event.ONKEYPRESS).setKeyCode(keyCode).build();
      dispatchEventsInternal(widget, true, keyDownEvent, keyPressEvent);

      // check if one on the events has been prevented
      boolean keyDownEventPreventDefault = JavaScriptObjects.getBoolean(keyDownEvent,
               JsoProperties.EVENT_PREVENTDEFAULT);
      boolean keyPressEventPreventDefault = JavaScriptObjects.getBoolean(keyPressEvent,
               JsoProperties.EVENT_PREVENTDEFAULT);

      if (!keyDownEventPreventDefault && !keyPressEventPreventDefault) {

         StringBuilder sb = new StringBuilder(widget.getText());

         // remove selectionRange
         int selectionStart = JavaScriptObjects.getInteger(widget.asWidget().getElement(),
                  JsoProperties.SELECTION_START);
         int selectionEnd = JavaScriptObjects.getInteger(widget.asWidget().getElement(),
                  JsoProperties.SELECTION_END);

         switch (keyCode) {
            case KeyCodes.KEY_ALT:
            case KeyCodes.KEY_CTRL:
            case KeyCodes.KEY_DELETE:
            case KeyCodes.KEY_DOWN:
            case KeyCodes.KEY_END:
            case KeyCodes.KEY_ENTER:
            case KeyCodes.KEY_ESCAPE:
            case KeyCodes.KEY_HOME:
            case KeyCodes.KEY_LEFT:
            case KeyCodes.KEY_PAGEDOWN:
            case KeyCodes.KEY_PAGEUP:
            case KeyCodes.KEY_RIGHT:
            case KeyCodes.KEY_SHIFT:
            case KeyCodes.KEY_UP:
               // nothing to do
               break;
            case KeyCodes.KEY_TAB:
               blur(widget);
               break;
            case KeyCodes.KEY_BACKSPACE:
               if (selectionStart == selectionEnd) {
                  sb.deleteCharAt(selectionStart);
               } else {
                  sb.replace(selectionStart, selectionEnd, "");
               }
               widget.setText(sb.toString());
               break;
            default:
               sb.replace(selectionStart, selectionEnd, "");

               sb.insert(selectionStart, (char) keyCode);

               selectionStart = selectionEnd = selectionStart + 1;

               widget.setText(sb.toString());

               JavaScriptObjects.setProperty(widget.asWidget().getElement(),
                        JsoProperties.SELECTION_START, selectionStart);
               JavaScriptObjects.setProperty(widget.asWidget().getElement(),
                        JsoProperties.SELECTION_END, selectionEnd);
         }

         // trigger keyUp
         Event keyUpEvent = EventBuilder.create(Event.ONKEYUP).setKeyCode(keyCode).build();
         dispatchEventsInternal(widget, true, keyUpEvent);
      }
   }

   /**
    * <p>
    * Remove a fixed number of character from the text within a widget which implements HasText
    * interface by simulating a backspace key press.
    * </p>
    * <p>
    * <ul>
    * <li>x {@link KeyDownEvent} are triggered with value {@link KeyCodes#KEY_BACKSPACE}, with x the
    * number of backspace press passed as parameter. It can be prevented with normal effect.</li>
    * <li>Than, x {@link KeyUpEvent} are triggered with value {@link KeyCodes#KEY_BACKSPACE}, with x
    * the number of backspace press passed as parameter.</li>
    * <li>Than, a {@link BlurEvent} is triggered.</li>
    * <li>Finally, if at least one on the KeyDown events has not been prevented, a
    * {@link ChangeEvent} is triggered.</li>
    * </p>
    * <p>
    * Note that no {@link KeyPressEvent} would be triggered.
    * </p>
    * 
    * @param hasTextWidget The targeted widget. If this implementation actually isn't a
    *           {@link Widget} instance, nothing would be done.
    * @param backspacePressNumber The number of backspace key press to simulate.
    */
   public static void removeText(HasText hasTextWidget, int backspacePressNumber) {
      boolean changed = false;

      for (int i = 0; i < backspacePressNumber; i++) {
         Event keyDownEvent = EventBuilder.create(Event.ONKEYDOWN).setKeyCode(
                  KeyCodes.KEY_BACKSPACE).build();
         Event keyUpEvent = EventBuilder.create(Event.ONKEYUP).setKeyCode(KeyCodes.KEY_BACKSPACE).build();
         dispatchEvent((IsWidget) hasTextWidget, keyDownEvent, keyUpEvent);

         boolean keyDownEventPreventDefault = JavaScriptObjects.getBoolean(keyDownEvent,
                  JsoProperties.EVENT_PREVENTDEFAULT);

         if (!keyDownEventPreventDefault) {
            hasTextWidget.setText(hasTextWidget.getText().substring(0,
                     hasTextWidget.getText().length() - 1));
            changed = true;
         }

      }

      dispatchEvent((IsWidget) hasTextWidget, EventBuilder.create(Event.ONBLUR).build());

      if (changed) {
         dispatchEvent((IsWidget) hasTextWidget, EventBuilder.create(Event.ONCHANGE).build());
      }
   }

   /**
    * Simulate the submission of a form with the expected html result from the server. The targeted
    * form is expected to be attached to the DOM.
    * 
    * @param form The form to submit
    * @param resultsHtml The mocked results to return from submitting the form
    * 
    * @return true is the form was submitted, false otherwise.
    * 
    * @see BrowserErrorHandler#onError(String)
    * 
    */
   public static boolean submit(FormPanel form, String resultsHtml) {
      Element synthesizedFrame = GwtReflectionUtils.getPrivateFieldValue(form, "synthesizedFrame");

      if (synthesizedFrame == null) {
         // the form has not been attached to the DOM
         GwtConfig.get().getModuleRunner().getBrowserErrorHandler().onError(
                  "Cannot submit form which is not attached to the DOM '");

         return false;
      } else {
         synthesizedFrame.setInnerHTML(resultsHtml);
         form.submit();

         return true;
      }

   }

   private static boolean canApplyEvent(IsWidget target, Event event) {

      Widget widget = target.asWidget();
      if (!widget.isAttached()
               && !GwtConfig.get().getModuleRunner().canDispatchEventsOnDetachedWidgets()) {
         GwtConfig.get().getModuleRunner().getBrowserErrorHandler().onError(
                  "Cannot dispatch '" + event.getType()
                           + "' event : the targeted widget is not attached to the DOM");
         return false;
      }

      Element targetElement = event.getEventTarget().cast();

      if (!WidgetUtils.isWidgetVisible(widget) && isVisible(widget, targetElement)) {
         GwtConfig.get().getModuleRunner().getBrowserErrorHandler().onError(
                  "Cannot dispatch '" + event.getType()
                           + "' event : the targeted element or one of its parents is not visible");

         return false;
      } else if (isDisabled(targetElement)) {
         GwtConfig.get().getModuleRunner().getBrowserErrorHandler().onError(
                  "Cannot dispatch '" + event.getType()
                           + "' event : the targeted element has to be enabled : "
                           + targetElement.toString());

         return false;
      }

      return true;
   }

   private static void clickInternal(IsWidget parent, UIObject target) {
      Event onMouseOver = EventBuilder.create(Event.ONMOUSEOVER).setTarget(target).build();
      Event onMouseDown = EventBuilder.create(Event.ONMOUSEDOWN).setTarget(target).setButton(
               Event.BUTTON_LEFT).build();
      Event onMouseUp = EventBuilder.create(Event.ONMOUSEUP).setTarget(target).setButton(
               Event.BUTTON_LEFT).build();
      Event onClick = EventBuilder.create(Event.ONCLICK).setTarget(target).build();

      dispatchEvent(parent, onMouseOver, onMouseDown, onMouseUp, onClick);
   }

   private static void clickOnCheckBox(CheckBox checkBox) {
      boolean newValue = RadioButton.class.isInstance(checkBox) ? true : !checkBox.getValue();

      // change value without triggering ValueChangeEvent : the trigger is done
      // in
      // CheckBox ensureDomEventHandlers()
      WidgetUtils.setCheckBoxValueSilent(checkBox, newValue);

      if (RadioButton.class.isInstance(checkBox)) {
         // change every other radiobutton and trigger ValueChangeEvent
         RadioButtonManager.onRadioGroupChanged((RadioButton) checkBox, newValue, true);
      }
   }

   private static void dispatchEventInternal(IsWidget target, Event event) {
      try {
         // special case of click on CheckBox : set the internal inputElement
         // value
         if (CheckBox.class.isInstance(target) && event.getTypeInt() == Event.ONCLICK) {
            clickOnCheckBox((CheckBox) target);
         }

         // set the related target
         Element relatedTargetElement = JavaScriptObjects.getObject(event,
                  JsoProperties.EVENT_RELATEDTARGET);

         if (relatedTargetElement == null) {
            switch (event.getTypeInt()) {
               case Event.ONMOUSEOVER:
               case Event.ONMOUSEOUT:
                  Widget parent = target.asWidget().getParent();
                  if (parent != null) {
                     relatedTargetElement = parent.getElement();
                  } else {
                     relatedTargetElement = Document.get().getDocumentElement();
                  }

                  JavaScriptObjects.setProperty(event, JsoProperties.EVENT_RELATEDTARGET,
                           relatedTargetElement);

                  break;
            }
         }

         // fire with bubble support
         Set<Widget> applied = new HashSet<Widget>();
         dispatchEventWithBubble(target, event, applied);

      } catch (UmbrellaException e) {
         if (AssertionError.class.isInstance(e.getCause())) {
            throw (AssertionError) e.getCause();
         } else if (RuntimeException.class.isInstance(e.getCause())) {
            throw (RuntimeException) e.getCause();
         } else {
            throw e;
         }
      }
   }

   private static void dispatchEventsInternal(IsWidget target, boolean check, Event... events) {

      if (events.length == 0) {
         return;
      }

      prepareEvents(target, events);

      boolean dipsatch = check ? canApplyEvent(target, events[0]) : true;

      if (dipsatch) {
         for (Event event : events) {
            dispatchEventInternal(target, event);
         }
      }
   }

   private static void dispatchEventWithBubble(IsWidget target, Event event, Set<Widget> applied) {
      if (target == null) {
         return;
      }

      Widget widget = target.asWidget();

      if (widget == null || isEventStopped(event) || applied.contains(widget)) {
         // cancel event handling
         return;
      } else if (widget.getParent() instanceof Composite) {
         // special case for composite, which trigger first its own handler,
         // than
         // the wrapped widget's handlers
         widget = widget.getParent();
      }

      // fire
      widget.onBrowserEvent(event);

      applied.add(widget);

      // process bubbling
      dispatchEventWithBubble(getParent(widget.getElement().getParentElement()), event, applied);
   }

   private static Widget getParent(Element parentElement) {
      if (parentElement == null) {
         return null;
      }

      Object o = DOM.getEventListener(parentElement.<com.google.gwt.user.client.Element> cast());

      if (!Widget.class.isInstance(o)) {
         return getParent(parentElement.getParentElement());
      } else {
         return (Widget) o;
      }
   }

   private static boolean isDisabled(Element element) {
      return element.getPropertyBoolean("disabled")
               || element.getClassName().contains("gwt-CheckBox-disabled");
   }

   private static boolean isEventStopped(Event event) {
      return JavaScriptObjects.getBoolean(event, "EVENT_isStopped");
   }

   private static boolean isVisible(Widget visibleRoot, Element element) {
      if (element == null) {
         return false;
      } else if (element == visibleRoot.getElement()) {
         return true;
      } else {

         return UIObject.isVisible(element) ? isVisible(visibleRoot, element.getParentElement())
                  : false;
      }
   }

   private static void prepareEvents(IsWidget target, Event... events) {
      for (Event event : events) {
         Element effectiveTarget = JavaScriptObjects.getObject(event, JsoProperties.EVENT_TARGET);

         if (effectiveTarget == null) {
            effectiveTarget = target.asWidget().getElement();
            JavaScriptObjects.setProperty(event, JsoProperties.EVENT_TARGET, effectiveTarget);
         }
      }
   }

   private Browser() {

   }

}
