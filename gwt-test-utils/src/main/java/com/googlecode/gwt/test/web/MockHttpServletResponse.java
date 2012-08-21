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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.gwt.test.internal.utils.LinkedCaseInsensitiveMap;

/**
 * Mock implementation of the {@link javax.servlet.http.HttpServletResponse} interface. Supports the
 * Servlet 2.5 API level. Adapted from <strong>spring-test</strong>.
 * 
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @author Gael Lazzari
 */
public class MockHttpServletResponse implements HttpServletResponse {

   /**
    * Inner class that adapts the PrintWriter to mark the response as committed once the buffer size
    * is exceeded.
    */
   private class ResponsePrintWriter extends PrintWriter {

      public ResponsePrintWriter(Writer out) {
         super(out, true);
      }

      @Override
      public void flush() {
         super.flush();
         setCommitted(true);
      }

      @Override
      public void write(char buf[], int off, int len) {
         super.write(buf, off, len);
         super.flush();
         setCommittedIfBufferSizeExceeded();
      }

      @Override
      public void write(int c) {
         super.write(c);
         super.flush();
         setCommittedIfBufferSizeExceeded();
      }

      @Override
      public void write(String s, int off, int len) {
         super.write(s, off, len);
         super.flush();
         setCommittedIfBufferSizeExceeded();
      }
   }

   /**
    * Inner class that adapts the ServletOutputStream to mark the response as committed once the
    * buffer size is exceeded.
    */
   private class ResponseServletOutputStream extends DelegatingServletOutputStream {

      public ResponseServletOutputStream(OutputStream out) {
         super(out);
      }

      @Override
      public void flush() throws IOException {
         super.flush();
         setCommitted(true);
      }

      @Override
      public void write(int b) throws IOException {
         super.write(b);
         super.flush();
         setCommittedIfBufferSizeExceeded();
      }
   }

   // ---------------------------------------------------------------------
   // ServletResponse properties
   // ---------------------------------------------------------------------

   public static final int DEFAULT_SERVER_PORT = 80;

   private static final String CHARSET_PREFIX = "charset=";

   /**
    * Default character encoding to use when <code>request.getCharacterEncoding</code> returns
    * <code>null</code>, according to the Servlet spec.
    * 
    * @see ServletRequest#getCharacterEncoding
    */
   private static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";

   private int bufferSize = 4096;

   private String characterEncoding = DEFAULT_CHARACTER_ENCODING;

   private boolean committed;

   private final ByteArrayOutputStream content = new ByteArrayOutputStream();

   private int contentLength = 0;

   private String contentType;

   private final List<Cookie> cookies = new ArrayList<Cookie>();

   private String errorMessage;

   private String forwardedUrl;

   // ---------------------------------------------------------------------
   // HttpServletResponse properties
   // ---------------------------------------------------------------------

   private final Map<String, HeaderValueHolder> headers = new LinkedCaseInsensitiveMap<HeaderValueHolder>();

   private final List<String> includedUrls = new ArrayList<String>();

   private Locale locale = Locale.getDefault();

   private final ServletOutputStream outputStream = new ResponseServletOutputStream(this.content);

   private boolean outputStreamAccessAllowed = true;

   private String redirectedUrl;

   private int status = HttpServletResponse.SC_OK;

   // ---------------------------------------------------------------------
   // ServletResponse interface
   // ---------------------------------------------------------------------

   private PrintWriter writer;

   private boolean writerAccessAllowed = true;

   public void addCookie(Cookie cookie) {
      assertThat(cookie).as("Cookie must not be null").isNotNull();
      this.cookies.add(cookie);
   }

   public void addDateHeader(String name, long value) {
      addHeaderValue(name, value);
   }

   public void addHeader(String name, String value) {
      addHeaderValue(name, value);
   }

   public void addIncludedUrl(String includedUrl) {
      assertThat(includedUrl).as("Included URL must not be null").isNotNull();
      this.includedUrls.add(includedUrl);
   }

   public void addIntHeader(String name, int value) {
      addHeaderValue(name, value);
   }

   public boolean containsHeader(String name) {
      return (HeaderValueHolder.getByName(this.headers, name) != null);
   }

   public String encodeRedirectUrl(String url) {
      return encodeRedirectURL(url);
   }

