package com.googlecode.gwt.test.uibinder;

import com.google.gwt.dev.Link;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gwt.test.exceptions.GwtTestUiBinderException;
import com.googlecode.gwt.test.exceptions.ReflectionException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

class UiBinderInvocationHandler implements InvocationHandler {

    private static final Map<Class<?>, GwtEvent<?>> EVENT_PROTOTYPES = new HashMap<>();

    private final Class<UiBinder<?, ?>> proxiedClass;

    UiBinderInvocationHandler(Class<UiBinder<?, ?>> proxiedClass) {
        this.proxiedClass = proxiedClass;
    }

    public Object invoke(Object proxy, Method method, Object[] args) {
        if (method.getName().equals("createAndBindUi")) {
            return createAndBindUi(args[0]);
        } else {
            throw new GwtTestUiBinderException("Not managed method for UiBinder : " + method.getName());
        }
    }

    @SuppressWarnings("unchecked")
    private <H extends EventHandler> void addHandlers(Object owner) {
        Map<Method, UiHandler> uiHandlerMethods = GwtReflectionUtils.getAnnotatedMethod(
                owner.getClass(), UiHandler.class);

        for (Map.Entry<Method, UiHandler> entry : uiHandlerMethods.entrySet()) {
            for (String uiFieldName : entry.getValue().value()) {
                Widget uiField = GwtReflectionUtils.getPrivateFieldValue(owner, uiFieldName);
                GwtEvent.Type<H> eventType = (GwtEvent.Type<H>) getEventType(entry.getKey());

                H handler = (H) createHandler(uiField, entry.getKey(), owner);

                if (eventType instanceof DomEvent.Type) {
                    uiField.addDomHandler(handler, (DomEvent.Type<H>) eventType);
                } else {

                    // special case for ValueChangeEvent and HasValueChangeHandlers

                    if (uiField instanceof HasValueChangeHandlers
                            && handler instanceof ValueChangeHandler) {
                        ((HasValueChangeHandlers<Object>) uiField).addValueChangeHandler((ValueChangeHandler<Object>) handler);
                    } else {
                        uiField.addHandler(handler, eventType);
                    }

                }
            }

        }
    }

    /**
     * This method is in charge of the instanciation of DOM object / GWT widget and their binding
     * with @UiField in the owner
     *
     * @param owner The owner UiBinder subclass instance.
     * @return The root component, initially returned by {@link Link
     * UiBinder#createAndBindUi(Object)}.
     */
    private Object createAndBindUi(Object owner) {
        UiBinderParser parser = new UiBinderParser();

        // parse .ui.xml file and inject @UiField
        Object rootComponent = parser.createUiComponent(proxiedClass, owner);
        // handle @UiHandlers
        addHandlers(owner);

        return rootComponent;
    }

    private InvocationHandler createEventHandlerInvocationHandler(final Method uiHandlerMethod,
                                                                  final Object uiHandlerOwner) {

        return new InvocationHandler() {

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return GwtReflectionUtils.callPrivateMethod(uiHandlerOwner, uiHandlerMethod, args);
            }
        };
    }

    private EventHandler createHandler(Widget uiField, Method uiHandlerMethod, Object uiHandlerOwner) {
        Class<EventHandler> eventHandlerClass = findHandlerClass(uiHandlerMethod);
        InvocationHandler eventHandlerInvocationHandler = createEventHandlerInvocationHandler(
                uiHandlerMethod, uiHandlerOwner);

        return (EventHandler) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class<?>[]{eventHandlerClass}, eventHandlerInvocationHandler);
    }

    @SuppressWarnings("unchecked")
    private Class<EventHandler> findHandlerClass(Method uiHandlerMethod) {
        Class<?> eventTypeClass = uiHandlerMethod.getParameterTypes()[0];
        String eventHandlerClassName = eventTypeClass.getName().substring(0,
                eventTypeClass.getName().lastIndexOf("Event"))
                + "Handler";

        try {
            return (Class<EventHandler>) GwtReflectionUtils.getClass(eventHandlerClassName);
        } catch (ClassNotFoundException e) {
        }

        String anotherEventHandlerClassName = eventTypeClass.getName() + ".Handler";
        try {
            return (Class<EventHandler>) GwtReflectionUtils.getClass(anotherEventHandlerClassName);
        } catch (ClassNotFoundException e) {
            // should never happen
            throw new GwtTestUiBinderException("Cannot find handler class for event type '"
                    + eventTypeClass.getName() + "'. By convention, it should be '"
                    + eventHandlerClassName + "' or '" + anotherEventHandlerClassName + "'");
        }
    }

    private GwtEvent.Type<?> getEventType(Method method) {
        Class<?> eventTypeClass = method.getParameterTypes()[0];
        try {
            return GwtReflectionUtils.callStaticMethod(eventTypeClass, "getType");
        } catch (ReflectionException e) {
            // custom event does not respect the "TYPE" private static field
            // 'standard', we have to try to instanciate it to call
            // 'getAssociatedType()' method on it
            GwtEvent<?> eventPrototype = EVENT_PROTOTYPES.get(eventTypeClass);
            if (eventPrototype == null) {
                eventPrototype = (GwtEvent<?>) GwtReflectionUtils.instantiateClass(eventTypeClass);
                EVENT_PROTOTYPES.put(eventTypeClass, eventPrototype);
            }

            return eventPrototype.getAssociatedType();
        }

    }

}
