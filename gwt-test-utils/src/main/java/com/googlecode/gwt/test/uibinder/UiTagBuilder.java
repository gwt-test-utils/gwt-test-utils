package com.googlecode.gwt.test.uibinder;

import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.exceptions.GwtTestUiBinderException;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import org.xml.sax.Attributes;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
class UiTagBuilder<T> {

    private static final Pattern EXTERNAL_RESOURCES_PATTERN = Pattern.compile("\\s*\\{(\\S*)\\}\\s*");

    static final <T> UiTagBuilder<T> create(Class<T> rootComponentClass, Object owner) {
        return new UiTagBuilder<T>(rootComponentClass, owner);
    }

    private UiTag<?> currentTag;
    private final Object owner;
    private final UiResourceManager resourceManager;

    private Object rootComponent;

    private final Class<T> rootComponentClass;

    private UiTagBuilder(Class<T> rootComponentClass, Object owner) {
        this.rootComponentClass = rootComponentClass;
        this.owner = owner;
        this.resourceManager = UiResourceManager.newInstance(owner);
    }

    UiTagBuilder<T> appendText(String text) {
        if (!"".equals(text.trim())) {
            currentTag.appendText(text);
        }
        return this;
    }

    T build() {
        if (rootComponent == null) {
            if (currentTag == null) {
                throw new GwtTestUiBinderException(owner.getClass().getName()
                        + " does not declare a root widget in its corresponding .ui.xml file");
            } else {
                throw new GwtTestUiBinderException(
                        "Cannot build UiBinder component while the parsing of '"
                                + owner.getClass().getSimpleName() + ".ui.xml' is not finished");
            }

        } else if (!rootComponentClass.isInstance(rootComponent)) {
            throw new GwtTestUiBinderException("Error in '" + owner.getClass().getSimpleName()
                    + ".ui.xml' configuration : root component is expected to be an instance of '"
                    + rootComponentClass.getName() + "' but is actually an instance of '"
                    + rootComponent.getClass().getName() + "'");
        }

        return (T) rootComponent;
    }

    UiTagBuilder<T> endTag(String nameSpaceURI, String localName) {

        // ignore <UiBinder> tag
        if (UiBinderXmlUtils.isUiBinderTag(nameSpaceURI, localName)) {
            return this;
        }

        Object currentObject = currentTag.endTag();
        UiTag<?> parentTag = currentTag.getParentTag();
        currentTag = parentTag;

        if (UiBinderXmlUtils.isResourceTag(nameSpaceURI, localName)
                || UiBinderXmlUtils.isImportTag(nameSpaceURI, localName)) {
            // ignore <ui:data>, <ui:image>, <ui:style> <ui:text> and <ui:import> tags
            return this;
        } else if (UiBinderXmlUtils.isMsgTag(nameSpaceURI, localName)
                || UiBinderXmlUtils.isTextTag(nameSpaceURI, localName)) {
            // special <ui:msg> and <ui:text> case
            parentTag.appendText((String) currentObject);
            return this;
        }

        if (parentTag == null) {
            // parsing is finished, this must be the root component
            if (rootComponent != null) {
                throw new GwtTestUiBinderException("UiBinder template '" + owner.getClass().getName()
                        + "' should declare only one root widget in its corresponding .ui.xml file");
            } else {
                rootComponent = currentObject;
            }
        } else {
            // add to its parent
            if (IsWidget.class.isInstance(currentObject)) {
                parentTag.addWidget((IsWidget) currentObject);
            } else if (UIObject.class.isInstance(currentObject)) {
                // UIObject instance that is not a Widget
                parentTag.addUiObject((UIObject) currentObject);
            } else {
                parentTag.addElement((Element) currentObject);
            }
        }

        return this;
    }

    UiTagBuilder<T> startTag(String nameSpaceURI, String localName, Attributes attributes) {

        if (UiBinderXmlUtils.isUiBinderTag(nameSpaceURI, localName)) {
            return this;
        }

        // register the current UiBinderTag in the stack
        currentTag = createUiTag(nameSpaceURI, localName, attributes, currentTag);

        return this;
    }

    private UiTag<?> createUiTag(String nameSpaceURI, String localName, Attributes attributesXML,
                                 UiTag<?> parentTag) {

        Map<String, Object> attributes = parseAttributesMap(nameSpaceURI, attributesXML);

        localName = localName.replaceAll("\\.", "\\$");

        int i = nameSpaceURI.lastIndexOf(':');
        if (i > 0 && Character.isUpperCase(localName.charAt(0))) {
            // the element should represent a Widget Class
            String className = nameSpaceURI.substring(i + 1) + "." + localName;

            Class<?> clazz = null;
            try {
                clazz = GwtReflectionUtils.getClass(className);
            } catch (ClassNotFoundException e) {
                throw new GwtTestUiBinderException("Cannot find class '" + className
                        + "' declared in file '" + owner.getClass().getSimpleName() + ".ui.xml");
            }

            if (UIObject.class.isAssignableFrom(clazz) || IsWidget.class.isAssignableFrom(clazz)) {

                UiObjectTag<Object> uibinderTag = DefaultUiWidgetTagFactory.get().createUiObjectTag(
                        clazz, attributes);

                uibinderTag.startTag(clazz, attributes, parentTag, owner);

                return uibinderTag;
            } else {
                throw new GwtTestUiBinderException("Not managed UiBinder type '" + clazz
                        + "' declared in file '" + owner.getClass().getSimpleName() + ".ui.xml"
                        + "', only implementation of '" + IsWidget.class.getName()
                        + "' or subclass of '" + UIObject.class.getName() + "' are allowed");
            }
        } else if (UiBinderXmlUtils.isResourceTag(nameSpaceURI, localName)) {
            return resourceManager.registerResource(localName, attributes, parentTag, owner);
        } else if (UiBinderXmlUtils.isImportTag(nameSpaceURI, localName)) {
            return resourceManager.registerImport(attributes, parentTag, owner);
        } else if (UiBinderXmlUtils.isMsgTag(nameSpaceURI, localName)) {
            return resourceManager.registerMsg(attributes, parentTag, attributes);
        } else if (UiBinderXmlUtils.isTextTag(nameSpaceURI, localName)) {
            return resourceManager.registerText(attributes, parentTag, attributes);
        } else {
            return new UiElementTag(nameSpaceURI, localName, attributes, parentTag, owner);
        }
    }

