package com.googlecode.gwt.test.uibinder;

import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBoxBase.TextAlignConstant;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.gwt.test.exceptions.ReflectionException;
import org.apache.commons.beanutils.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to populate Widgets declared in .ui.xml files. It relies on
 * <code>commons-beanutils</code> API.
 *
 * @author Gael Lazzari
 */
@SuppressWarnings("deprecation")
public class UiBinderBeanUtils {

    private static final BeanUtilsBean UIBINDER_BEANUTILS = new BeanUtilsBean(
            new ConvertUtilsBean() {
                @SuppressWarnings({"unchecked", "rawtypes"})
                @Override
                public Object convert(String value, Class clazz) {
                    if (clazz.isEnum()) {
                        return Enum.valueOf(clazz, value);
                    } else {
                        return super.convert(value, clazz);
                    }
                }
            }, new PropertyUtilsBean());

    static {
        registerConverter((type, value) ->
                UiBinderXmlUtils.parseHorizontalAlignment(value.toString()), HorizontalAlignmentConstant.class
        );

        registerConverter((type, value) ->
                UiBinderXmlUtils.parseVerticalAlignment(value.toString()), VerticalAlignmentConstant.class
        );

        registerConverter((type, value) ->
                UiBinderXmlUtils.parseTextAlignConstant(value.toString()), TextAlignConstant.class
        );
    }

    /**
     * @param o
     * @param properties
     * @see BeanUtilsBean#populate(Object, Map)
     */
    public static void populateObject(Object o, Map<String, Object> properties) {
        try {
            Map<String, Object> filteredProperties = new HashMap<>();
            for (String key : properties.keySet()) {
                if (PropertyUtils.isWriteable(o, key)) {
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
     * @param converter
     * @param clazz
     * @see ConvertUtils#register(Converter, Class)
     */
    public static void registerConverter(Converter converter, Class<?> clazz) {
        UIBINDER_BEANUTILS.getConvertUtils().register(converter, clazz);
    }

}
