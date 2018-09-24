package com.googlecode.gwt.test.internal.handlers;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.exceptions.GwtTestPatchException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class HTMLTableImplCreateHandler implements GwtCreateHandler {

    private static final class HTMLTableImplInvocationHandler implements InvocationHandler {

        public Object invoke(Object proxy, Method method, Object[] args) {
            if (method.getName().equals("getRows")) {
                return getRows((Element) args[0]);
            } else if (method.getName().equals("getCells")) {
                return getCells((Element) args[0]);
            } else {
                throw new GwtTestPatchException(
                        "Unhandled method '"
                                + method.getDeclaringClass().getName()
                                + "."
                                + method.getName()
                                + "' by the default gwt-test-utils GwtCreateHandler for HTMLTable.HTMLTableImpl subtypes");
            }
        }

        private JsArray<Element> getCells(Element row) {
            return toArray(row.getElementsByTagName("td"));
        }

        private JsArray<Element> getRows(Element tbody) {
            return toArray(tbody.getElementsByTagName("tr"));
        }

        private JsArray<Element> toArray(NodeList<Element> elements) {
            JsArray<Element> array = JavaScriptObject.createArray().<JsArray<Element>>cast();
            for (int i = 0; i < elements.getLength(); i++) {
                array.set(i, elements.getItem(i));
            }

            return array;
        }
    }

    public Object create(Class<?> classLiteral) throws Exception {
        if (!classLiteral.getName().equals("com.google.gwt.user.client.ui.HTMLTable$HTMLTableImpl")) {
            return null;
        }

        return Proxy.newProxyInstance(classLiteral.getClassLoader(), new Class<?>[]{classLiteral},
                new HTMLTableImplInvocationHandler());
    }

}