    private Object extractResource(String group, UiResourceManager resourceManager, Object owner) {
        String[] splitted = group.split("\\.");
        String resourceAlias = splitted[0];
        Object resource = resourceManager.getUiResource(resourceAlias);
        if (resource == null) {
            throw new GwtTestUiBinderException("Error in file '" + owner.getClass().getSimpleName()
                    + ".ui.xml' : no resource declared for alias '" + resourceAlias + "'");
        }

        if (splitted.length == 1) {
            return resource;
        }

        // handle "alias.myDataResource.getUrl"
        try {
            for (int i = 1; i < splitted.length; i++) {
                if (CssResource.class.isInstance(resource)) {
                    // special case of css styles
                    return splitted[i];
                } else {
                    resource = GwtReflectionUtils.callPrivateMethod(resource, splitted[i]);
                }
            }

            return resource;

        } catch (Exception e) {
            if (GwtTestException.class.isInstance(e)) {
                throw (GwtTestException) e;
            } else {
                throw new GwtTestUiBinderException("Error while calling property '"
                        + group.substring(group.indexOf('.') + 1) + "' on object of type '"
                        + resourceManager.getUiResource(resourceAlias).getClass().getName() + "'", e);
            }
        }
    }

    private String formatResourcesMessage(String attrValue, List<Object> resources) {

        StringBuilder sb = new StringBuilder();
        Matcher m = EXTERNAL_RESOURCES_PATTERN.matcher(attrValue);

        int foundNb = 0;
        int token = 0;
        while (m.find()) {
            sb.append(attrValue.substring(token, m.start(1))).append(foundNb++);
            token = m.end(1);
        }
        sb.append(attrValue.substring(token));

        return MessageFormat.format(sb.toString(), resources.toArray());
    }

    private Map<String, Object> parseAttributesMap(String elementNsURI, Attributes attributesXML) {

        Map<String, Object> attributesMap = new HashMap<>();

        for (int i = 0; i < attributesXML.getLength(); i++) {
            String attrName = attributesXML.getLocalName(i);
            String attrValue = attributesXML.getValue(i).trim();
            String attrUri = "".equals(attributesXML.getURI(i)) ? elementNsURI
                    : attributesXML.getURI(i);

            if (UiBinderXmlUtils.isUiFieldAttribute(attrUri, attrName)) {
                attributesMap.put("ui:field", attrValue);
            } else if ("styleName".equals(attrName)) {
                // special case of style
                attributesMap.put("styleName", UiBinderXmlUtils.getEffectiveStyleName(attrValue));

            } else if ("addStyleNames".equals(attrName)) {
                // special case of multiple style to add
                treatAddStyleNamesAttr(attrValue, attributesMap);

            } else {
                // normal attribute
                treatStandardAttr(owner, resourceManager, attrName, attrValue, attributesMap);

            }
        }

        return attributesMap;
    }

    private void treatAddStyleNamesAttr(String attrValue, Map<String, Object> attributesMap) {
        String[] styles = attrValue.trim().split("\\s+");
        String[] effectiveStyles = new String[styles.length];

        for (int j = 0; j < styles.length; j++) {
            effectiveStyles[j] = UiBinderXmlUtils.getEffectiveStyleName(styles[j]);
        }
        attributesMap.put("addStyleNames", effectiveStyles);
    }

    private void treatStandardAttr(Object owner, UiResourceManager resourceManager, String attrName,
                                   String attrValue, Map<String, Object> attributesMap) {

        Matcher m = EXTERNAL_RESOURCES_PATTERN.matcher(attrValue);
        if (m.matches()) {
            // entire value matches : only one {} resource, not only string
            attributesMap.put(attrName, extractResource(m.group(1), resourceManager, owner));
        } else {
            // we have to find potential {} resources
            m = EXTERNAL_RESOURCES_PATTERN.matcher(attrValue);

            List<Object> resources = new ArrayList<>();
            while (m.find()) {
                resources.add(extractResource(m.group(1), resourceManager, owner));
            }

            if (resources.size() == 0) {
                // attribute should be a String or a simple object convertible to a
                // String
                attributesMap.put(attrName, attrValue);
            } else {
                // case '{OBJ1} {Obj2}' : this must be formatted to string
                attributesMap.put(attrName, formatResourcesMessage(attrValue, resources));
            }
        }
    }

}
