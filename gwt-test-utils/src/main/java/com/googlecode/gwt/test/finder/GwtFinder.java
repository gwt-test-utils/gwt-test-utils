package com.googlecode.gwt.test.finder;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.*;
import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import com.googlecode.gwt.test.utils.WidgetUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Helper class which expose some static methods to retrieve Widgets (or widget properties) that are
 * attached to the {@link RootPanel} or {@link RootLayoutPanel}.
 *
 * @author Gael Lazzari
 */
public class GwtFinder implements AfterTestCallback {

    /**
     * The default ObjectFinder, which is responsible for delegating find request to registered
     * {@link NodeObjectFinder} instances.
     *
     * @author Gael Lazzari
     */
    private class DefaultObjectFinder implements ObjectFinder {

        private final Map<String, NodeObjectFinder> nodeObjectFinders;

        DefaultObjectFinder() {
            nodeObjectFinders = new HashMap<>();
        }

        @Override
        public boolean accept(String... params) {
            return params.length == 1 && params[0].matches("^/\\w+/.*$");
        }

        @Override
        public Object find(String... params) {
            Node node = Node.parse(params[0]);
            if (node == null) {
                throw new GwtFinderException("Cannot parse search string to a valid Node : "
                        + params[0]);
            }

            String prefix = node.getLabel();

            NodeObjectFinder finder = nodeObjectFinders.get(prefix);

            if (finder == null) {
                throw new GwtFinderException("Unknown node prefix '" + prefix
                        + "'. You must register your own " + NodeObjectFinder.class.getSimpleName()
                        + " instance for this prefix through the " + GwtFinder.class.getSimpleName()
                        + ".registerNodeFinder(..) method");
            }

            return finder.find(node.getNext());
        }

    }

    /**
     * An ObjectFinder implementation which find objects in a map of instanciated widget, indexed
     * with theirs id. Widgets which implements {@link HasText}, {@link HasHTML} and/or
     * {@link HasName} are also indexed with theirs text, html and/or name.
     *
     * @author Gael Lazzari
     */
    private static class IndexedObjectFinder implements ObjectFinder {

        private final Map<String, Set<Object>> mapByHTML;
        private final Map<String, Object> mapById;
        private final Map<String, Set<Object>> mapByName;
        private final Map<String, Set<Object>> mapByText;

        IndexedObjectFinder() {
            this.mapByHTML = new HashMap<>();
            this.mapById = new HashMap<>();
            this.mapByName = new HashMap<>();
            this.mapByText = new HashMap<>();
        }

        @Override
        public boolean accept(String... params) {
            return params.length == 1 && !params[0].trim().startsWith("/");
        }

        @Override
        public Object find(String... params) {

            String alias = params[0];
            Object result = findByAlias(alias);

            if (result != null) {
                return result;
            }

            int flag = alias.indexOf("/");
            if (flag == -1) {
                return null;
            }

            String introspectionPath = null;
            introspectionPath = alias.substring(flag);
            alias = alias.substring(0, flag);

            result = findByAlias(alias);

            if (result == null) {
                return null;
            }

            return GwtFinder.find(result, Node.parse(introspectionPath));
        }

        private Object findByAlias(String alias) {
            Object result = mapById.get(alias);
            if (result != null) {
                return result;
            }

            Set<Object> byHTML = mapByHTML.get(alias);
            if (byHTML != null && byHTML.size() > 0) {
                if (byHTML.size() == 1) {
                    return byHTML.iterator().next();
                } else {
                    throw new GwtFinderException("There are " + byHTML.size()
                            + " Widgets matching HTML filter [" + alias
                            + "]. You should use an unique identifier instead");

                }
            }

            Set<Object> byText = mapByText.get(alias);
            if (byText != null && byText.size() > 0) {
                if (byText.size() == 1) {
                    return byText.iterator().next();
                } else {
                    throw new GwtFinderException("There are " + byText.size()
                            + " Widgets matching text filter [" + alias
                            + "]. You should use an unique identifier instead");

                }
            }

            Set<Object> byName = mapByText.get(alias);
            if (byName != null && byName.size() > 0) {
                if (byName.size() == 1) {
                    return byName.iterator().next();
                } else {
                    throw new GwtFinderException("There are " + byName.size()
                            + " Widgets matching name filter [" + alias
                            + "]. You should use an unique identifier instead");

                }
            }

            return null;
        }

    }

