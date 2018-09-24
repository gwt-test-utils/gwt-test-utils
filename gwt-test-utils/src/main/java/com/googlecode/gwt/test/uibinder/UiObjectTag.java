package com.googlecode.gwt.test.uibinder;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Text;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.HasWidgets.ForIsWidget;
import com.googlecode.gwt.test.exceptions.GwtTestUiBinderException;
import com.googlecode.gwt.test.exceptions.ReflectionException;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base handler for any object tag (e.g. &lt;g:Xxx /> tags, where Xxx is either {@link UIObject}
 * subclass or a {@link IsWidget} subtype. This class is expected to be extended to add custom code
 * to handle specific widget / attributes.
 *
 * @param <T> The wrapped object subtype
 * @author Gael Lazzari
 * @see UiObjectTag#instanciate(Class, Map, Object)
 * @see UiObjectTag#initializeObject(Object, Map, Object)
 * @see UiObjectTag#finalizeObject(Object)
 */
public abstract class UiObjectTag<T> implements UiTag<T> {

    private static class UiChildMethodHolder {
        int invocationCount;
        int invocationLimit;
        Method uiChildMethod;
    }

    private UiTag<?> parentTag;
    private Map<String, UiChildMethodHolder> uiChildMethodMap;
    private T wrapped;

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.uibinder.UiTag#addElement(com.google.gwt.dom.client .Element )
     */
    public final void addElement(Element element) {

        String namespaceURI = JavaScriptObjects.getString(element,
                UiElementTag.UIBINDER_XML_NAMESPACE);

        appendElement(this.wrapped, element, namespaceURI, UiBinderXmlUtils.getChildWidgets(element));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.uibinder.UiTag#addUiObject(com.google.gwt.user. client
     * .ui.UIObject)
     */
    public final void addUiObject(UIObject uiObject) {
        addUIObject(this.wrapped, uiObject);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.uibinder.UiTag#addWidget(com.google.gwt.user.client .ui
     * .IsWidget)
     */
    public final void addWidget(IsWidget isWidget) {
        addWidget(this.wrapped, isWidget);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.uibinder.UiTag#appendText(java.lang.String)
     */
    public final void appendText(String data) {
        if (!"".equals(data.trim())) {
            appendText(this.wrapped, data);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.uibinder.UiTag#endTag()
     */
    public final T endTag() {
        finalizeObject(wrapped);
        return wrapped;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.uibinder.UiTag#getParentTag()
     */
    public final UiTag<?> getParentTag() {
        return parentTag;
    }

    /**
     * Add a new UIObject which is not a {@link IsWidget} instance as a child to this uiObject. This
     * implementation does nothing, the method is expected to be overridden.
     *
     * @param wrapped  The wrapped uiObject of this tag.
     * @param uiObject The UIObject instance to add, which is not a {@link IsWidget} instance.
     */
    protected void addUIObject(T wrapped, UIObject uiObject) {

    }

    /**
     * Add a new Widget as a child to this uiObject. This implementation just calls
     * {@link ForIsWidget#add(IsWidget)} or
     * {@link HasWidgets#add(com.google.gwt.user.client.ui.Widget)} according to which interface the
     * wrapped uiObject is implementing.
     *
     * @param wrapped  The wrapped uiObject of this tag.
     * @param isWidget The child widget to be added.
     */
    protected void addWidget(T wrapped, IsWidget isWidget) {
        if (ForIsWidget.class.isInstance(wrapped)) {
            ((ForIsWidget) wrapped).add(isWidget);
        } else if (HasWidgets.class.isInstance(wrapped)) {
            ((HasWidgets) wrapped).add(isWidget.asWidget());
        }

    }

    /**
     * Append an element declared in the .ui.xml to this uiObject, which is supposed to be its
     * parent. This implementation has one of the following behavior :
     * <ul>
     * <li>If a {@link UiChild} annotated method which correspond to the element to append is found,
     * it will be called with the element's first child Widget.</li>
     * <li>Else if the wrapped {@link UIObject} implements {@link HasHTML}, the child would be
     * appended through {@link Element#appendChild(com.google.gwt.dom.client.Node)}</li>
     * <li>Otherwise, a {@link GwtTestUiBinderException} would be thrown with message: 'Found
     * unexpected child element : <x:xxxx>'</li>
     * </ul>
     *
     * @param wrapped      The wrapped uiObject of this tag.
     * @param element      The child element to be appended.
     * @param namespaceURI The namespace URI of the child element.
     * @param childWidgets The element's childs widgets, which could be empty if no child has been
     *                     added to it.
     */
    protected void appendElement(T wrapped, Element element, String namespaceURI,
                                 List<IsWidget> childWidgets) {

        UiChildMethodHolder uiChildMethodHolder = uiChildMethodMap.get(element.getTagName());

        if (uiChildMethodHolder != null) {
            invokeUiChildMethod(wrapped, childWidgets, uiChildMethodHolder);
        } else if (HasHTML.class.isInstance(wrapped)) {
            getElement(wrapped).appendChild(element);
        } else {
            String elementToString = (namespaceURI != null && namespaceURI.length() > 0)
                    ? namespaceURI + ":" + element.getTagName() : element.getTagName();
            throw new GwtTestUiBinderException("Found unexpected child element : <" + elementToString
                    + "> in " + wrapped.getClass().getName());
        }
    }

    /**
     * Append text to this uiObject. This implementation calls {@link HasText#setText(String)} if the
     * current uiObject is implementing the {@link HasText} interface, or append a new {@link Text}
     * node wrapping the data value to the Widget's element.
     *
     * @param wrapped The wrapped uiObject of this tag.
     * @param data    The string value.
     */
    protected void appendText(T wrapped, String data) {
        if (HasText.class.isInstance(wrapped)) {
            ((HasText) wrapped).setText(data);
        } else {
            Element element = getElement(wrapped);
            Text text = JsoUtils.newText(data, element.getOwnerDocument());
            element.appendChild(text);
        }
    }

    /**
     * A callback executed after every standard uiObject properties have been setup to be able to
     * process any custom finalization on the wrapped uiObject.
     *
     * @param uiObject The uiObject to finalize.
     */
    protected abstract void finalizeObject(T uiObject);

    protected Element getElement(T wrapped) {
        if (wrapped instanceof UIObject) {
            return ((UIObject) wrapped).getElement();
        } else if (wrapped instanceof IsWidget) {
            return ((IsWidget) wrapped).asWidget().getElement();
        } else {
            throw new GwtTestUiBinderException(
                    "Cannot retrieve the Element instance in instances of '"
                            + wrapped.getClass().getName() + "', you have to override "
                            + this.getClass() + ".getElement(..) protected method");
        }
    }

    /**
     * A callback method executed just after the corresponding UiBinder tag was opened to be able to
     * process any custom initialization on the wrapped uiObject.
     *
     * @param wrapped    The uiObject to initialize
     * @param attributes map of attributes of the wrapped uiObject, with attribute XML names as keys,
     *                   corresponding objects as values. This map will be used to populate the wrapped
     *                   uiObject just after this callback would be executed.
     * @param owner      The owner of the UiBinder template, with {@link UiField} fields.
     */
    protected abstract void initializeObject(T wrapped, Map<String, Object> attributes, Object owner);

    /**
     * Method responsible for the uiObject instanciation. It is called only if the uiBinder tag is
     * not a provided {@link UiField} and not annotated with either {@link UiFactory} nor
     * {@link UiConstructor}. This implementation simply check for a zero-arg constructor to call and
     * would throw an exception if it does not exist.
     *
     * @param clazz      The uiObject class to instanciate.
     * @param attributes map of attributes of the wrapped uiObject, with attribute XML names as keys,
     *                   corresponding objects as values.
     * @param owner      The owner of the UiBinder template, with {@link UiField} fields.
     * @return The created instance.
     */
    protected T instanciate(Class<? extends T> clazz, Map<String, Object> attributes, Object owner) {
        try {
            Constructor<? extends T> defaultCons = clazz.getDeclaredConstructor();
            return GwtReflectionUtils.instantiateClass(defaultCons);
        } catch (NoSuchMethodException e) {
            throw new GwtTestUiBinderException(
                    clazz.getName()
                            + " has no default (zero args) constructor. You have to register a custom "
                            + UiObjectTagFactory.class.getSimpleName()
                            + " by calling the protected method 'addUiObjectTagFactory' of your test class and override the 'instanciate(Class<T>) method in it");
        }
    }

    /**
     * Callback method called whenever a new uiBinder tag is opened, so implementation could apply
     * some custom initialization.
     *
     * @param clazz        The class of the object to be wrapped in this UiTag.
     * @param namespaceURI The namespace URI of the opened tag
     * @param attributes   map of attributes of the wrapped uiObject, with attribute XML names as keys,
     *                     corresponding objects as values.
     * @param parentTag    The parent tag
     * @param owner        The owner of the UiBinder template, with {@link UiField} fields.
     */
    final void startTag(Class<? extends T> clazz, Map<String, Object> attributes,
                        UiTag<?> parentTag, Object owner) {

        this.parentTag = parentTag;
        this.uiChildMethodMap = collectUiChildMethods(clazz);

        wrapped = UiBinderInstanciator.getInstance(clazz, attributes, owner);

        if (wrapped == null) {
            wrapped = instanciate(clazz, attributes, owner);
        }

        String uiFieldValue = (String) attributes.get("ui:field");

        if (uiFieldValue != null) {
            attributes.remove("ui:field");
            try {
                GwtReflectionUtils.setPrivateFieldValue(owner, uiFieldValue, wrapped);
            } catch (ReflectionException e) {
                // ui:field has no corresponding @UiField declared : just ignore it
            }
        }

        initializeObject(wrapped, attributes, owner);

        UiBinderBeanUtils.populateObject(wrapped, attributes);
    }

    private Map<String, UiChildMethodHolder> collectUiChildMethods(Class<? extends T> clazz) {

        Map<String, UiChildMethodHolder> map = new HashMap<>();

        Map<Method, UiChild> uiChildMap = GwtReflectionUtils.getAnnotatedMethod(clazz, UiChild.class);
        for (Map.Entry<Method, UiChild> entry : uiChildMap.entrySet()) {
            Method method = entry.getKey();
            UiChild annotation = entry.getValue();
            UiChildMethodHolder holder = new UiChildMethodHolder();
            holder.uiChildMethod = method;
            holder.invocationLimit = annotation.limit(); // default is -1
            holder.invocationCount = 0;

            String tagName = (annotation.tagname().equals("")) ? computeUiChildMethodTagName(method)
                    : annotation.tagname();

            map.put(tagName, holder);
        }
        return map;
    }

    private String computeUiChildMethodTagName(Method method) {
        if (!method.getName().startsWith("add")) {
            throw new GwtTestUiBinderException(
                    "Cannot compute tagname of @UiChild annotated method '"
                            + method.toGenericString()
                            + "': you have to fill the 'tagname' property of the @UiChild or to prefix your the method name with 'add'");
        }
        return method.getName().substring(3).toLowerCase();
    }

    private void invokeUiChildMethod(T wrapped, List<IsWidget> childWidgets,
                                     UiChildMethodHolder uiChildMethodHolder) {
        if (uiChildMethodHolder.invocationLimit > -1
                && uiChildMethodHolder.invocationCount > uiChildMethodHolder.invocationLimit) {
            throw new GwtTestUiBinderException("@UiChild method '"
                    + uiChildMethodHolder.uiChildMethod.toGenericString()
                    + "' cannot be invoked more than " + uiChildMethodHolder.invocationLimit
                    + " times");
        } else if (childWidgets.size() != 1) {
            throw new GwtTestUiBinderException("@UiChild method '"
                    + uiChildMethodHolder.uiChildMethod.toGenericString()
                    + "' can only be applied to add one Widget, but " + childWidgets.size()
                    + " have been found");
        }

        try {
            uiChildMethodHolder.uiChildMethod.invoke(wrapped, childWidgets.get(0));
        } catch (Exception e) {
            throw new GwtTestUiBinderException(
                    "An exception has been thrown during invocation of @UiChild method: "
                            + uiChildMethodHolder.uiChildMethod.toGenericString(), e);
        }
        uiChildMethodHolder.invocationCount++;
    }

}