   /**
    * The default implementation delegates to {@link #encodeURL}, returning the given URL String
    * as-is.
    * <p>
    * Can be overridden in subclasses, appending a session id or the like in a redirect-specific
    * fashion. For general URL encoding rules, override the common {@link #encodeURL} method
    * instead, appyling to redirect URLs as well as to general URLs.
    */
   public String encodeRedirectURL(String url) {
      return encodeURL(url);
   }

   public String encodeUrl(String url) {
      return encodeURL(url);
   }

   /**
    * The default implementation returns the given URL String as-is.
    * <p>
    * Can be overridden in subclasses, appending a session id or the like.
    */
   public String encodeURL(String url) {
      return url;
   }

   public void flushBuffer() {
      setCommitted(true);
   }

   public int getBufferSize() {
      return this.bufferSize;
   }

   public String getCharacterEncoding() {
      return this.characterEncoding;
   }

   public byte[] getContentAsByteArray() {
      flushBuffer();
      return this.content.toByteArray();
   }

   public String getContentAsString() throws UnsupportedEncodingException {
      flushBuffer();
      return (this.characterEncoding != null) ? this.content.toString(this.characterEncoding)
               : this.content.toString();
   }

   public int getContentLength() {
      return this.contentLength;
   }

   public String getContentType() {
      return this.contentType;
   }

   public Cookie getCookie(String name) {
      assertThat(name).as("Cookie name must not be null").isNotNull();
      for (Cookie cookie : this.cookies) {
         if (name.equals(cookie.getName())) {
            return cookie;
         }
      }
      return null;
   }

   public Cookie[] getCookies() {
      return this.cookies.toArray(new Cookie[this.cookies.size()]);
   }

   public String getErrorMessage() {
      return this.errorMessage;
   }

   public String getForwardedUrl() {
      return this.forwardedUrl;
   }

   /**
    * Return the primary value for the given header, if any.
    * <p>
    * Will return the first value in case of multiple values.
    * 
    * @param name the name of the header
    * @return the associated header value, or <code>null<code> if none
    */
   public Object getHeader(String name) {
      HeaderValueHolder header = HeaderValueHolder.getByName(this.headers, name);
      return (header != null ? header.getValue() : null);
   }

   // ---------------------------------------------------------------------
   // HttpServletResponse interface
   // ---------------------------------------------------------------------

   /**
    * Return the names of all specified headers as a Set of Strings.
    * 
    * @return the <code>Set</code> of header name <code>Strings</code>, or an empty <code>Set</code>
    *         if none
    */
   public Set<String> getHeaderNames() {
      return this.headers.keySet();
   }

   /**
    * Return all values for the given header as a List of value objects.
    * 
    * @param name the name of the header
    * @return the associated header values, or an empty List if none
    */
   public List<Object> getHeaders(String name) {
      HeaderValueHolder header = HeaderValueHolder.getByName(this.headers, name);
      return (header != null ? header.getValues() : Collections.emptyList());
   }

   public String getIncludedUrl() {
      int count = this.includedUrls.size();
      if (count > 1) {
         throw new IllegalStateException(
                  "More than 1 URL included - check getIncludedUrls instead: " + this.includedUrls);
      }
      return (count == 1 ? this.includedUrls.get(0) : null);
   }

   public List<String> getIncludedUrls() {
      return this.includedUrls;
   }

   public Locale getLocale() {
      return this.locale;
   }

   public ServletOutputStream getOutputStream() {
      if (!this.outputStreamAccessAllowed) {
         throw new IllegalStateException("OutputStream access not allowed");
      }
      return this.outputStream;
   }

   public String getRedirectedUrl() {
      return this.redirectedUrl;
   }

   public int getStatus() {
      return this.status;
   }

   public PrintWriter getWriter() throws UnsupportedEncodingException {
      if (!this.writerAccessAllowed) {
         throw new IllegalStateException("Writer access not allowed");
      }
      if (this.writer == null) {
         Writer targetWriter = (this.characterEncoding != null ? new OutputStreamWriter(
                  this.content, this.characterEncoding) : new OutputStreamWriter(this.content));
         this.writer = new ResponsePrintWriter(targetWriter);
      }
      return this.writer;
   }

   public boolean isCommitted() {
      return this.committed;
   }

   /**
    * Return whether {@link #getOutputStream()} access is allowed.
    */
   public boolean isOutputStreamAccessAllowed() {
      return this.outputStreamAccessAllowed;
   }