    private static GwtFinder INSTANCE = new GwtFinder();

    /**
     * Find an attached widget or a property of an attached widget in the DOM which matchs the given
     * introspection Node.
     *
     * @param o    The root object to apply the introspection path
     * @param node The node introspection path
     * @return The corresponding widget (or one of its properties), or null if nothing was found.
     */
    @SuppressWarnings("unchecked")
    public static <T> T find(Object o, Node node) {
        return (T) INSTANCE.findInternal(o, node);
    }

    /**
     * Create a new instance of <code>{@link GwtInstance}</code> which wraps an
     * <strong>attached</strong> Widget or a property of an <strong>attached</strong> widget matching
     * the given identifier. The wrapped value would be null if nothing matched.
     * <p>
     * <p>
     * Note there is one exception for {@link PopupPanel} instances, which can be found even if not
     * currently attached to the DOM.
     * </p>
     *
     * @param identifier An array of identifier parameters, which could be either an introspection
     *                   path, a DOM id, a random text (for {@link HasText} widgets), a random html (for
     *                   {@link HasHTML} widget) or a name attribute (for {@link HasName} widget).
     * @return the created <code>GwtInstance</code> to make assertion on.
     */
    public static GwtInstance object(String... identifier) {
        return new GwtInstance(INSTANCE.findInternal(identifier), identifier);
    }

    /**
     * Register a custom {@link ObjectFinder} implementation to handle
     * {@link GwtFinder#find(Object, Node)} calls with specific parameters.
     *
     * @param finder The custom finder implementation
     */
    public static void registerFinder(ObjectFinder finder) {
        INSTANCE.customObjectFinders.add(finder);
    }

    /**
     * Register a custom {@link NodeObjectFinder} implementation to handle
     * {@link GwtFinder#find(Object, Node)} calls with a specific root introspection path.
     *
     * @param prefix           The root of the introspection path the custom {@link NodeObjectFinder} is
     *                         responsible for
     * @param nodeObjectFinder The custom implementation which would be used whenever the root label
     *                         of the introspection node does equals the registered prefix.
     */
    public static void registerNodeFinder(String prefix, NodeObjectFinder nodeObjectFinder) {
        INSTANCE.defaultObjectFinder.nodeObjectFinders.put(prefix, nodeObjectFinder);
    }

    /*
     * Callback method which is made public by GwtFinderPatcher to be called by others internal
     * patchers
     */
    protected static void onAttach(Widget widget) {

        if (Composite.class.isInstance(widget.getParent())
                && getCompositeWidget(widget.getParent()) == widget) {
            onAttach(widget.getParent());

            return;
        }

        if (HasHTML.class.isInstance(widget)) {
            onSetIndex(widget, ((HasHTML) widget).getHTML(), INSTANCE.indexedObjectFinder.mapByHTML);
        }

        if (HasText.class.isInstance(widget)) {
            onSetIndex(widget, ((HasText) widget).getText(), INSTANCE.indexedObjectFinder.mapByText);
        }

        if (HasName.class.isInstance(widget)) {
            onSetIndex(widget, ((HasName) widget).getName(), INSTANCE.indexedObjectFinder.mapByName);
        }

        if (widget.getElement() != null) {
            storeId(widget, widget.getElement());
        }
    }

