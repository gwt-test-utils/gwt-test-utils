/*
 * Ext GWT 2.2.1 - Ext for GWT Copyright(c) 2007-2010, Ext JS, LLC. licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.resources.client.model;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.i18n.client.DateTimeFormat;

@SuppressWarnings("serial")
public class Plant extends BaseModelData {

   private DateTimeFormat df = DateTimeFormat.getFormat("MM/dd/y");

   public Plant() {

   }

   public Plant(String name, String light, double price, String available, boolean indoor) {
      setName(name);
      setLight(light);
      setPrice(price);
      setAvailable(df.parse(available));
      setIndoor(indoor);
   }

   public Date getAvailable() {
      return get("available");
   }

   public String getLight() {
      return get("light");
   }

   public String getName() {
      return get("name");
   }

   public double getPrice() {
      return (Double) get("price");
   }

   public boolean isIndoor() {
      return (Boolean) get("indoor");
   }

   public void setAvailable(Date available) {
      set("available", available);
   }

   public void setIndoor(boolean indoor) {
      set("indoor", indoor);
   }

   public void setLight(String light) {
      set("light", light);
   }

   public void setName(String name) {
      set("name", name);
   }

   public void setPrice(double price) {
      set("price", price);
   }

}
