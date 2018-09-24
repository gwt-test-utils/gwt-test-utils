package com.googlecode.gwt.test.uibinder;

import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.gwt.test.exceptions.GwtTestUiBinderException;
import com.googlecode.gwt.test.exceptions.ReflectionException;
import com.googlecode.gwt.test.internal.resources.ResourcePrototypeProxyBuilder;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class responsible for managing declared resources, e.g. <ui:with />, <ui:style />, <ui:image />
 * and <ui:data /> tags.
 *
 * @author Gael Lazzari
 */
class UiResourceManager {

    /**
     * Handles <ui:data /> tags.
     */
    private static class UiDataTag extends UiResourceTag {

        UiDataTag(ResourcePrototypeProxyBuilder builder, String alias, UiTag<?> parentTag,
                  Object owner, Map<String, Object> resources, Map<String, Object> attributes) {
            super(builder, alias, parentTag, owner, resources);

            // handle "src" attribute
            String src = (String) attributes.get("src");
            builder.resourceURL(computeImageURL(owner, src));
        }

        @Override
        protected Object buildObject(ResourcePrototypeProxyBuilder builder) {
            return builder.build();
        }

        private URL computeImageURL(Object owner, String src) {
            URL dataURL = owner.getClass().getResource(src);

            if (dataURL == null) {
                throw new GwtTestUiBinderException("Cannot find binary file with src=\"" + src
                        + "\" declared in " + owner.getClass().getSimpleName() + ".ui.xml");
            }

            return dataURL;
        }
    }

    /**
     * Handles <ui:image /> tags.
     */
    private static class UiImgTag extends UiResourceTag {

        UiImgTag(ResourcePrototypeProxyBuilder builder, String alias, UiTag<?> parentTag,
                 Object owner, Map<String, Object> resources, Map<String, Object> attributes) {
            super(builder, alias, parentTag, owner, resources);

            // handle "src" attribute
            String src = (String) attributes.get("src");
            builder.resourceURL(computeImageURLs(owner, src));
        }

        @Override
        protected Object buildObject(ResourcePrototypeProxyBuilder builder) {
            return builder.build();
        }

        private URL computeImageURLs(Object owner, String src) {
            URL imageURL = owner.getClass().getResource(src);

            if (imageURL == null) {
                throw new GwtTestUiBinderException("Cannot find image file with src=\"" + src
                        + "\" declared in " + owner.getClass().getSimpleName() + ".ui.xml");
            }

            return imageURL;
        }

    }

    /**
     * Handles <ui:import> tags.
     */
    private static class UiImportTag implements UiTag<Object> {

        private final UiTag<?> parentTag;

        UiImportTag(Map<String, Object> attributes, UiTag<?> parentTag,
                    Map<String, Object> resources, Object owner) {
            this.parentTag = parentTag;
            // collects single and multiple imports in UiBinderResourceManager
            // inner
            // map
            collectObjectToImport(attributes, resources, owner);
        }

        public void addElement(Element element) {
            // nothing to do
        }

        public void addUiObject(UIObject uiObject) {
            // nothing to do
        }

        public void addWidget(IsWidget widget) {
            // nothing to do
        }

        public void appendText(String text) {
            // nothing to do
        }

        public Object endTag() {
            // the result will not be used by UiBinderTagBuilder
            return null;
        }

        public UiTag<?> getParentTag() {
            return parentTag;
        }

        private void collectMultipleImports(String importValue, Map<String, Object> resources,
                                            Object owner) {

            try {
                String className = importValue.substring(0, importValue.lastIndexOf('.'));

                Class<?> clazz = GwtReflectionUtils.getClass(className);

                // this code handles classes and enums fine
                for (Field field : GwtReflectionUtils.getFields(clazz)) {
                    if (Modifier.isStatic(field.getModifiers())
                            && !Modifier.isPrivate(field.getModifiers())
                            && !Modifier.isProtected(field.getModifiers())) {
                        // register static field value in UiResourcesManager inner map
                        Object value = GwtReflectionUtils.getStaticFieldValue(clazz, field.getName());
                        resources.put(field.getName(), value);
                    }
                }

            } catch (Exception e) {
                throw new GwtTestUiBinderException("Error while trying to import multiple ui fields '"
                        + importValue + "'", e);
            }
        }

