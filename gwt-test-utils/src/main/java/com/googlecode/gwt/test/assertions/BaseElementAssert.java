package com.googlecode.gwt.test.assertions;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.UIObject;
import com.googlecode.gwt.test.utils.GwtDomUtils;

import static org.assertj.core.util.Objects.areEqual;

/**
 * Base class for all {@link Element} assertions.
 *
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *            "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *            target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *            implementation</a>.&quot;
 * @param <A> the type of the "actual" value.
 * @author Gael Lazzari
 */
public abstract class BaseElementAssert<S extends BaseElementAssert<S, A>, A extends Element>
        extends GwtGenericAssert<S, A> {

    /**
     * Creates a new <code>{@link BaseElementAssert}</code>.
     *
     * @param actual   the actual value to verify.
     * @param selfType the "self type."
     */
    protected BaseElementAssert(A actual, Class<S> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual <code>{@link UIObject}</code> styleName does not have the supplied
     * one(s).
     *
     * @param notExpected the given styleName(s) to check.
     * @return this assertion object.
     * @throws AssertionError if the actual styleName value is not equal to the given one.
     */
    public S doesNotHaveStyle(String... notExpected) {
        for (String styleName : notExpected) {
            if (GwtDomUtils.hasStyle(actual, styleName)) {
                failWithMessage("should not have style %s", styleName);
            }
        }

        return myself;
    }

    /**
     * Verifies that the actual <code>{@link UIObject}</code> styleName has the supplied one(s).
     *
     * @param expected the given styleName(s) to check.
     * @return this assertion object.
     * @throws AssertionError if the actual styleName value is not equal to the given one.
     */
    public S hasStyle(String... expected) {
        for (String styleName : expected) {
            if (!GwtDomUtils.hasStyle(actual, styleName)) {
                failWithMessage("should have style %s", styleName);
            }
        }

        return myself;
    }

    /**
     * Verifies that the actual {@link UIObject} HTML contains the given sequence.
     *
     * @param sequence the sequence to search for.
     * @return this assertion object.
     * @throws AssertionError if the actual HTML value does not contain the given sequence.
     */
    public S htmlContains(String sequence) {
        String html = HasHTML.class.isInstance(actual) ? ((HasHTML) actual).getHTML()
                : actual.getInnerHTML();
        if (!html.contains(sequence))
            failWithMessage("actual HTML [%s] does not contains [%s]", html, sequence);

        return myself;
    }

    /**
     * Verifies that the actual {@link UIObject} HTML is equal to the given one.
     *
     * @param expected the given HTML to compare the actual HTML value to.
     * @return this assertion object.
     * @throws AssertionError if the actual HTML value is not equal to the given one.
     */
    public S htmlEquals(String expected) {
        String html = HasHTML.class.isInstance(actual) ? ((HasHTML) actual).getHTML()
                : actual.getInnerHTML();
        if (areEqual(html, expected))
            return myself;
        throw propertyComparisonFailed("HTML", html, expected);
    }

    /**
     * Verifies that the actual {@link UIObject} is not visible.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link UIObject} is visible.
     */
    public S isNotVisible() {
        if (GwtDomUtils.isVisible(actual))
            failWithMessage("should not be visible");

        return myself;
    }

    /**
     * Verifies that the actual {@link UIObject} is visible.
     *
     * @return this assertion object.
     * @throws AssertionError if the actual {@link UIObject} is not visible.
     */
    public S isVisible() {
        if (!GwtDomUtils.isVisible(actual))
            failWithMessage("should be visible");

        return myself;
    }

    /**
     * Verifies that the actual styleName is equal to the given one.
     *
     * @param expected the given styleName to compare the actual styleName value to.
     * @return this assertion object.
     * @throws AssertionError if the actual styleName value is not equal to the given one.
     */
    public S styleNameEquals(String expected) {
        String styleName = GwtDomUtils.getStyleName(actual);
        if (areEqual(styleName, expected))
            return myself;
        throw propertyComparisonFailed("styleName", styleName, expected);
    }

    /**
     * Verifies that the actual {@link UIObject} stylePrimaryName is equal to the given one.
     *
     * @param expected the given stylePrimaryName to compare the actual stylePrimaryName value to.
     * @return this assertion object.
     * @throws AssertionError if the actual stylePrimaryName value is not equal to the given one.
     */
    public S stylePrimaryNameEquals(String expected) {
        String stylePrimaryName = GwtDomUtils.getStylePrimaryName(actual);
        if (areEqual(stylePrimaryName, expected))
            return myself;
        throw propertyComparisonFailed("stylePrimaryName", stylePrimaryName, expected);
    }

    /**
     * Verifies that the actual {@link UIObject} text contains the given sequence.
     *
     * @param sequence the sequence to search for.
     * @return this assertion object.
     * @throws AssertionError if the actual text value does not contain the given sequence.
     */
    public S textContains(String sequence) {
        String text = HasText.class.isInstance(actual) ? ((HasText) actual).getText()
                : actual.getInnerText();
        if (!text.contains(sequence))
            failWithMessage("actual text [%s] does not contains [%s]", text, sequence);

        return myself;
    }

    /**
     * Verifies that the actual {@link UIObject} text is equal to the given one.
     *
     * @param expected the given text to compare the actual text value to.
     * @return this assertion object.
     * @throws AssertionError if the actual text value is not equal to the given one.
     */
    public S textEquals(String expected) {
        String text = HasText.class.isInstance(actual) ? ((HasText) actual).getText()
                : actual.getInnerText();
        if (areEqual(text, expected))
            return myself;
        throw propertyComparisonFailed("text", text, expected);
    }

    /**
     * Verifies that the actual {@link UIObject} title contains the given sequence.
     *
     * @param sequence the sequence to search for.
     * @return this assertion object.
     * @throws AssertionError if the actual title value does not contain the given sequence.
     */
    public S titleContains(String sequence) {
        String title = actual.getTitle();
        if (!title.contains(sequence))
            failWithMessage("actual title [%s] does not contains [%s]", title, sequence);

        return myself;
    }

    /**
     * Verifies that the actual {@link UIObject} title is equal to the given one.
     *
     * @param expected the given title to compare the actual title value to.
     * @return this assertion object.
     * @throws AssertionError if the actual title value is not equal to the given one.
     */
    public S titleEquals(String expected) {
        String title = actual.getTitle();
        if (areEqual(title, expected))
            return myself;
        throw propertyComparisonFailed("title", title, expected);
    }

}
