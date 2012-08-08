/*
 * Ext GWT 2.2.1 - Ext for GWT Copyright(c) 2007-2010, Ext JS, LLC. licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.resources.client.model;

import com.extjs.gxt.ui.client.data.BaseModelData;

@SuppressWarnings("serial")
public class Task extends BaseModelData {

   public Task(int id, String project, int taskId, String desc, double estimate, double rate,
            String due) {
      set("id", id);
      set("project", project);
      set("taskId", taskId);
      set("description", desc);
      set("estimate", estimate);
      set("rate", rate);
      set("due", due);
   }

   public Double getEstimate() {
      return (Double) get("estimate");
   }

   public double getRate() {
      return (Double) get("rate");
   }

}