   /**
    * Return whether {@link #getOutputStream()} access is allowed.
    */
   public boolean isWriterAccessAllowed() {
      return this.writerAccessAllowed;
   }

   public void reset() {
      resetBuffer();
      this.characterEncoding = null;
      this.contentLength = 0;
      this.contentType = null;
      this.locale = null;
      this.cookies.clear();
      this.headers.clear();
      this.status = HttpServletResponse.SC_OK;
      this.errorMessage = null;
   }

   public void resetBuffer() {
      if (isCommitted()) {
         throw new IllegalStateException("Cannot reset buffer - response is already committed");
      }
      this.content.reset();
   }

   public void sendError(int status) throws IOException {
      if (isCommitted()) {
         throw new IllegalStateException("Cannot set error status - response is already committed");
      }
      this.status = status;
      setCommitted(true);
   }

   public void sendError(int status, String errorMessage) throws IOException {
      if (isCommitted()) {
         throw new IllegalStateException("Cannot set error status - response is already committed");
      }
      this.status = status;
      this.errorMessage = errorMessage;
      setCommitted(true);
   }

   public void sendRedirect(String url) throws IOException {
      if (isCommitted()) {
         throw new IllegalStateException("Cannot send redirect - response is already committed");
      }
      assertThat(url).as("Redirect URL must not be null").isNotNull();
      this.redirectedUrl = url;
      setCommitted(true);
   }

   public void setBufferSize(int bufferSize) {
      this.bufferSize = bufferSize;
   }

   public void setCharacterEncoding(String characterEncoding) {
      this.characterEncoding = characterEncoding;
   }

   public void setCommitted(boolean committed) {
      this.committed = committed;
   }

   public void setContentLength(int contentLength) {
      this.contentLength = contentLength;
   }

   public void setContentType(String contentType) {
      this.contentType = contentType;
      if (contentType != null) {
         int charsetIndex = contentType.toLowerCase().indexOf(CHARSET_PREFIX);
         if (charsetIndex != -1) {
            String encoding = contentType.substring(charsetIndex + CHARSET_PREFIX.length());
            setCharacterEncoding(encoding);
         }
      }
   }

   public void setDateHeader(String name, long value) {
      setHeaderValue(name, value);
   }

   public void setForwardedUrl(String forwardedUrl) {
      this.forwardedUrl = forwardedUrl;
   }

   public void setHeader(String name, String value) {
      setHeaderValue(name, value);
   }

   public void setIncludedUrl(String includedUrl) {
      this.includedUrls.clear();
      if (includedUrl != null) {
         this.includedUrls.add(includedUrl);
      }
   }

   public void setIntHeader(String name, int value) {
      setHeaderValue(name, value);
   }

   public void setLocale(Locale locale) {
      this.locale = locale;
   }

   // ---------------------------------------------------------------------
   // Methods for MockRequestDispatcher
   // ---------------------------------------------------------------------

   /**
    * Set whether {@link #getOutputStream()} access is allowed.
    * <p>
    * Default is <code>true</code>.
    */
   public void setOutputStreamAccessAllowed(boolean outputStreamAccessAllowed) {
      this.outputStreamAccessAllowed = outputStreamAccessAllowed;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   public void setStatus(int status, String errorMessage) {
      this.status = status;
      this.errorMessage = errorMessage;
   }

   /**
    * Set whether {@link #getWriter()} access is allowed.
    * <p>
    * Default is <code>true</code>.
    */
   public void setWriterAccessAllowed(boolean writerAccessAllowed) {
      this.writerAccessAllowed = writerAccessAllowed;
   }

   private void addHeaderValue(String name, Object value) {
      doAddHeaderValue(name, value, false);
   }

   private void doAddHeaderValue(String name, Object value, boolean replace) {
      HeaderValueHolder header = HeaderValueHolder.getByName(this.headers, name);
      assertThat(value).as("Header value must not be null").isNotNull();
      if (header == null) {
         header = new HeaderValueHolder();
         this.headers.put(name, header);
      }
      if (replace) {
         header.setValue(value);
      } else {
         header.addValue(value);
      }
   }

   private void setCommittedIfBufferSizeExceeded() {
      int bufSize = getBufferSize();
      if (bufSize > 0 && this.content.size() > bufSize) {
         setCommitted(true);
      }
   }

   private void setHeaderValue(String name, Object value) {
      doAddHeaderValue(name, value, true);
   }

}
