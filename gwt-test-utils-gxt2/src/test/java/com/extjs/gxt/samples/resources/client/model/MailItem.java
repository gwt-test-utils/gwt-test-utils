/*
 * Ext GWT 2.2.1 - Ext for GWT Copyright(c) 2007-2010, Ext JS, LLC. licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.resources.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;

@SuppressWarnings("serial")
public class MailItem extends BaseModel {

   public MailItem() {

   }

   public MailItem(String sender, String email, String subject) {
      setSender(sender);
      setEmail(email);
      setSubject(subject);
   }

   public MailItem(String sender, String email, String subject, String body) {
      this(sender, email, subject);
      set("body", body);
   }

   public String getBody() {
      return (String) get("body");
   }

   public String getEmail() {
      return (String) get("email");
   }

   public String getSender() {
      return (String) get("sender");
   }

   public String getSubject() {
      return (String) get("subject");
   }

   public void setEmail(String email) {
      set("email", email);
   }

   public void setSender(String sender) {
      set("sender", sender);
   }

   public void setSubject(String subject) {
      set("subject", subject);
   }
}
