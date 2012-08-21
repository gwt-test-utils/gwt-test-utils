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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.activation.FileTypeMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mock implementation of the {@link javax.servlet.ServletContext} interface. Adapted from
 * <strong>spring-test</strong>.
 * 
 * 
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Gael Lazzari
 */
@SuppressWarnings("restriction")
public class MockServletContext implements ServletContext {

   /**
    * Inner factory class used to just introduce a Java Activation Framework dependency when
    * actually asked to resolve a MIME type.
    */
   private static class MimeTypeResolver {

      public static String getMimeType(String filePath) {
         return FileTypeMap.getDefaultFileTypeMap().getContentType(filePath);
      }
   }

   /**
    * Standard Servlet spec context attribute that specifies a temporary directory for the current
    * web application, of type <code>java.io.File</code>.
    */
   public static final String TEMP_DIR_CONTEXT_ATTRIBUTE = "javax.servlet.context.tempdir";

   private final Map<String, Object> attributes = new LinkedHashMap<String, Object>();

   private String contextPath = "";

   private final Map<String, ServletContext> contexts = new HashMap<String, ServletContext>();

   private final Map<String, String> initParameters = new LinkedHashMap<String, String>();

   private final Logger logger = LoggerFactory.getLogger(getClass());

   private int minorVersion = 5;

   private String serverInfo = "MockServerInfo";

   private String servletContextName = "MockServletContext";

   public void addInitParameter(String name, String value) {
      assertThat(name).as("Parameter name must not be null").isNotNull();
      this.initParameters.put(name, value);
   }

   public Object getAttribute(String name) {
      assertThat(name).as("Attribute name must not be null").isNotNull();
      return this.attributes.get(name);
   }

   public Enumeration<String> getAttributeNames() {
      return new Vector<String>(this.attributes.keySet()).elements();
   }

   public ServletContext getContext(String contextPath) {
      if (this.contextPath.equals(contextPath)) {
         return this;
      }
      return this.contexts.get(contextPath);
   }

   /* This is a Servlet API 2.5 method. */
   public String getContextPath() {
      return this.contextPath;
   }

   public String getInitParameter(String name) {
      assertThat(name).as("Parameter name must not be null").isNotNull();
      return this.initParameters.get(name);
   }

   public Enumeration<String> getInitParameterNames() {
      return Collections.enumeration(this.initParameters.keySet());
   }

   public int getMajorVersion() {
      return 2;
   }

   public String getMimeType(String filePath) {
      return MimeTypeResolver.getMimeType(filePath);
   }

   public int getMinorVersion() {
      return this.minorVersion;
   }

   public RequestDispatcher getNamedDispatcher(String path) {
      return null;
   }

   public String getRealPath(String path) {
      return null;
   }

   public RequestDispatcher getRequestDispatcher(String path) {
      if (!path.startsWith("/")) {
         throw new IllegalArgumentException(
                  "RequestDispatcher path at ServletContext level must start with '/'");
      }
      return new MockRequestDispatcher(path);
   }

   public URL getResource(String path) throws MalformedURLException {
      return null;
   }

   public InputStream getResourceAsStream(String path) {
      return null;
   }

   public Set<String> getResourcePaths() {
      return Collections.emptySet();
   }

   public Set<?> getResourcePaths(String arg0) {
      return Collections.emptySet();
   }

   public String getServerInfo() {
      return serverInfo;
   }

   public Servlet getServlet(String name) {
      return null;
   }

   public String getServletContextName() {
      return this.servletContextName;
   }

   public Enumeration<String> getServletNames() {
      return Collections.enumeration(new HashSet<String>());
   }

   public Enumeration<Servlet> getServlets() {
      return Collections.enumeration(new HashSet<Servlet>());
   }

   public void log(Exception ex, String message) {
      logger.info(message, ex);
   }

   public void log(String message) {
      logger.info(message);
   }

   public void log(String message, Throwable ex) {
      logger.info(message, ex);
   }

   public void registerContext(String contextPath, ServletContext context) {
      this.contexts.put(contextPath, context);
   }

   public void removeAttribute(String name) {
      assertThat(name).as("Attribute name must not be null").isNotNull();
      this.attributes.remove(name);
   }

   public void setAttribute(String name, Object value) {
      assertThat(name).as("Attribute name must not be null").isNotNull();
      if (value != null) {
         this.attributes.put(name, value);
      } else {
         this.attributes.remove(name);
      }
   }

   public void setContextPath(String contextPath) {
      this.contextPath = (contextPath != null ? contextPath : "");
   }

   public void setMinorVersion(int minorVersion) {
      if (minorVersion < 3 || minorVersion > 5) {
         throw new IllegalArgumentException(
                  "Only Servlet minor versions between 3 and 5 are supported");
      }
      this.minorVersion = minorVersion;
   }

   public void setServerInfo(String serverInfo) {
      this.serverInfo = serverInfo;
   }

   public void setServletContextName(String servletContextName) {
      this.servletContextName = servletContextName;
   }

}
