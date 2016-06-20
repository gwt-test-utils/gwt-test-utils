package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Base class for all {@link PopupPanel} assertions.
 *
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *            "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *            target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *            implementation</a>.&quot;
 * @param <A> the type of the "actual" value.
 * @author Gael Lazzari
 */
public class BasePopupPanelAssert<S extends BasePopupPanelAssert<S, A>, A extends PopupPanel>
        extends BaseWidgetAssert<S, A> {

    /**
     * Creates a new <code>{@link BasePopupPanelAssert}</code>.
     *
     * @param actual   the actual value to verify.
     * @param selfType the "self type."
     */
    protected BasePopupPanelAssert(A actual, Class<S> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link PopupPanel} is modal.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link PopupPanel} is not modal.
     * @see PopupPanel#isModal()
     */
    public S isModal() {
        if (!actual.isModal())
            failWithMessage("should be modal");

        return myself;
    }

    /**
     * Verifies that the actual {@link PopupPanel} is not modal.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link PopupPanel} is modal.
     * @see PopupPanel#isModal()
     */
    public S isNotModal() {
        if (actual.isModal())
            failWithMessage("should not be modal");

        return myself;
    }

    /**
     * Verifies that the actual {@link PopupPanel} is not currently showing.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link PopupPanel} is showing.
     * @see PopupPanel#isShowing()
     */
    public S isNotShowing() {
        if (actual.isShowing())
            failWithMessage("should not be showing");

        return myself;
    }

    /**
     * Verifies that the actual {@link PopupPanel} is currently showing.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link PopupPanel} is not showing.
     * @see PopupPanel#isShowing()
     */
    public S isShowing() {
        if (!actual.isShowing())
            failWithMessage("should be showing");

        return myself;
    }

}
