package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.FocusWidget;

/**
 * Base class for all {@link FocusWidget} assertions.
 *
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *            "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *            target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *            implementation</a>.&quot;
 * @param <A> the type of the "actual" value.
 * @author Gael Lazzari
 */
public class BaseFocusWidgetAssert<S extends BaseFocusWidgetAssert<S, A>, A extends FocusWidget>
        extends BaseWidgetAssert<S, A> {

    /**
     * Creates a new <code>{@link BaseFocusWidgetAssert}</code>.
     *
     * @param actual   the actual value to verify.
     * @param selfType the "self type."
     */
    protected BaseFocusWidgetAssert(A actual, Class<S> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link FocusWidget} is currently enabled.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link FocusWidget} is not enabled.
     * @see FocusWidget#isEnabled()
     */
    public S isEnabled() {
        if (!actual.isEnabled())
            failWithMessage("should be enabled");

        return myself;
    }

    /**
     * Verifies that the actual {@link FocusWidget} is not currently enabled.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link FocusWidget} is enabled.
     * @see FocusWidget#isEnabled()
     */
    public S isNotEnabled() {
        if (actual.isEnabled())
            failWithMessage("should not be enabled");

        return myself;
    }

}
