package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.ListBox;
import com.googlecode.gwt.test.utils.WidgetUtils;

import static org.assertj.core.util.Objects.areEqual;

/**
 * Base class for {@link ListBox} assertions.
 *
 * @param <S> used to simulate "self types." For more information please read &quot;<a href=
 *            "http://passion.forco.de/content/emulating-self-types-using-java-generics-simplify-fluent-api-implementation"
 *            target="_blank">Emulating 'self types' using Java Generics to simplify fluent API
 *            implementation</a>.&quot;
 * @param <A> the type of the "actual" value.
 * @author Gael Lazzari
 */
public class BaseListBoxAssert<S extends BaseListBoxAssert<S, A>, A extends ListBox> extends
        BaseFocusWidgetAssert<S, A> {

    /**
     * Creates a new <code>{@link BaseListBoxAssert}</code>.
     *
     * @param actual   the actual value to verify.
     * @param selfType the "self type."
     */
    protected BaseListBoxAssert(A actual, Class<S> selfType) {
        super(actual, selfType);
    }

    /**
     * Verifies that the actual {@link ListBox} text content exactly matches the supplied one.
     *
     * @param expected the expected content to compare the actual content to.
     * @return this assertion object.
     */
    public S dataMatches(String... expected) {
        int contentSize = expected.length;
        if (contentSize != actual.getItemCount()) {
            failWithMessage("does not match actual listbox's content: [%s]",
                    WidgetUtils.getListBoxContentToString(actual));
        }
        for (int i = 0; i < contentSize; i++) {
            if (!expected[i].equals(actual.getItemText(i))) {
                failWithMessage("does not match actual listbox's content: [%s]",
                        WidgetUtils.getListBoxContentToString(actual));
            }
        }

        return myself;
    }

    /**
     * Verifies that the actual {@link ListBox} selected value is equal to the given one.
     *
     * @param expected the given selected value to compare the actual selected value to.
     * @return this assertion object.
     * @throws AssertionError if the actual selected value is null or not equal to the given one.
     */
    public S selectedValueEquals(String expected) {
        int selectedIndex = actual.getSelectedIndex();
        if (selectedIndex == -1) {
            failWithMessage("listbox does not have a selected value");
        }

        String selected = actual.getItemText(selectedIndex);
        if (areEqual(selected, expected)) {
            return myself;
        } else {
            throw propertyComparisonFailed("selected value", selected, expected);
        }
    }

}
