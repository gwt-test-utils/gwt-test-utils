/*
 * Ext GWT 2.2.1 - Ext for GWT Copyright(c) 2007-2010, Ext JS, LLC. licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.resources.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.PropertyChangeEvent;

public class TeamSales extends BaseModel {

   private static final long serialVersionUID = 2103699184769341265L;

   public TeamSales(String month, int a, int b, int c) {
      setMonth(month);
      setAlphaSales(a);
      setBetaSales(b);
      setGammaSales(c);
      setAvgSales();
   }

   public int getAlphaSales() {
      return (Integer) get("alphasales");
   }

   public int getBetaSales() {
      return (Integer) get("betasales");
   }

   public int getGammaSales() {
      return (Integer) get("gammasales");
   }

   public String getMonth() {
      return (String) get("month");
   }

   @Override
   public void notify(ChangeEvent evt) {
      super.notify(evt);

      PropertyChangeEvent e = (PropertyChangeEvent) evt;
      if (!e.getName().equals("avgsales")) {
         setAvgSales();
      }
   }

   public void setAlphaSales(int sales) {
      set("alphasales", sales);
   }

   public void setAvgSales() {
      if (get("alphasales") != null && get("gammasales") != null && get("betasales") != null) {
         double avg = (getAlphaSales() + getBetaSales() + getGammaSales()) / 3.0;
         set("avgsales", avg);
      }
   }

   public void setBetaSales(int sales) {
      set("betasales", sales);
   }

   public void setGammaSales(int sales) {
      set("gammasales", sales);
   }

   public void setMonth(String month) {
      set("month", month);
   }
}
