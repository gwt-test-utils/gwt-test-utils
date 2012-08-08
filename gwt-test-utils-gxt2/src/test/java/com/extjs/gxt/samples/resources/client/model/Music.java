/*
 * Ext GWT 2.2.1 - Ext for GWT Copyright(c) 2007-2010, Ext JS, LLC. licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.resources.client.model;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

@SuppressWarnings("serial")
public class Music extends BaseTreeModel {

   public Music() {

   }

   public Music(String name) {
      set("name", name);
   }

   public Music(String name, String author, String genre) {
      set("name", name);
      set("author", author);
      set("genre", genre);
   }

   public String getAuthor() {
      return (String) get("author");
   }

   public String getGenre() {
      return (String) get("genre");
   }

   public String getName() {
      return (String) get("name");
   }

   public String toString() {
      return getName();
   }
}
