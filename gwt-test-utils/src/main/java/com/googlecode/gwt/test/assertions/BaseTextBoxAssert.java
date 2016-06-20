package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.TextBox;

import static org.assertj.core.util.Objects.areEqual;

/**
 * Base class for {@link TextBox} assertions.
 *
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *            "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *            target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *            implementation</a>.&quot;
 * @param <A> the type of the "actual" value.
 * @author Gael Lazzari
 */
public class BaseTextBoxAssert<S extends BaseTextBoxAssert<S, A>, A extends TextBox> extends
        BaseValueBoxBaseAssert<S, A, String> {

    /**
     * Creates a new <code>{@link BaseTextBoxAssert}</code>.
     *
     * @param actual   the actual value to verify.
     * @param selfType the "self type."
     */
    protected BaseTextBoxAssert(A actual, Class<S> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link TextBox} max length is equal to the given one.
     *
     * @param expected the given max length to compare the actual value to.
     * @return this assertion object.
     * @throws AssertionError if the actual max length is not equal to the given one.
     * @see TextBox#getMaxLength()
     */
    public S maxLengthEquals(int expected) {
        int length = actual.getMaxLength();
        if (areEqual(length, expected))
            return myself;
        throw propertyComparisonFailed("max length", length, expected);
    }

    /**
     * Verifies that the actual {@link TextBox} visible length is equal to the given one.
     *
     * @param expected the given visible length to compare the actual value to.
     * @return this assertion object.
     * @throws AssertionError if the actual visible length is not equal to the given one.
     * @see TextBox#getVisibleLength()
     */
    public S visibleLengthEquals(int expected) {
        int length = actual.getVisibleLength();
        if (areEqual(length, expected))
            return myself;
        throw propertyComparisonFailed("visible length", length, expected);
    }

}
