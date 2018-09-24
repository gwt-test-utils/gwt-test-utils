package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for JsArrayXXXPatcher
 *
 * @author Gael Lazzari
 */
class JsArrayHelper {

    interface Converter<T> {

        T convert(Object o);

        String serialize(Object o);

    }

    private static Converter<Boolean> booleanConverter;

    private static Converter<Integer> integerConverter;

    private static final String JSARRAY_WRAPPED_LIST = "JSARRAY_WRAPPED_LIST";

    private static Converter<Object> mixedConverter;

    private static Converter<Double> numberConverter;

    private static Converter<JavaScriptObject> objectConverter;

    private static Converter<String> stringConverter;

    public static <T> T get(JavaScriptObject jsArray, int index, Converter<T> converter) {
        List<Object> wrapped = getWrappedList(jsArray);

        Object o = (index < wrapped.size()) ? wrapped.get(index) : null;
        return converter.convert(o);
    }

    public static Converter<Boolean> getBooleanConverter() {
        if (booleanConverter == null) {
            booleanConverter = new Converter<Boolean>() {

                public Boolean convert(Object o) {
                    return (o != null) ? Boolean.valueOf(o.toString()) : false;
                }

                public String serialize(Object o) {
                    return (o != null) ? o.toString() : "";
                }
            };
        }

        return booleanConverter;
    }

    public static Converter<Integer> getIntegerConverter() {
        if (integerConverter == null) {
            integerConverter = new Converter<Integer>() {

                public Integer convert(Object o) {
                    return (o != null) ? Integer.valueOf(o.toString()) : 0;
                }

                public String serialize(Object o) {
                    return (o != null) ? o.toString() : "";
                }
            };
        }

        return integerConverter;
    }

    public static Converter<Object> getMixedConverter() {
        if (mixedConverter == null) {
            mixedConverter = new Converter<Object>() {

                public Object convert(Object o) {
                    return o;
                }

                public String serialize(Object o) {
                    if (o == null) {
                        return "";
                    } else if (o instanceof JavaScriptObject) {
                        return getObjectConverter().serialize(o);
                    } else {
                        return o.toString();
                    }
                }
            };
        }

        return mixedConverter;
    }

    public static Converter<Double> getNumberConverter() {
        if (numberConverter == null) {
            numberConverter = new Converter<Double>() {

                public Double convert(Object o) {
                    return (o != null) ? Double.valueOf(o.toString()) : 0;
                }

                public String serialize(Object o) {
                    return (o != null) ? o.toString() : "";
                }
            };
        }

        return numberConverter;
    }

    public static Converter<JavaScriptObject> getObjectConverter() {
        if (objectConverter == null) {
            objectConverter = new Converter<JavaScriptObject>() {

                public JavaScriptObject convert(Object o) {
                    if (o == null) {
                        return null;
                    } else if (o instanceof JavaScriptObject) {
                        return (JavaScriptObject) o;
                    } else {
                        return JsonUtils.safeEval(o.toString());
                    }
                }

                public String serialize(Object o) {
                    if (o == null) {
                        return "";
                    } else if (o instanceof JavaScriptObject) {
                        JavaScriptObject jso = (JavaScriptObject) o;
                        return JsoUtils.serialize(jso);
                    } else {
                        return o.toString();
                    }
                }
            };
        }

        return objectConverter;
    }

    public static Converter<String> getStringConverter() {
        if (stringConverter == null) {
            stringConverter = new Converter<String>() {

                public String convert(Object o) {
                    if (o == null) {
                        return null;
                    } else if (o instanceof JavaScriptObject) {
                        return JsoUtils.serialize((JavaScriptObject) o);
                    } else {
                        return o.toString();
                    }
                }

                public String serialize(Object o) {
                    if (o == null) {
                        return "";
                    } else if (o instanceof JavaScriptObject) {
                        return JsoUtils.serialize((JavaScriptObject) o);
                    } else {
                        return o.toString();
                    }
                }
            };
        }

        return stringConverter;
    }

    @SuppressWarnings("unchecked")
    public static List<Object> getWrappedList(JavaScriptObject jsArray) {

        List<Object> wrappedList = JavaScriptObjects.getObject(jsArray, JSARRAY_WRAPPED_LIST);

        if (wrappedList == null) {
            wrappedList = new ArrayList<>();
            JavaScriptObjects.setProperty(jsArray, JSARRAY_WRAPPED_LIST, wrappedList);
        }

        return wrappedList;
    }

    public static <T> String join(JavaScriptObject jsArray, String separator, Converter<T> converter) {
        StringBuilder sb = new StringBuilder();

        for (Object o : getWrappedList(jsArray)) {
            sb.append(converter.serialize(o));
            sb.append(separator);
        }

        return sb.substring(0, sb.length() - separator.length());
    }

    public static int length(JavaScriptObject jsArray) {
        return getWrappedList(jsArray).size();
    }

    public static void push(JavaScriptObject jsArray, Object value) {
        getWrappedList(jsArray).add(value);
    }

    public static void set(JavaScriptObject jsArray, int index, Object value) {
        List<Object> wrapped = getWrappedList(jsArray);
        int currentSize = wrapped.size();

        if (index >= currentSize) {
            for (int i = currentSize; i <= index; i++) {
                wrapped.add(null);
            }
        }

        wrapped.set(index, value);
    }

    public static void setLength(JavaScriptObject jsArray, int newLength) {
        List<Object> list = getWrappedList(jsArray);
        int currentSize = list.size();
        if (currentSize > newLength) {
            for (int i = newLength; i < currentSize; i++) {
                list.remove(i - 1);
            }
        } else if (currentSize < newLength) {
            for (int i = currentSize; i <= newLength; i++) {
                list.add(null);
            }
        }
    }

    public static <T> T shift(JavaScriptObject jsArray, Converter<T> converter) {
        List<Object> wrapped = getWrappedList(jsArray);
        if (wrapped.size() == 0) {
            return null;
        }

        Object o = wrapped.remove(0);
        return converter.convert(o);
    }

    public static void unshift(JavaScriptObject jsArray, Object value) {
        getWrappedList(jsArray).add(0, value);
    }

}
