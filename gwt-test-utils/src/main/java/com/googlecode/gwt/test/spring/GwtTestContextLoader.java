package com.googlecode.gwt.test.spring;

import com.googlecode.gwt.test.internal.GwtFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.lang.NonNull;
import org.springframework.test.context.support.GenericXmlContextLoader;

public class GwtTestContextLoader extends GenericXmlContextLoader {

    @NonNull
    @Override
    protected BeanDefinitionReader createBeanDefinitionReader(GenericApplicationContext context) {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(context);
        beanDefinitionReader.setResourceLoader(new DefaultResourceLoader(
                GwtFactory.get().getClassLoader()));
        return beanDefinitionReader;
    }

}
