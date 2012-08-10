package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Assertions for {@link MenuItem}.
 * 
 * @author Gael Lazzari
 * 
 */
public class MenuItemAssert extends BaseUIObjectAssert<MenuItemAssert, MenuItem> {

   /**
    * Creates a new <code>{@link MenuItemAssert}</code>.
    * 
    * @param actual the actual value to verify.
    */
   protected MenuItemAssert(MenuItem actual) {
      super(MenuItemAssert.class, actual);
   }

   /**
    * Verifies that the actual {@link MenuItem} does not have an associated {@link Command}.
    * 
    * @return this assertion object.
    * @throws AssertionError if the actual {@link MenuItem} has an associated {@link Command}.
    */
   public MenuItemAssert doesNotHaveCommand() {
      if (actual.getCommand() == null)
         return this;

      throw failWithMessage("should not have an associated command");
   }

   /**
    * Verifies that the actual {@link MenuItem} does not have an associated parent menu.
    * 
    * @return this assertion object.
    * @throws AssertionError if the actual {@link MenuItem} has an associated parent menu.
    */
   public MenuItemAssert doesNotHaveParentMenu() {
      if (actual.getParentMenu() == null)
         return this;

      throw failWithMessage("should not have an associated parent menu");
   }

   /**
    * Verifies that the actual {@link MenuItem} does not have an associated sub-menu.
    * 
    * @return this assertion object.
    * @throws AssertionError if the actual {@link MenuItem} has an associated sub-menu.
    */
   public MenuItemAssert doesNotHaveSubMenu() {
      if (actual.getSubMenu() == null)
         return this;

      throw failWithMessage("should not have an associated sub-menu");
   }

   /**
    * Verifies that the actual {@link MenuItem} has an associated {@link Command}.
    * 
    * @return this assertion object.
    * @throws AssertionError if the actual {@link MenuItem} does not have an associated
    *            {@link Command}.
    */
   public MenuItemAssert hasCommand() {
      if (actual.getCommand() != null)
         return this;

      throw failWithMessage("should have an associated command");
   }

   /**
    * Verifies that the actual {@link MenuItem} has an associated parent menu.
    * 
    * @return this assertion object.
    * @throws AssertionError if the actual {@link MenuItem} does not have an associated parent menu.
    */
   public MenuItemAssert hasParentMenu() {
      if (actual.getParentMenu() != null)
         return this;

      throw failWithMessage("should have an associated parent menu");
   }

   /**
    * Verifies that the actual {@link MenuItem} has an associated sub-menu.
    * 
    * @return this assertion object.
    * @throws AssertionError if the actual {@link MenuItem} does not have an associated sub-menu.
    */
   public MenuItemAssert hasSubMenu() {
      if (actual.getSubMenu() != null)
         return this;

      throw failWithMessage("should have an associated sub-menu");
   }

   /**
    * Verifies that the actual {@link MenuItem} is currently enabled.
    * 
    * @return this assertion object.
    * @throws AssertionError if the actual {@link MenuItem} is not enabled.
    */
   public MenuItemAssert isEnabled() {
      if (actual.isEnabled())
         return this;

      throw failWithMessage("should be enabled");
   }

   /**
    * Verifies that the actual {@link MenuItem} is not currently enabled.
    * 
    * @return this assertion object.
    * @throws AssertionError if the actual {@link MenuItem} is enabled.
    */
   public MenuItemAssert isNotEnabled() {
      if (!actual.isEnabled())
         return this;

      throw failWithMessage("should not be enabled");
   }

}
