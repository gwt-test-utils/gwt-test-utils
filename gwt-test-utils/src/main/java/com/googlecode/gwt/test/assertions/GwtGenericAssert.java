package com.googlecode.gwt.test.assertions;

import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.description.Description;
import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.internal.Failures;
import org.assertj.core.presentation.StandardRepresentation;

import static java.lang.String.format;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;

/**
 * Template for all gwt-test-utils assertions.
 *
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *            "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *            target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *            implementation</a>.&quot;
 * @param <A> the type of the "actual" value.
 * @author Gael Lazzari
 */
public abstract class GwtGenericAssert<S extends GwtGenericAssert<S, A>, A> extends
        AbstractAssert<S, A> {

    protected final GwtWritableAssertionInfo gwtInfo;

    private final Failures failures = Failures.instance();

    /**
     * Creates a new <code>{@link GwtGenericAssert}</code>.
     *
     * @param actual   the actual value to verify.
     * @param selfType the "self type."
     */
    protected GwtGenericAssert(A actual, Class<S> selfType) {
        super(actual, selfType);

        // hack because assertj "info" is not configurable..
        gwtInfo = new GwtWritableAssertionInfo();
        GwtReflectionUtils.setPrivateFieldValue(this, "info", gwtInfo);
    }

    /**
     * Sets the description of this object.
     *
     * @param description the new description to set.
     * @param args        the args used to fill description as in {@link String#format(String, Object...)}.
     * @return {@code this} object.
     * @throws NullPointerException if the description is {@code null}.
     * @see #describedAs(String, Object...)
     */
    @Override
    public S as(String description, Object... args) {
        return describedAs(format(description, args));
    }

    /**
     * Prefixes the assertion {@link Description} with a raw prefix string.
     *
     * @param prefix the error message prefix.
     * @return this assertion object.
     */
    public S withPrefix(String prefix) {
        this.gwtInfo.prefix(prefix);
        return myself;
    }

    /**
     * Returns a <code>{@link AssertionError}</code> describing an assertion failure. A default
     * description is set if the default error message is not overrided and a custom description is
     * not applied. In both case, the resulting description would be prefixed by the message
     * eventually supplied through {@link GwtGenericAssert#withPrefix(String)}.
     *
     * @param format    the format string.
     * @param arguments arguments referenced by the format specifiers in the format string.
     * @return a {@code AssertionError} describing the assertion failure based on the supplied
     * message.
     */
    @Override
    protected void failWithMessage(String format, Object... arguments) {
        GwtWritableAssertionInfo info = new GwtWritableAssertionInfo();
        info.prefix(gwtInfo.prefix());
        info.overridingErrorMessage(gwtInfo.overridingErrorMessage());

        Description d = gwtInfo.superDescription();
        String newDescription = d != null ? d.value() : this.actual.getClass().getSimpleName();
        info.description(newDescription);

        throw failures.failure(info, new BasicErrorMessageFactory(format, arguments));
    }

    /**
     * Returns a <code>{@link AssertionError}</code> describing a property
     * comparison failure. A default description is set if the default error
     * message is not overrided and a custom description is not applied. In both
     * case, the resulting description would be prefixed by the message
     * eventually supplied through {@link GwtGenericAssert#withPrefix(String)}.
     *
     * @param propertyName
     *            the compared property name
     * @param actual
     *            the actual value.
     * @param expected
     *            the expected value.
     * @return a {@code AssertionError} describing the comparison failure.
     */
    protected AssertionError propertyComparisonFailed(String propertyName, Object actual, Object expected) {
        GwtWritableAssertionInfo info = generateAssertionInfo(propertyName, actual, expected, false);
        return failures.failure(info, shouldBeEqual(actual, expected, new StandardRepresentation()));
    }

    /**
     * Returns a <code>{@link AssertionError}</code> describing a property
     * comparison failure. A default description is set if the default error
     * message is not overrided and a custom description is not applied. In both
     * case, the resulting description would be prefixed by the ignore case
     * label mode and by the message eventually supplied through
     * {@link GwtGenericAssert#withPrefix(String)}.
     *
     * @param propertyName
     *            the compared property name
     * @param actual
     *            the actual value.
     * @param expected
     *            the expected value.
     * @return a {@code AssertionError} describing the comparison failure.
     */
    protected AssertionError propertyIgnoreCaseComparisonFailed(String propertyName, Object actual, Object expected) {
        GwtWritableAssertionInfo info = generateAssertionInfo(propertyName, actual, expected, true);
        return failures.failure(info, shouldBeEqual(actual, expected, new StandardRepresentation()));

    }

    /**
     * Returns a decription of comparison failure. A default description is set
     * if the default error message is not overrided and a custom description is
     * not applied. In both case, the resulting description would be prefixed by
     * the message and eventually supplied through
     * {@link GwtGenericAssert#withPrefix(String)}. If the ignoring case mode is
     * enabled the resulting description would be prefixed by a ignore case mode
     * label.
     *
     * @param propertyName
     *            the compared property name
     * @param actual
     *            the actual value.
     * @param expected
     *            the expected value.
     * @return a {@code AssertionError} describing the comparison failure.
     */
    protected GwtWritableAssertionInfo generateAssertionInfo(String propertyName, Object actual, Object expected, boolean ignoreCase) {
        GwtWritableAssertionInfo info = new GwtWritableAssertionInfo();
        info.prefix(gwtInfo.prefix());
        info.overridingErrorMessage(gwtInfo.overridingErrorMessage());

        Description d = gwtInfo.superDescription();
        String newDescription = d != null ? d.value() + " " + propertyName : this.actual.getClass().getSimpleName() + "'s " + propertyName;
        newDescription = ignoreCase ? "(ignore case mode)  " + newDescription : newDescription;
        info.description(newDescription);
        return info;

    }

}
