package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for all {@link Widget} assertions.
 *
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *            "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *            target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *            implementation</a>.&quot;
 * @param <A> the type of the "actual" value.
 * @author Gael Lazzari
 */
public abstract class BaseWidgetAssert<S extends BaseWidgetAssert<S, A>, A extends Widget> extends
        BaseUIObjectAssert<S, A> {

    /**
     * Creates a new <code>{@link BaseWidgetAssert}</code>.
     *
     * @param actual   the actual value to verify.
     * @param selfType the "self type."
     */
    protected BaseWidgetAssert(A actual, Class<S> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link Widget} is currently attached to the browser's document (i.e.,
     * there is an unbroken chain of widgets between this widget and the underlying browser
     * document).
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link UIObject} is not visible.
     */
    public S isAttached() {
        if (!actual.isAttached())
            failWithMessage("should be attached");

        return myself;
    }

    /**
     * Verifies that the actual {@link Widget} is not currently attached to the browser's document
     * (i.e., there is a broken chain of widgets between this widget and the underlying browser
     * document).
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link UIObject} is visible.
     */
    public S isNotAttached() {
        if (actual.isAttached())
            failWithMessage("should not be attached");

        return myself;
    }

}
