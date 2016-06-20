package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.CheckBox;

import static org.assertj.core.util.Objects.areEqual;

/**
 * Base class for {@link CheckBox} assertions.
 *
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *            "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *            target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *            implementation</a>.&quot;
 * @param <A> the type of the "actual" value.
 * @author Gael Lazzari
 */
public class BaseCheckBoxAssert<S extends BaseCheckBoxAssert<S, A>, A extends CheckBox> extends
        BaseFocusWidgetAssert<S, A> {

    /**
     * Creates a new <code>{@link BaseCheckBoxAssert}</code>.
     *
     * @param actual   the actual value to verify.
     * @param selfType the "self type."
     */
    protected BaseCheckBoxAssert(A actual, Class<S> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link CheckBox} form value is equal to the given one.
     *
     * @param expected the given form value to compare the actual form value to.
     * @return this assertion object.
     * @throws AssertionError if the actual form value is not equal to the given one.
     * @see CheckBox#getFormValue()
     */
    public S formValueEquals(String expected) {
        String formValue = actual.getFormValue();
        if (areEqual(formValue, expected))
            return myself;
        throw propertyComparisonFailed("form value", formValue, expected);
    }

    /**
     * Verifies that the actual {@link CheckBox} is checked.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link CheckBox} is not checked.
     * @see CheckBox#getValue()
     */
    public S isChecked() {
        if (!actual.getValue())
            failWithMessage("should be checked");

        return myself;


    }

    /**
     * Verifies that the actual {@link CheckBox} is not checked.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link CheckBox} is checked.
     * @see CheckBox#getValue()
     */
    public S isNotChecked() {
        if (actual.getValue())
            failWithMessage("should not be checked");

        return myself;
    }

    /**
     * Verifies that the actual {@link CheckBox} is not word wrapping.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link CheckBox} is word wrapping.
     * @see CheckBox#getWordWrap()
     */
    public S isNotWordWrap() {
        if (actual.getWordWrap())
            failWithMessage("should not be word wrapping");

        return myself;
    }

    /**
     * Verifies that the actual {@link CheckBox} is word wrapping.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link CheckBox} is not word wrapping.
     * @see CheckBox#getWordWrap()
     */
    public S isWordWrap() {
        if (!actual.getWordWrap())
            failWithMessage("should be word wrapping");

        return myself;
    }

    /**
     * Verifies that the actual {@link CheckBox} name is equal to the given one.
     *
     * @param expected the given name to compare the actual name to.
     * @return this assertion object.
     * @throws AssertionError if the actual name value is not equal to the given one.
     * @see CheckBox#getName()
     */
    public S nameEquals(String expected) {
        String name = actual.getName();
        if (areEqual(name, expected))
            return myself;
        throw propertyComparisonFailed("name", name, expected);
    }

}