    /*
     * Callback method which is made public by GwtFinderPatcher to be called by others internal
     * patchers
     */
    protected static void onDetach(Widget widget) {

        if (Composite.class.isInstance(widget.getParent())
                && getCompositeWidget(widget.getParent()) == widget) {
            onDetach(widget.getParent());

            return;
        }

        if (HasHTML.class.isInstance(widget)) {
            Set<Object> byHTML = INSTANCE.indexedObjectFinder.mapByHTML.get(((HasHTML) widget).getHTML());
            if (byHTML != null) {
                byHTML.remove(widget);
            }
        }

        if (HasText.class.isInstance(widget)) {
            Set<Object> byText = INSTANCE.indexedObjectFinder.mapByText.get(((HasText) widget).getText());
            if (byText != null) {
                byText.remove(widget);
            }
        }

        if (HasName.class.isInstance(widget)) {
            Set<Object> byName = INSTANCE.indexedObjectFinder.mapByName.get(((HasName) widget).getName());
            if (byName != null) {
                byName.remove(widget);
            }
        }

        if (widget.getElement() != null) {
            removeId(widget, widget.getElement());
        }
    }

    /*
     * Callback method which is made public by GwtFinderPatcher to be called by others internal
     * patchers
     */
    protected static void onSetHTML(Object hasHTML, String newHTML, String oldHTML) {
        if (!(hasHTML instanceof Widget) || ((Widget) hasHTML).isAttached()
                || (hasHTML instanceof PopupPanel)) {
            onSetIndex(hasHTML, newHTML, oldHTML, INSTANCE.indexedObjectFinder.mapByHTML);
        }
    }

    /*
     * Callback method which is made public by GwtFinderPatcher to be called by others internal
     * patchers
     */
    protected static void onSetId(Object o, String newId, String oldId) {
        if (!(o instanceof Widget) || ((Widget) o).isAttached() || (o instanceof PopupPanel)) {
            INSTANCE.indexedObjectFinder.mapById.remove(oldId);
            INSTANCE.indexedObjectFinder.mapById.put(newId, o);
        }
    }

    /*
     * Callback method which is made public by GwtFinderPatcher to be called by others internal
     * patchers
     */
    protected static void onSetName(Object hasName, String newName, String oldName) {
        if (!(hasName instanceof Widget) || ((Widget) hasName).isAttached()
                || (hasName instanceof PopupPanel)) {
            onSetIndex(hasName, newName, oldName, INSTANCE.indexedObjectFinder.mapByName);
        }
    }

    /*
     * Callback method which is made public by GwtFinderPatcher to be called by others internal
     * patchers
     */
    protected static void onSetText(Object hasText, String newText, String oldText) {
        if (!(hasText instanceof Widget) || ((Widget) hasText).isAttached()
                || (hasText instanceof PopupPanel)) {
            onSetIndex(hasText, newText, oldText, INSTANCE.indexedObjectFinder.mapByText);
        }
    }

    private static void storeId(Widget widget, Element element) {
        String id = element.getId();
        if ((widget.getElement() == element || WidgetUtils.getWidget(element) == widget) && id != null && !id.isEmpty()) {
            INSTANCE.indexedObjectFinder.mapById.put(id, widget);
            for (int i = 0; i < element.getChildCount(); i++) {
                if (element.getChild(i) instanceof Element) {
                    storeId(widget, (Element) element.getChild(i));
                }
            }
        }
    }

    private static void removeId(Widget widget, Element element) {
        String id = widget.getElement().getId();
        if ((widget.getElement() == element || WidgetUtils.getWidget(element) == widget) && id != null && !id.isEmpty()) {
            INSTANCE.indexedObjectFinder.mapById.remove(id);
            for (int i = 0; i < element.getChildCount(); i++) {
                if (element.getChild(i) instanceof Element) {
                    removeId(widget, (Element) element.getChild(i));
                }
            }
        }
    }

    private static Widget getCompositeWidget(Widget composite) {
        return GwtReflectionUtils.callPrivateMethod(composite, "getWidget");
    }

    private static void onSetIndex(Object object, String index, Map<String, Set<Object>> cache) {

        Set<Object> newSet = cache.get(index);
        if (newSet == null) {
            newSet = new HashSet<>();
            cache.put(index, newSet);
        }
        newSet.add(object);
    }