        private Map<String, Object> collectObjectToImport(Map<String, Object> attributes,
                                                          Map<String, Object> resources, Object owner) {
            Map<String, Object> result = new HashMap<String, Object>();

            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                String attrName = entry.getKey();
                String attrValue = entry.getValue().toString().trim();

                // ignore attributes other than <ui:field>
                if (!"ui:field".equals(attrName)) {
                    continue;
                }

                // ignore empty attributes
                if (attrValue.length() == 0) {
                    continue;
                }

                int token = attrValue.lastIndexOf('.');
                if (token > -1 && token < attrValue.length() - 1
                        && attrValue.substring(token).equals(".*")) {
                    // case of multiple import
                    collectMultipleImports(attrValue, resources, owner);

                } else {
                    // case of single import
                    collectSingleImport(attrValue, resources, owner);
                }
            }

            return result;
        }

        private void collectSingleImport(String importValue, Map<String, Object> resources,
                                         Object owner) {
            try {
                int token = importValue.lastIndexOf('.');
                String className = importValue.substring(0, token);
                Class<?> clazz = GwtReflectionUtils.getClass(className);
                String fieldName = importValue.substring(token + 1);

                Object objectToImport = GwtReflectionUtils.getStaticFieldValue(clazz, fieldName);

                // register static field value in UiResourcesManager inner map
                resources.put(fieldName, objectToImport);

            } catch (Exception e) {
                throw new GwtTestUiBinderException("Error while trying to import ui field '"
                        + importValue + "'", e);
            }
        }
    }

    /**
     * Handles <ui:msg /> tags.
     *
     * @author Gael Lazzari
     */
    private static class UiMsgTag implements UiTag<String> {

        private final UiTag<?> parentTag;
        private final StringBuilder sb;

        UiMsgTag(UiTag<?> parent) {
            this.parentTag = parent;
            sb = new StringBuilder();
        }

        public void addElement(Element element) {
            sb.append("<").append(element.getTagName());
            // TODO : append attributes
            sb.append(">").append(element.getInnerText()).append("</").append(element.getTagName()).append(
                    ">");
        }

        public void addUiObject(UIObject uiObject) {
            parentTag.addUiObject(uiObject);
        }

        public void addWidget(IsWidget isWidget) {
            parentTag.addWidget(isWidget);

        }

        public void appendText(String data) {
            sb.append(data);
        }

        public String endTag() {
            return sb.toString();
        }

        public UiTag<?> getParentTag() {
            return parentTag;
        }
    }

    /**
     * Base class for resource tags : <ui:style />, <ui:image /> and <ui:data />
     */
    private static abstract class UiResourceTag implements UiTag<Object> {

        private final String alias;
        private final ResourcePrototypeProxyBuilder builder;
        private final Object owner;
        private final UiTag<?> parentTag;
        private final Map<String, Object> resources;
        private Object wrappedObject;

        UiResourceTag(ResourcePrototypeProxyBuilder builder, String alias, UiTag<?> parentTag,
                      Object owner, Map<String, Object> resources) {
            this.builder = builder;
            this.owner = owner;
            this.parentTag = parentTag;
            this.alias = alias;
            this.resources = resources;
        }

        public void addElement(Element element) {
            // adapter method
        }

        public void addUiObject(UIObject uiObject) {
            // adapter method
        }

        public void addWidget(IsWidget widget) {
            // adapter method
        }

        public void appendText(String text) {
            // adapter method
        }

        public Object endTag() {
            if (wrappedObject == null) {
                // delegate the creation to subclasses
                wrappedObject = buildObject(builder);

                // set the corresponding @UiField
                Field resourceUiField = getUniqueUiField(alias);
                if (resourceUiField != null) {
                    try {
                        resourceUiField.set(owner, wrappedObject);
                    } catch (Exception e) {
                        throw new ReflectionException(e);
                    }
                }

                // register the built resource to the resourceManager inner map
                resources.put(alias, wrappedObject);

            }

            return wrappedObject;
        }

        public UiTag<?> getParentTag() {
            return parentTag;
        }

        protected abstract Object buildObject(ResourcePrototypeProxyBuilder builder);

        private Field getUniqueUiField(String alias) {
            Set<Field> resourceFields = GwtReflectionUtils.getFields(owner.getClass());
            if (resourceFields.size() == 0) {
                return null;
            }

            Field result = null;

            for (Field f : resourceFields) {
                if (alias.equals(f.getName()) && f.isAnnotationPresent(UiField.class)) {
                    if (result != null) {
                        throw new GwtTestUiBinderException("There are more than one '" + f.getName()
                                + "' @UiField in class '" + owner.getClass().getName()
                                + "' or its superclass");
                    }

                    result = f;
                }
            }

            return result;
        }

    }

    /**
     * Handles <ui:style /> tags with a "type" attribute to specify a {@link CssResource} subtype.
     */
    private static class UiStyleTag extends UiResourceTag {

        private final StringBuilder text;

        UiStyleTag(ResourcePrototypeProxyBuilder builder, String alias, UiTag<?> parentTag,
                   Object owner, Map<String, Object> resources) {
            super(builder, alias, parentTag, owner, resources);
            this.text = new StringBuilder();
        }

        @Override
        public void appendText(String text) {
            this.text.append(text.trim());
        }

        @Override
        protected Object buildObject(ResourcePrototypeProxyBuilder builder) {
            return builder.text(text.toString()).build();
        }

    }

    /**
     * Handles <ui:text /> tags
     */
    private static class UiTextTag implements UiTag<String> {

        private final UiTag<?> parentTag;
        private final String text;

        UiTextTag(Map<String, Object> attributes, UiTag<?> parentTag, Object owner) {
            this.parentTag = parentTag;

            Object value = attributes.get("from");
            if (value instanceof TextResource) {
                text = ((TextResource) value).getText();
            } else if (value instanceof String) {
                text = (String) value;
            } else {
                throw new GwtTestUiBinderException("Error in " + owner.getClass().getSimpleName()
                        + ".ui.xml : <ui:text> tag declared without 'from' attribute");
            }
        }

        public void addElement(Element element) {
            // adapter method
        }

        public void addUiObject(UIObject uiObject) {
            // adapter method
        }

        public void addWidget(IsWidget widget) {
            // adapter method
        }

        public void appendText(String text) {
            // adapter method
        }

        public String endTag() {
            return text;
        }

        public UiTag<?> getParentTag() {
            return parentTag;
        }

    }

    /**
     * Handles <ui:with /> tags.
     */
    private static class UiWithTag implements UiTag<Object> {

        private final Object with;

        public UiWithTag(Object with) {
            this.with = with;
        }

        public void addElement(Element element) {
            // nothing to do
        }

        public void addUiObject(UIObject uiObject) {
            // nothing to do
        }

        public void addWidget(IsWidget isWidget) {
            // nothing to do
        }

        public void appendText(String text) {
            // nothing to do
        }

        public Object endTag() {
            return with;
        }

        public UiTag<?> getParentTag() {
            // nothing to do
            return null;
        }

    }

    /**
     * Constructs a new UiResourceManager associated with a widget.
     *
     * @param owner The {@link UiBinder} owner widget to be associated with the UiResourceManager,
     *              which calls the {@link UiBinder#createAndBindUi(Object)} method to initialize itself
     * @return The new instance
     */
    static final UiResourceManager newInstance(Object owner) {
        return new UiResourceManager(owner);
    }

    private final Object owner;
    private final Map<String, Object> resources = new HashMap<>();

    private UiResourceManager(Object owner) {
        this.owner = owner;
    }

    /**
     * Get a resource declared in the .ui.xml file with the <with res="alias" /> tag.
     *
     * @param alias The alias of the resource ('res' attribute in the .ui.xml file)
     * @return The corresponding resource, or null if it does not exist
     */
    @SuppressWarnings("unchecked")
    <T> T getUiResource(String alias) {
        return (T) resources.get(alias);
    }

    /**
     * Register some new resources which should correspond to a <ui:import> tag in the .ui.xml file.
     *
     * @param attributes Map of attributes parsed from the tag
     * @param parentTag  The parent tag if any
     * @param owner      The {@link UiBinder} owner widget, which calls the
     *                   {@link UiBinder#createAndBindUi(Object)} method to initialize itself.
     * @return The UiBinderTag which has registered the imported object instances in the
     * {@link UiResourceManager}.
     */
    UiTag<?> registerImport(Map<String, Object> attributes, UiTag<?> parentTag, Object owner) {
        return new UiImportTag(attributes, parentTag, resources, owner);
    }

    /**
     * Register a <ui:msg> tag declared in the .ui.xml file.
     *
     * @param attributes Map of attributes parsed from the tag
     * @param parentTag  The parent tag if any
     * @param owner      The {@link UiBinder} owner widget, which calls the
     *                   {@link UiBinder#createAndBindUi(Object)} method to initialize itself.
     * @return The UiBinderTag which has registered the imported object instances in the
     * {@link UiResourceManager}.
     */
    UiTag<String> registerMsg(Map<String, Object> attributes, UiTag<?> parentTag, Object owner) {
        return new UiMsgTag(parentTag);
    }

    /**
     * Register a new resource which should correspond to a resource tag in the .ui.xml file.
     *
     * @param localName  The type of the resource ('with', 'style', 'image' or 'data')
     * @param attributes Map of attributes parsed from the tag
     * @param parentTag  The parent tag if any
     * @param owner      The {@link UiBinder} owner widget, which calls the
     *                   {@link UiBinder#createAndBindUi(Object)} method to initialize itself.
     * @return The UiBinderTag which wraps the Resource instance.
     * @throws GwtTestUiBinderException If the localName is not managed or if the alias is already
     *                                  binded to another Resource object
     */
    UiTag<?> registerResource(String localName, Map<String, Object> attributes, UiTag<?> parentTag,
                              Object owner) throws GwtTestUiBinderException {

        String alias = getResourceAlias(localName, attributes);

        if (resources.containsKey(alias)) {
            throw new GwtTestUiBinderException("Two inner resources '" + alias + " are declared in "
                    + owner.getClass().getSimpleName()
                    + ".ui.xml. You should add a 'field' attribute to one of them");
        }

        Class<?> type = getResourceType(alias, localName, attributes);

        if ("with".equals(localName)) {
            // special resource <ui:with> : the resource can be annotated with
            // @UiConstructor, @UiFactory or @UiField(provided=true)
            Object resource = UiBinderInstanciator.getInstance(type, attributes, owner);
            resources.put(alias, resource);
            return new UiWithTag(resource);

        }

        ResourcePrototypeProxyBuilder builder = ResourcePrototypeProxyBuilder.createBuilder(type,
                owner.getClass());
        // common properties
        builder.name(alias);

        if ("style".equals(localName)) {
            // <ui:style>
            return new UiStyleTag(builder, alias, parentTag, owner, resources);

        } else if ("image".equals(localName)) {
            // <ui:image>
            return new UiImgTag(builder, alias, parentTag, owner, resources, attributes);

        } else if ("data".equals(localName)) {
            // <ui:data>
            return new UiDataTag(builder, alias, parentTag, owner, resources, attributes);
        } else {
            throw new GwtTestUiBinderException("resource <" + localName
                    + "> element is not yet implemented by gwt-test-utils");
        }
    }

    /**
     * Register a <ui:text> tag declared in the .ui.xml file.
     *
     * @param attributes Map of attributes parsed from the tag
     * @param parentTag  The parent tag if any
     * @param owner      The {@link UiBinder} owner widget, which calls the
     *                   {@link UiBinder#createAndBindUi(Object)} method to initialize itself.
     * @return The UiBinderTag which has registered the imported object instances in the
     * {@link UiResourceManager}.
     */
    UiTag<String> registerText(Map<String, Object> attributes, UiTag<?> parentTag, Object owner) {
        return new UiTextTag(attributes, parentTag, owner);
    }

    private String getResourceAlias(String localName, Map<String, Object> attributes) {
        String alias;
        alias = (String) attributes.get("ui:field");
        if (alias == null && !"with".equals(localName)) {
            alias = localName;
        }
        if (alias == null) {
            throw new GwtTestUiBinderException("Cannot find the required 'field' value for tag <"
                    + localName + "> in " + owner.getClass().getSimpleName() + ".ui.xml");
        }

        return alias;
    }

    private Class<?> getResourceType(String alias, String localName, Map<String, Object> attributes) {
        String type = (String) attributes.get("type");

        if (type == null && "image".equals(localName)) {
            // special code for <ui:image> with no 'type' attribute
            return ImageResource.class;
        } else if (type == null && "style".equals(localName)) {
            // special code for <ui:style> with no 'type' attribute
            return CssResource.class;
        } else if (type == null && "data".equals(localName)) {
            // special code for <ui:data> with no 'type' attribute
            return DataResource.class;
        } else if (type == null && "text".equals(localName)) {
            // sepcial code for <ui:text> with no 'type' attribute
            return TextResource.class;
        } else if (type == null) {
            throw new GwtTestUiBinderException("<" + localName + "> element declared in "
                    + owner.getClass().getSimpleName() + ".ui.xml must specify a 'type' attribute");
        }

        try {
            return GwtReflectionUtils.getClass(type);
        } catch (ClassNotFoundException e2) {
            throw new GwtTestUiBinderException("Cannot find class '" + type + "' for resource '"
                    + alias + "' declared in file '" + owner.getClass().getSimpleName() + ".ui.xml'");
        }
    }
}
