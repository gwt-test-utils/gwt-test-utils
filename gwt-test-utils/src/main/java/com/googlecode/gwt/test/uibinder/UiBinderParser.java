package com.googlecode.gwt.test.uibinder;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.exceptions.GwtTestUiBinderException;
import com.googlecode.gwt.test.internal.utils.XmlUtils;
import com.googlecode.gwt.test.utils.JavassistUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Class in charge of parsing the .ui.xml file and filling both root element/widget and all
 * {@link UiField} in the owner object.
 *
 * @author Gael Lazzari
 */
class UiBinderParser {

    /**
     * Parse the .ui.xml file to fill the corresponding objects.
     *
     * @param rootComponentClass the root component's class that UiBinder has to instanciated.
     * @param uiBinderClass      the UiBinder subinterface which is used
     * @param owner              The owner of the UiBinder template, with {@link UiField} fields.
     */
    <T> T createUiComponent(Class<UiBinder<?, ?>> uiBinderClass, Object owner) {
        @SuppressWarnings("unchecked")
        Class<T> rootComponentClass = (Class<T>) getRootElementClass(uiBinderClass);
        InputStream uiXmlStream = getUiXmlFile(owner.getClass(), uiBinderClass);
        if (uiXmlStream == null) {
            throw new GwtTestUiBinderException("Cannot find the .ui.xml file corresponding to '"
                    + owner.getClass().getName() + "'");
        }

        UiXmlContentHandler<T> contentHandler = new UiXmlContentHandler<T>(rootComponentClass, owner);

        XMLReader saxReader = XmlUtils.newXMLReader();

        try {
            saxReader.setContentHandler(contentHandler);
            saxReader.parse(new InputSource(uiXmlStream));
        } catch (Exception e) {
            if (GwtTestException.class.isInstance(e)) {
                throw (GwtTestException) e;
            } else {
                throw new GwtTestUiBinderException("Error while parsing '"
                        + owner.getClass().getSimpleName() + ".ui.xml'", e);
            }
        } finally {
            try {
                uiXmlStream.close();
            } catch (IOException e) {
                // do nothing
            }
        }

        return contentHandler.getRootComponent();
    }

    /**
     * Get the actual class of the <U> parameter.
     *
     * @return The class of the root element or widget generated from UiBinder.
     */
    private Class<?> getRootElementClass(Class<UiBinder<?, ?>> uiBinderClass) {
        for (Type type : uiBinderClass.getGenericInterfaces()) {

            if (type instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) type;

                return (Class<?>) pType.getActualTypeArguments()[0];
            }
        }

        throw new GwtTestUiBinderException("The UiBinder subinterface '" + uiBinderClass.getName()
                + "' is not parameterized. Please add its generic types.");
    }

    private InputStream getUiXmlFile(Class<?> ownerClass, Class<UiBinder<?, ?>> uiBinderClass) {
        InputStream is = ownerClass.getResourceAsStream(ownerClass.getSimpleName() + ".ui.xml");

        if (is == null) {
            String uiTemplateValue = JavassistUtils.getInvisibleAnnotationStringValue(uiBinderClass,
                    UiTemplate.class, "value");
            if (uiTemplateValue != null) {
                is = ownerClass.getResourceAsStream(uiTemplateValue);
            }
        }

        if (is == null && ownerClass.getSuperclass() != Object.class) {
            is = getUiXmlFile(ownerClass.getSuperclass(), uiBinderClass);
        }

        return is;
    }

}
