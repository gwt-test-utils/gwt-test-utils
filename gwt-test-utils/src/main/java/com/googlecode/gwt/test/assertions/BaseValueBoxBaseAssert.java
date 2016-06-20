package com.googlecode.gwt.test.assertions;

import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.user.client.ui.ValueBoxBase;

import java.text.ParseException;

import static org.assertj.core.util.Objects.areEqual;

/**
 * Base class for {@link ValueBoxBase} assertions.
 *
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *            "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *            target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *            implementation</a>.&quot;
 * @param <A> the type of the "actual" value.
 * @param <T> the type of the ValueBoxBase value.
 * @author Gael Lazzari
 */
public class BaseValueBoxBaseAssert<S extends BaseValueBoxBaseAssert<S, A, T>, A extends ValueBoxBase<T>, T>
        extends BaseFocusWidgetAssert<S, A> {

    /**
     * Creates a new <code>{@link BaseValueBoxBaseAssert}</code>.
     *
     * @param actual   the actual value to verify.
     * @param selfType the "self type."
     */
    protected BaseValueBoxBaseAssert(A actual, Class<S> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link ValueBoxBase} cursor position is equal to the given one.
     *
     * @param expected the given cursor position value to compare its actual value to.
     * @return this assertion object.
     * @throws AssertionError if the actual cursor position value is not equal to the given one.
     * @see ValueBoxBase#getCursorPos()
     */
    public S cursorPosEquals(int expected) {
        int cursorPos = actual.getCursorPos();
        if (areEqual(cursorPos, expected))
            return myself;
        throw propertyComparisonFailed("cursorPos", cursorPos, expected);
    }

    /**
     * Verifies that the actual {@link ValueBoxBase} direction is equal to the given one.
     *
     * @param expected the given direction to compare the actual direction value to.
     * @return this assertion object.
     * @throws AssertionError if the actual direction is not equal to the given one.
     * @see ValueBoxBase#getDirection()
     */
    public S directionEquals(Direction expected) {
        Direction direction = actual.getDirection();
        if (areEqual(direction, expected))
            return myself;
        throw propertyComparisonFailed("Direction", direction, expected);
    }

    /**
     * Verifies that the actual {@link ValueBoxBase} directionEstimator is equal to the given one.
     *
     * @param expected the given directionEstimator to compare the actual direction value to.
     * @return this assertion object.
     * @throws AssertionError if the actual directionEstimator is not equal to the given one.
     * @see ValueBoxBase#getDirectionEstimator()
     */
    public S directionEstimatorEquals(DirectionEstimator expected) {
        DirectionEstimator directionEstimator = actual.getDirectionEstimator();
        if (areEqual(directionEstimator, expected))
            return myself;
        throw propertyComparisonFailed("DirectionEstimator", directionEstimator, expected);
    }

    /**
     * Verifies that the actual {@link ValueBoxBase} is not ready-only.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link ValueBoxBase} is ready-only.
     * @see ValueBoxBase#isReadOnly()
     */
    public S isNotReadOnly() {
        if (actual.isReadOnly())
            failWithMessage("should not be read-only");

        return myself;
    }

    /**
     * Verifies that the actual {@link ValueBoxBase} is ready-only.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link ValueBoxBase} is not ready-only.
     * @see ValueBoxBase#isReadOnly()
     */
    public S isReadOnly() {
        if (!actual.isReadOnly())
            failWithMessage("should be read-only");

        return myself;
    }

    /**
     * Verifies that the actual {@link ValueBoxBase} name is equal to the given one.
     *
     * @param expected the given name to compare the actual name value to.
     * @return this assertion object.
     * @throws AssertionError if the actual name value is not equal to the given one.
     * @see ValueBoxBase#getName()
     */
    public S nameEquals(String expected) {
        String name = actual.getName();
        if (areEqual(name, expected))
            return myself;
        throw propertyComparisonFailed("name", name, expected);
    }

    /**
     * Verifies that the actual {@link ValueBoxBase} value is equal to the given one.
     *
     * @param expected the given value to compare the actual value to.
     * @return this assertion object.
     * @throws AssertionError if the actual value is not equal to the given one.
     * @see ValueBoxBase#getValue()
     */
    public S valueEquals(T expected) {
        T value = actual.getValue();
        if (areEqual(value, expected))
            return myself;
        throw propertyComparisonFailed("value", value, expected);
    }

    /**
     * Verifies that the actual {@link ValueBoxBase} value is equal to the given one.
     *
     * @param expected the given value to compare the actual value to.
     * @return this assertion object.
     * @throws AssertionError if the actual value is not equal to the given one or if a
     *                        {@link ParseException} is thrown while converting the value.
     * @see ValueBoxBase#getValueOrThrow()
     */
    public S valueOrThrowEquals(T expected) {
        try {
            T value = actual.getValueOrThrow();
            if (areEqual(value, expected))
                return myself;
            throw propertyComparisonFailed("value", value, expected);
        } catch (ParseException e) {
            throw new AssertionError(
                    "unexpected ParseException while getting the value of the tested "
                            + actual.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

}
