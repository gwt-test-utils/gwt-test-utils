package com.googlecode.gwt.test.uibinder;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;

import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBoxBase.TextAlignConstant;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.gwt.test.exceptions.ReflectionException;

/**
 * Utility class to populate Widgets declared in .ui.xml files. It relies on
 * <code>commons-beanutils</code> API.
 * 
 * @author Gael Lazzari
 * 
 */
@SuppressWarnings("deprecation")
public class UiBinderBeanUtils {

   private static final BeanUtilsBean UIBINDER_BEANUTILS = new BeanUtilsBean();

   static {
      registerConverter(new Converter() {

         @SuppressWarnings("rawtypes")
         public Object convert(Class type, Object value) {
            return UiBinderXmlUtils.parseHorizontalAlignment(value.toString());
         }
      }, HorizontalAlignmentConstant.class);

      registerConverter(new Converter() {

         @SuppressWarnings("rawtypes")
         public Object convert(Class type, Object value) {
            return UiBinderXmlUtils.parseVerticalAlignment(value.toString());
         }
      }, VerticalAlignmentConstant.class);

      registerConverter(new Converter() {

         @SuppressWarnings("rawtypes")
         public Object convert(Class type, Object value) {
            return UiBinderXmlUtils.parseTextAlignConstant(value.toString());
         }
      }, TextAlignConstant.class);
   }

   /**
    * 
    * @param o
    * @param properties
    * 
    * @see BeanUtilsBean#populate(Object, Map)
    */
   public static void populateObject(Object o, Map<String, Object> properties) {
      try {
         Map<String, Object> filteredProperties = new HashMap<String, Object>();
         for(String key :properties.keySet()){
            if(PropertyUtils.isWriteable(o, key)){
               filteredProperties.put(key, properties.get(key));
            }
         }
         UIBINDER_BEANUTILS.populate(o, filteredProperties);
      } catch (Exception e) {
         throw new ReflectionException("UiBinder error while setting properties for '"
                  + o.getClass().getSimpleName() + "'", e);
      }

      // handle specifics
      String[] styles = (String[]) properties.get("addStyleNames");
      if (styles != null) {
         for (String style : styles) {
            if (o instanceof IsWidget) {
               ((IsWidget) o).asWidget().addStyleName(style);
            } else if (o instanceof UIObject) {
               ((UIObject) o).addStyleName(style);
            }

         }
      }
   }

   /**
    * 
    * @param converter
    * @param clazz
    * 
    * @see ConvertUtils#register(Converter, Class)
    */
   public static void registerConverter(Converter converter, Class<?> clazz) {
      UIBINDER_BEANUTILS.getConvertUtils().register(converter, clazz);
   }

}
