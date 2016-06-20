package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.TextArea;

import static org.assertj.core.util.Objects.areEqual;

/**
 * Base class for {@link TextArea} assertions.
 *
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *            "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *            target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *            implementation</a>.&quot;
 * @param <A> the type of the "actual" value.
 * @author Gael Lazzari
 */
public class BaseTextAreaAssert<S extends BaseTextAreaAssert<S, A>, A extends TextArea> extends
        BaseValueBoxBaseAssert<S, A, String> {

    /**
     * Creates a new <code>{@link BaseTextAreaAssert}</code>.
     *
     * @param actual   the actual value to verify.
     * @param selfType the "self type."
     */
    protected BaseTextAreaAssert(A actual, Class<S> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link TextArea} character width is equal to the given one.
     *
     * @param expected the given character width to compare the actual value to.
     * @return this assertion object.
     * @throws AssertionError if the character width is not equal to the given one.
     * @see TextArea#getCharacterWidth()
     */
    public S characterWidthEquals(int expected) {
        int width = actual.getCharacterWidth();
        if (areEqual(width, expected))
            return myself;
        throw propertyComparisonFailed("character width", width, expected);
    }

    /**
     * Verifies that the actual {@link TextArea} selection length is equal to the given one.
     *
     * @param expected the given selection length to compare the actual value to.
     * @return this assertion object.
     * @throws AssertionError if the actual selection length is not equal to the given one.
     * @see TextArea#getSelectionLength()
     */
    public S selectionLengthEquals(int expected) {
        int length = actual.getSelectionLength();
        if (areEqual(length, expected))
            return myself;
        throw propertyComparisonFailed("selection length", length, expected);
    }

    /**
     * Verifies that the actual {@link TextArea} visible lines number is equal to the given one.
     *
     * @param expected the given visible lines number to compare the actual value to.
     * @return this assertion object.
     * @throws AssertionError if the actual visible lines number is not equal to the given one.
     * @see TextArea#getVisibleLines()
     */
    public S visibleLinesEquals(int expected) {
        int linesNumber = actual.getVisibleLines();
        if (areEqual(linesNumber, expected))
            return myself;
        throw propertyComparisonFailed("visible lines number", linesNumber, expected);
    }

}