    private static void onSetIndex(Object object, String newIndex, String oldIndex,
                                   Map<String, Set<Object>> cache) {
        Set<Object> oldSet = cache.get(oldIndex);
        if (oldSet != null) {
            if (oldSet.size() == 1 && oldSet.contains(object)) {
                cache.remove(object);
            } else {
                oldSet.remove(object);
            }

        }
        onSetIndex(object, newIndex, cache);
    }

    private final List<ObjectFinder> customObjectFinders;

    private final DefaultObjectFinder defaultObjectFinder;

    private final IndexedObjectFinder indexedObjectFinder;

    private GwtFinder() {
        customObjectFinders = new ArrayList<>();
        defaultObjectFinder = new DefaultObjectFinder();
        indexedObjectFinder = new IndexedObjectFinder();

        AfterTestCallbackManager.get().registerCallback(this);
    }

    public void afterTest() {
        customObjectFinders.clear();
        defaultObjectFinder.nodeObjectFinders.clear();
        indexedObjectFinder.mapById.clear();
        indexedObjectFinder.mapByHTML.clear();
        indexedObjectFinder.mapByText.clear();
        indexedObjectFinder.mapByName.clear();
    }

    private boolean checkCondition(Object n, Node before, String after) {
        Object result = find(n, before);
        String s = result == null ? null : result.toString();
        return after.equals(s);
    }

    private Object findInIterable(Iterable<Object> list, Node before, String after, Object current,
                                  Method m) {
        Object found = null;
        for (Object n : list) {
            if (checkCondition(n, before, after)) {
                if (found != null) {
                    throw new GwtFinderException("Not unique object with condition " + before + "="
                            + after);
                }
                found = n;
            }
        }
        if (found == null) {
            throw new GwtFinderException("Not found " + before + "=" + after + " in "
                    + current.getClass().getCanonicalName()
                    + (m != null ? " method " + m.getName() : ""));
        }

        return found;
    }

    private Object findInList(final Object current, final Method m, Node mapEq, String map)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (m.getParameterTypes().length != 1 && m.getParameterTypes()[0] != int.class) {
            throw new GwtFinderException("Unable to navigate " + current.getClass().getCanonicalName()
                    + " with method " + m.getName());
        }
        Method countM = GwtReflectionUtils.getMethod(current.getClass(), m.getName() + "Count");
        if (countM == null) {
            throw new GwtFinderException("Count method not found in "
                    + current.getClass().getCanonicalName() + " method " + m.getName());
        }
        if (countM.getParameterTypes().length > 0) {
            throw new GwtFinderException("Too many parameter in count method "
                    + countM.toGenericString());
        }

