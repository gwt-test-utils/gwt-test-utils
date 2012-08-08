package com.googlecode.gwt.test.gxt3.internal.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.XTemplate;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;

public class XtemplatesCreateHandler implements GwtCreateHandler {

  private class XTemplatesInvocationHandler implements InvocationHandler {

    private final Class<?> proxiedClass;

    private XTemplatesInvocationHandler(Class<?> proxiedClass) {
      this.proxiedClass = proxiedClass;

    }

    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {

      Template template = templates.get(method);
      if (template == null) {
        template = createTemplate(method);
      }

      String[] paramNames = JavaFileParanamer.get().lookupParameterNames(method);

      Map<String, Object> dataModel = new HashMap<String, Object>();
      for (int i = 0; i < paramNames.length; i++) {
        dataModel.put(paramNames[i], args[i]);
      }

      StringWriter writer = new StringWriter();
      template.process(dataModel, writer);

      if (method.getReturnType() == String.class) {
        return writer.toString();
      } else {
        return SafeHtmlUtils.fromTrustedString(writer.toString());
      }

    }

    private Template createTemplate(Method method) {
      Configuration cfg = new Configuration();
      cfg.setObjectWrapper(ObjectWrapper.DEFAULT_WRAPPER);

      XTemplate xTemplate = method.getAnnotation(XTemplate.class);
      if (xTemplate == null) {
        throw new GwtTestException(
            "gwt-test-utils expects to find a @XTemplate annotation on method "
                + method.toString());
      }

      String templateName = method.toGenericString();

      if (xTemplate.source().length() > 0) {
        InputStream in = method.getDeclaringClass().getResourceAsStream(
            xTemplate.source());

        if (in == null) {
          throw new GwtTestException("Cannot find file @Template source file "
              + xTemplate.source() + " declared for method " + method);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        try {
          return new Template(templateName, br, cfg);
        } catch (IOException e) {
          throw new GwtTestException(
              "Error while trying to get template for method " + method);
        }
      } else {
        return Template.getPlainTextTemplate(templateName, xTemplate.value(),
            cfg);
      }

    }

  }

  private static final XtemplatesCreateHandler INSTANCE = new XtemplatesCreateHandler();

  public static final XtemplatesCreateHandler get() {
    return INSTANCE;
  }

  private final Configuration config;
  private final Map<Method, Template> templates;

  private XtemplatesCreateHandler() {
    this.config = new Configuration();
    this.templates = new HashMap<Method, Template>();
  }

  public Object create(Class<?> classLiteral) throws Exception {
    if (!XTemplates.class.isAssignableFrom(classLiteral)) {
      return null;
    }

    return Proxy.newProxyInstance(classLiteral.getClassLoader(),
        new Class<?>[]{classLiteral}, new XTemplatesInvocationHandler(
            classLiteral));

  }

}
