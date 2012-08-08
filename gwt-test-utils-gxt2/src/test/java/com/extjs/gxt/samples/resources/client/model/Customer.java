/*
 * Ext GWT 2.2.1 - Ext for GWT Copyright(c) 2007-2010, Ext JS, LLC. licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.resources.client.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Customer implements Serializable {

   private int age;
   private String email;
   private String name;

   public Customer() {

   }

   public Customer(String name, String email, int age) {
      this.age = age;
      this.email = email;
      this.name = name;
   }

   public int getAge() {
      return age;
   }

   public String getEmail() {
      return email;
   }

   public String getName() {
      return name;
   }

   public void setAge(int age) {
      this.age = age;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public void setName(String name) {
      this.name = name;
   }

}
