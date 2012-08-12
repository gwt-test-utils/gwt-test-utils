/*
 * Copyright 2002-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.gwt.test.web;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;

/**
 * Mock implementation of the {@link javax.servlet.http.HttpSession} interface. Supports the Servlet
 * 2.4 API level. Adapted from <strong>spring-test</strong>.
 * 
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @author Mark Fisher
 * @author Gael Lazzari
 */
@SuppressWarnings("deprecation")
public class MockHttpSession implements HttpSession {

   public static final String SESSION_COOKIE_NAME = "JSESSION";

   private static int nextId = 1;

   private final Map<String, Object> attributes = new LinkedHashMap<String, Object>();

   private final long creationTime = System.currentTimeMillis();

   private final String id;

   private boolean invalid = false;

   private boolean isNew = true;

   private long lastAccessedTime = System.currentTimeMillis();

   private int maxInactiveInterval;

   private final ServletContext servletContext;

   /**
    * Create a new MockHttpSession with a default {@link MockServletContext}.
    * 
    * @see MockServletContext
    */
   public MockHttpSession() {
      this(null);
   }

   /**
    * Create a new MockHttpSession.
    * 
    * @param servletContext the ServletContext that the session runs in
    */
   public MockHttpSession(ServletContext servletContext) {
      this(servletContext, null);
   }

   /**
    * Create a new MockHttpSession.
    * 
    * @param servletContext the ServletContext that the session runs in
    * @param id a unique identifier for this session
    */
   public MockHttpSession(ServletContext servletContext, String id) {
      this.servletContext = (servletContext != null ? servletContext : new MockServletContext());
      this.id = (id != null ? id : Integer.toString(nextId++));
   }

   public void access() {
      this.lastAccessedTime = System.currentTimeMillis();
      this.isNew = false;
   }

   /**
    * Clear all of this session's attributes.
    */
   public void clearAttributes() {
      for (Iterator<Map.Entry<String, Object>> it = this.attributes.entrySet().iterator(); it.hasNext();) {
         Map.Entry<String, Object> entry = it.next();
         String name = entry.getKey();
         Object value = entry.getValue();
         it.remove();
         if (value instanceof HttpSessionBindingListener) {
            ((HttpSessionBindingListener) value).valueUnbound(new HttpSessionBindingEvent(this,
                     name, value));
         }
      }
   }

   /**
    * Deserialize the attributes of this session from a state object created by
    * {@link #serializeState()}.
    * 
    * @param state a representation of this session's serialized state
    */
   @SuppressWarnings("unchecked")
   public void deserializeState(Serializable state) {
      assertThat(state).as("Serialized state needs to be of type [java.util.Map]").isInstanceOf(
               Map.class);
      this.attributes.putAll((Map<String, Object>) state);
   }

   public Object getAttribute(String name) {
      assertThat(name).as("Attribute name must not be null").isNotNull();
      return this.attributes.get(name);
   }

   public Enumeration<String> getAttributeNames() {
      return new Vector<String>(this.attributes.keySet()).elements();
   }

   public long getCreationTime() {
      return this.creationTime;
   }

   public String getId() {
      return this.id;
   }

   public long getLastAccessedTime() {
      return this.lastAccessedTime;
   }

   public int getMaxInactiveInterval() {
      return this.maxInactiveInterval;
   }

   public ServletContext getServletContext() {
      return this.servletContext;
   }

   public HttpSessionContext getSessionContext() {
      throw new UnsupportedOperationException("getSessionContext");
   }

   public Object getValue(String name) {
      return getAttribute(name);
   }

   public String[] getValueNames() {
      return this.attributes.keySet().toArray(new String[this.attributes.size()]);
   }

   public void invalidate() {
      this.invalid = true;
      clearAttributes();
   }

   public boolean isInvalid() {
      return this.invalid;
   }

   public boolean isNew() {
      return this.isNew;
   }

   public void putValue(String name, Object value) {
      setAttribute(name, value);
   }

   public void removeAttribute(String name) {
      assertThat(name).as("Attribute name must not be null").isNotNull();
      Object value = this.attributes.remove(name);
      if (value instanceof HttpSessionBindingListener) {
         ((HttpSessionBindingListener) value).valueUnbound(new HttpSessionBindingEvent(this, name,
                  value));
      }
   }

   public void removeValue(String name) {
      removeAttribute(name);
   }

   /**
    * Serialize the attributes of this session into an object that can be turned into a byte array
    * with standard Java serialization.
    * 
    * @return a representation of this session's serialized state
    */
   public Serializable serializeState() {
      HashMap<String, Serializable> state = new HashMap<String, Serializable>();
      for (Iterator<Map.Entry<String, Object>> it = this.attributes.entrySet().iterator(); it.hasNext();) {
         Map.Entry<String, Object> entry = it.next();
         String name = entry.getKey();
         Object value = entry.getValue();
         it.remove();
         if (value instanceof Serializable) {
            state.put(name, (Serializable) value);
         } else {
            // Not serializable... Servlet containers usually automatically
            // unbind the attribute in this case.
            if (value instanceof HttpSessionBindingListener) {
               ((HttpSessionBindingListener) value).valueUnbound(new HttpSessionBindingEvent(this,
                        name, value));
            }
         }
      }
      return state;
   }

   public void setAttribute(String name, Object value) {
      assertThat(name).as("Attribute name must not be null").isNotNull();
      if (value != null) {
         this.attributes.put(name, value);
         if (value instanceof HttpSessionBindingListener) {
            ((HttpSessionBindingListener) value).valueBound(new HttpSessionBindingEvent(this, name,
                     value));
         }
      } else {
         removeAttribute(name);
      }
   }

   public void setMaxInactiveInterval(int interval) {
      this.maxInactiveInterval = interval;
   }

   public void setNew(boolean value) {
      this.isNew = value;
   }

}
