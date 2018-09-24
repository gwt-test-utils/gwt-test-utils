package com.googlecode.gwt.test.internal.handlers;

import com.google.gwt.place.impl.AbstractPlaceHistoryMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.WithTokenizers;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

class PlaceHistoryMapperCreateHandler implements GwtCreateHandler {

    private static final class GwtTestPlaceHistoryMapper extends AbstractPlaceHistoryMapper<Object> {

        private final Map<Class<?>, String> prefixMap;
        private final Map<String, PlaceTokenizer<?>> tokenizerMap;

        private GwtTestPlaceHistoryMapper(Class<? extends PlaceTokenizer<?>>[] placeTokenizers) {

            this.prefixMap = new HashMap<>();
            this.tokenizerMap = new HashMap<>();

            collectTokenizerByPrefix(placeTokenizers);
        }

        @Override
        protected PrefixAndToken getPrefixAndToken(Place newPlace) {
            String prefix = prefixMap.get(newPlace.getClass());

            PlaceTokenizer<?> tokenizer = getTokenizer(prefix);

            if (tokenizer == null) {
                return null;
            }

            String token = GwtReflectionUtils.callPrivateMethod(tokenizer, "getToken", newPlace);

            return new PrefixAndToken(prefix, token);
        }

        @Override
        protected PlaceTokenizer<?> getTokenizer(String prefix) {
            return tokenizerMap.get(prefix);
        }

        private void collectTokenizerByPrefix(Class<? extends PlaceTokenizer<?>>[] placeTokenizers) {

            for (Class<? extends PlaceTokenizer<?>> tokenizerClass : placeTokenizers) {
                try {
                    Method getPlaceMethod = tokenizerClass.getMethod("getPlace", String.class);
                    Class<?> placeClass = getPlaceMethod.getReturnType();

                    String placePrefix = createUniquePrefix(placeClass);

                    PlaceTokenizer<?> instance = GwtReflectionUtils.instantiateClass(tokenizerClass);
                    tokenizerMap.put(placePrefix, instance);
                    prefixMap.put(placeClass, placePrefix);

                } catch (Exception e) {
                    // should never happened : just mute this throw
                    e.printStackTrace();
                }
            }

        }

        private String createUniquePrefix(Class<?> placeClass) {
            String prefix = placeClass.getSimpleName();

            String uniquePrefix = prefix;
            int i = 1;

            while (tokenizerMap.containsKey(uniquePrefix)) {
                uniquePrefix = prefix + i;
                i++;
            }

            return uniquePrefix;
        }

    }

    private static final class PlaceHistoryInvocationHandler implements InvocationHandler {

        private final GwtTestPlaceHistoryMapper placeHistoryMapper;

        private PlaceHistoryInvocationHandler(Class<? extends PlaceTokenizer<?>>[] placeTokenizers) {
            placeHistoryMapper = new GwtTestPlaceHistoryMapper(placeTokenizers);
        }

        public Object invoke(Object proxy, Method method, Object[] args) {
            switch (method.getName()) {
                case "getPlace":
                    return placeHistoryMapper.getPlace((String) args[0]);
                case "getToken":
                    return placeHistoryMapper.getToken((Place) args[0]);
                default:
                    throw new GwtTestPatchException(
                            "Unhandled method '"
                                    + method.getDeclaringClass().getName()
                                    + "."
                                    + method.getName()
                                    + "' by the default gwt-test-utils GwtCreateHandler for PlaceHistoryMapper subtypes");
            }
        }
    }

    public Object create(Class<?> classLiteral) {
        if (!PlaceHistoryMapper.class.isAssignableFrom(classLiteral)) {
            return null;
        }

        WithTokenizers withTokenizers = classLiteral.getAnnotation(WithTokenizers.class);
        if (withTokenizers == null) {
            throw new GwtTestPatchException(
                    "Error while trying to create an instance of "
                            + classLiteral.getName()
                            + " : gwt-test-utils default GwtCreateHandler for PlaceHistoryMapper is not currently able to create a valid implementation without relying on the @WithTokenizer annotation");
        }

        return Proxy.newProxyInstance(classLiteral.getClassLoader(), new Class<?>[]{classLiteral},
                new PlaceHistoryInvocationHandler(withTokenizers.value()));

    }

}
