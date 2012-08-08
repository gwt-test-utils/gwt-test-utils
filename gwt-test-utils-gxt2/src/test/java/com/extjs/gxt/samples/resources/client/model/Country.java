/*
 * Ext GWT 2.2.1 - Ext for GWT Copyright(c) 2007-2010, Ext JS, LLC. licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.resources.client.model;

import com.extjs.gxt.ui.client.data.BaseModelData;

@SuppressWarnings("serial")
public class Country extends BaseModelData {

   public Country() {

   }

   public Country(String abbr, String name, int value) {
      setAbbr(abbr);
      setName(name);
      set("value", value);
   }

   public String getAbbr() {
      return get("abbr");
   }

   public String getName() {
      return get("name");
   }

   public void setAbbr(String abbr) {
      set("abbr", abbr);
   }

   public void setName(String name) {
      set("name", name);
   }

}
