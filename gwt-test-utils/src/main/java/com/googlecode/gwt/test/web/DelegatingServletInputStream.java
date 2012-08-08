/*
 * Copyright 2002-2009 the original author or authors.
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

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

/**
 * Delegating implementation of {@link javax.servlet.ServletInputStream}. Adapted from
 * <strong>spring-test</strong>.
 * 
 * @author Juergen Hoeller
 * @author Gael Lazzari
 * 
 * @see MockHttpServletRequest
 */
class DelegatingServletInputStream extends ServletInputStream {

   private final InputStream sourceStream;

   /**
    * Create a DelegatingServletInputStream for the given source stream.
    * 
    * @param sourceStream the source stream (never <code>null</code>)
    */
   public DelegatingServletInputStream(InputStream sourceStream) {
      assertThat(sourceStream).as("Source InputStream must not be null").isNotNull();
      this.sourceStream = sourceStream;
   }

   @Override
   public void close() throws IOException {
      super.close();
      this.sourceStream.close();
   }

   /**
    * Return the underlying source stream (never <code>null</code>).
    */
   public final InputStream getSourceStream() {
      return this.sourceStream;
   }

   @Override
   public int read() throws IOException {
      return this.sourceStream.read();
   }

}