        final int count = (Integer) countM.invoke(current);
        return findInIterable(new Iterable<Object>() {

            public Iterator<Object> iterator() {
                return new Iterator<Object>() {

                    int counter = 0;

                    public boolean hasNext() {
                        return counter < count;
                    }

                    public Object next() {
                        try {
                            return m.invoke(current, counter++);
                        } catch (Exception e) {
                            throw new RuntimeException("Iterator exception", e);
                        }
                    }

                    public void remove() {
                        throw new UnsupportedOperationException("Remove not implemented");
                    }

                };
            }

        }, mapEq, map, current, m);
    }

    @SuppressWarnings("unchecked")
    private <T> T findInternal(Object o, Node node) {
        Object current = o;
        Node currentNode = node;
        while (currentNode != null) {
            if (current == null) {
                return null;
            }
            String currentName = currentNode.getLabel();
            boolean mapEqIsProcessed = false;
            boolean ok = false;
            if (!ok) {
                int numberOfParams = currentNode.getParams() != null ? currentNode.getParams().size() : 0;

                Method m = GwtReflectionUtils.getMethod(current.getClass(), currentName, numberOfParams);

                if (m == null) {
                    m = GwtReflectionUtils.getMethod(current.getClass(), "get" + currentName, numberOfParams);
                }
                if (m != null) {
                    try {
                        if (m.getParameterTypes().length == 0 || currentNode.getParams() != null) {
                            current = invoke(current, m, currentNode.getParams());
                            ok = true;
                        } else {
                            current = findInList(current, m, currentNode.getMapEq(), currentNode.getMap());
                            mapEqIsProcessed = true;
                            ok = true;
                        }

                    } catch (Exception e) {
                        throw new GwtFinderException("Unable to get method result on "
                                + o.getClass().getCanonicalName() + ", method " + m.getName()
                                + ", params " + currentNode.getParams(), e);
                    }
                }
            }
            if (!ok) {
                Field f = getField(current, current.getClass(), currentName);
                if (f != null) {
                    try {
                        current = f.get(current);
                        ok = true;
                    } catch (Exception e) {
                        throw new GwtFinderException("Unable to get field value on "
                                + o.getClass().getCanonicalName() + ", field " + f.getName()
                                + ", params " + node, e);
                    }
                }
            }
            if (ok && currentNode.getMap() != null) {
                if (currentNode.getMapEq() == null) {
                    current = proccessMap(current, currentNode.getMap());
                } else {
                    if (!mapEqIsProcessed) {
                        if (current instanceof Iterable<?>) {
                            Iterable<Object> list = (Iterable<Object>) current;
                            current = findInIterable(list, currentNode.getMapEq(), currentNode.getMap(),
                                    current, null);
                        } else {
                            throw new GwtFinderException("Not managed type for iteration "
                                    + current.getClass().getCanonicalName());
                        }
                    }
                }
            }
            if (!ok) {
                return null;
            }
            currentNode = currentNode.getNext();
        }

        return (T) current;
    }

    @SuppressWarnings("unchecked")
    private <T> T findInternal(String... params) {
        for (ObjectFinder customObjectFinder : customObjectFinders) {
            if (customObjectFinder.accept(params)) {
                return (T) customObjectFinder.find(params);
            }
        }

        if (indexedObjectFinder.accept(params)) {
            return (T) indexedObjectFinder.find(params);
        } else if (defaultObjectFinder.accept(params)) {
            return (T) defaultObjectFinder.find(params);
        }

        throw new GwtFinderException("No registered " + ObjectFinder.class.getSimpleName()
                + " instance can be used to find object with param: " + paramsToString(params));
    }

    private Field getField(Object fixture, Class<?> clazz, String fieldName) {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.getName().equalsIgnoreCase(fieldName)) {
                GwtReflectionUtils.makeAccessible(f);
                return f;
            }
        }
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            return getField(fixture, superClazz, fieldName);
        }
        return null;
    }

    private Object invoke(Object current, Method m, List<String> list)
            throws IllegalArgumentException, IllegalAccessException {
        GwtReflectionUtils.makeAccessible(m);
        if (list == null) {
            if (m.getParameterTypes().length == 0) {
                try {
                    return m.invoke(current);
                } catch (InvocationTargetException e) {
                    return null;
                }
            }
        }
        Object[] tab = new Object[m.getParameterTypes().length];
        for (int index = 0; index < m.getParameterTypes().length; index++) {
            if (m.getParameterTypes()[index] == String.class) {
                tab[index] = list.get(index);
            } else if (m.getParameterTypes()[index] == Integer.class
                    || m.getParameterTypes()[index] == int.class) {
                tab[index] = Integer.parseInt(list.get(index));
            } else {
                throw new GwtFinderException("Error while calling method " + m.toGenericString()
                        + ": Not managed type " + m.getParameterTypes()[index]);
            }
        }
        try {
            return m.invoke(current, tab);
        } catch (InvocationTargetException e) {
            return null;
        }
    }

    private String paramsToString(String... params) {
        StringBuilder sb = new StringBuilder();
        for (String param : params) {
            sb.append("'").append(param).append("', ");
        }

        return sb.substring(0, sb.length() - 2);
    }

    private Object proccessMap(Object current, String map) {
        if (current instanceof Map<?, ?>) {
            Map<?, ?> m = (Map<?, ?>) current;
            current = m.get(map);
        } else if (current instanceof List<?>) {
            List<?> l = (List<?>) current;
            current = l.get(Integer.parseInt(map));
        } else {
            throw new GwtFinderException("Object not a map " + current.getClass().getCanonicalName()
                    + " : " + map);
        }
        return current;
    }

}
