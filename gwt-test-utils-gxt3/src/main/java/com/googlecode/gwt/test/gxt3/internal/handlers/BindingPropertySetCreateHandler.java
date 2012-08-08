package com.googlecode.gwt.test.gxt3.internal.handlers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.utils.JavassistUtils;
import com.sencha.gxt.core.client.BindingPropertySet;
import com.sencha.gxt.core.client.BindingPropertySet.PropertyName;
import com.sencha.gxt.core.client.BindingPropertySet.PropertyValue;

/**
 * Custom {@link GwtCreateHandler} implementation for {@link BindingPropertySet}
 * subtype instanciation.<strong>For internal use only.</strong>
 * 
 * @author Gael Lazzari
 * 
 */
public class BindingPropertySetCreateHandler implements GwtCreateHandler {

  private static final class BindingPropertySetInvocationHandler implements
      InvocationHandler {

    private final String propertyName;
    private final Class<?> proxiedClass;

    private BindingPropertySetInvocationHandler(Class<?> proxiedClass) {
      this.proxiedClass = proxiedClass;
      this.propertyName = JavassistUtils.getInvisibleAnnotationStringValue(
          proxiedClass, PropertyName.class, "value");
      if (propertyName == null) {
        throw new GwtTestException(proxiedClass.getName()
            + " must be annotated with @" + PropertyName.class.getSimpleName());
      }
    }

    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {
      String propertyValue = JavassistUtils.getInvisibleAnnotationStringValue(
          method, PropertyValue.class, "value");
      if (propertyValue == null) {
        throw new GwtTestException(method.toString()
            + " must be annotated with @" + PropertyValue.class.getSimpleName());
      }

      if (method.getReturnType() != Boolean.TYPE
          && method.getReturnType() != Boolean.class) {
        throw new GwtTestException(proxiedClass.getName()
            + " must return a boolean");
      }

      return propertyValue.equals(GwtConfig.get().getModuleRunner().getClientProperty(
          propertyName));
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.googlecode.gwt.test.GwtCreateHandler#create(java.lang.Class)
   */
  public Object create(Class<?> classLiteral) throws Exception {
    if (!BindingPropertySet.class.isAssignableFrom(classLiteral)) {
      return null;
    }

    InvocationHandler ih = new BindingPropertySetInvocationHandler(classLiteral);
    return Proxy.newProxyInstance(classLiteral.getClassLoader(),
        new Class<?>[]{classLiteral}, ih);
  }

}
