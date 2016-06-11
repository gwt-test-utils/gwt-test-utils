package com.googlecode.gwt.test.assertions;

import com.googlecode.gwt.test.finder.GwtFinder;

import static com.googlecode.gwt.test.finder.GwtFinder.object;

/**
 * Wrapper arround a raw object retrieved via {@link GwtFinder#object(String...)}.
 *
 * @author Gael Lazzari
 */
public class GwtInstance {

    static void validateNotNull(Class<?> type) {
        if (type == null)
            throw new IllegalArgumentException("Expected type cannot be null");
    }

    private final String[] identifier;
    private final Object instance;

    /**
     * Creates a new <code>{@link GwtInstance}</code>.
     *
     * @param identifier The targeted object's identifier.
     */
    protected GwtInstance(String... identifier) {
        this.identifier = identifier;
        this.instance = object(identifier);
    }

    /**
     * Cast the wrapped object to the specified class
     *
     * @param type The type to convert to.
     * @return The casted object
     * @throws NullPointerException if the wrapped object is null.
     * @throws ClassCastException   if the wrapped object cannot be cast to the supplied type.
     */
    @SuppressWarnings("unchecked")
    public <T> T ofType(Class<T> type) throws NullPointerException, ClassCastException {
        validateNotNull(type);

        if (instance == null) {
            throw new NullPointerException("Object with identifier '" + identifierToString()
                    + "' is null");
        }

        return (T) instance;
    }

    final Object getInstance() {
        return instance;
    }

    private String identifierToString() {
        StringBuilder sb = new StringBuilder();
        for (String s : identifier) {
            sb.append("'").append(s).append("', ");
        }

        return sb.substring(0, sb.length() - 2);
    }

}
