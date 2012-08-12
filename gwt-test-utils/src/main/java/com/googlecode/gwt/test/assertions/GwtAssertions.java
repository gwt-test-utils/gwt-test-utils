package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point for assertion methods for different GWT types. Each method in this class is a static
 * factory for the type-specific assertion objects. The purpose of this class is to make test code
 * more readable, following <strong>fest-assert</strong> principles.
 * 
 * @author Gael Lazzari
 */
public class GwtAssertions {

   /**
    * Creates a new instance of <code>{@link FocusWidgetAssert}</code>.
    * 
    * @param actual the generic focus widget to be the target of the assertions methods.
    * @return the created assertion object.
    */
   public static FocusWidgetAssert assertThat(FocusWidget actual) {
      return new FocusWidgetAssert(actual);
   }

   /**
    * Creates a new instance of <code>{@link WidgetAssert}</code>.
    * 
    * @param actual the generic widget to be the target of the assertions methods.
    * @return the created assertion object.
    */
   public static GwtInstanceAssert assertThat(GwtInstance actual) {
      return new GwtInstanceAssert(actual);
   }

   /**
    * Creates a new instance of <code>{@link MenuItemAssert}</code>.
    * 
    * @param actual the menu item to be the target of the assertions methods.
    * @return the created assertion object.
    */
   public static MenuItemAssert assertThat(MenuItem actual) {
      return new MenuItemAssert(actual);
   }

   /**
    * Creates a new instance of <code>{@link TreeItemAssert}</code>.
    * 
    * @param actual the tree item to be the target of the assertions methods.
    * @return the created assertion object.
    */
   public static TreeItemAssert assertThat(TreeItem actual) {
      return new TreeItemAssert(actual);
   }

   /**
    * Creates a new instance of <code>{@link UIObjectAssert}</code>.
    * 
    * @param actual the generic UiObject to be the target of the assertions methods.
    * @return the created assertion object.
    */
   public static UIObjectAssert assertThat(UIObject actual) {
      return new UIObjectAssert(actual);
   }

   /**
    * Creates a new instance of <code>{@link WidgetAssert}</code>.
    * 
    * @param actual the generic widget to be the target of the assertions methods.
    * @return the created assertion object.
    */
   public static WidgetAssert assertThat(Widget actual) {
      return new WidgetAssert(actual);
   }

   public static GwtInstance uiObject(String... identifier) {
      return new GwtInstance(identifier);
   }

}
