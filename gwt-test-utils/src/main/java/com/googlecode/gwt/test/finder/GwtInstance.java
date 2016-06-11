package com.googlecode.gwt.test.finder;

/**
 * Wrapper arround a raw object retrieved via {@link GwtFinder#object(String...)}.
 *
 * @author Gael Lazzari
 */
public class GwtInstance {

    private final String[] identifier;
    private final Object wrapped;

    /**
     * Creates a new <code>{@link GwtInstance}</code>.
     *
     * @param instance
     * @param identifier The targeted object's identifier.
     */
    protected GwtInstance(Object instance, String... identifier) {
        this.identifier = identifier;
        this.wrapped = instance;
    }

    /**
     * Returns the raw object wrapped in the current {@link GwtInstance}, without any type checking.
     *
     * @return the wrapped raw object.
     */
    public final Object getRaw() {
        return wrapped;
    }

    /**
     * Returns the identifier which was used to find the wrapped object.
     *
     * @return the used identifier.
     */
    public String identifierToString() {
        StringBuilder sb = new StringBuilder();
        for (String s : identifier) {
            sb.append("'").append(s).append("', ");
        }

        return sb.substring(0, sb.length() - 2);
    }

    /**
     * Cast the wrapped object to the specified class
     *
     * @param type The type to convert to.
     * @return The casted object
     * @throws IllegalArgumentException if the given type is {@code null}.
     * @throws NullPointerException     if the wrapped object is null.
     * @throws ClassCastException       if the wrapped object cannot be cast to the supplied type.
     */
    @SuppressWarnings("unchecked")
    public <T> T ofType(Class<T> type) throws IllegalArgumentException, NullPointerException,
            ClassCastException {
        validateNotNull(type);

        if (wrapped == null) {
            throw new NullPointerException("Object with identifier " + identifierToString()
                    + " is null");
        }

        if (!type.isInstance(wrapped)) {
            throw new ClassCastException("Object of type " + wrapped.getClass().getName()
                    + " with identifier " + identifierToString()
                    + " cannot be cast to an instance of " + type.getName());
        }

        return (T) wrapped;
    }

    private void validateNotNull(Class<?> type) {
        if (type == null)
            throw new IllegalArgumentException("Expected type cannot be null");
    }

}
