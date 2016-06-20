package com.googlecode.gwt.test.assertions;

import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;

/**
 * Writable information about a GWT assertion.
 *
 * @author Gael Lazzari
 */
class GwtWritableAssertionInfo extends WritableAssertionInfo {

    private String prefix;

    @Override
    public Description description() {
        return (prefix == null) ? super.description()
                : new TextDescription(computeDescribitionText());
    }

    @Override
    public String descriptionText() {
        return (prefix == null) ? super.descriptionText() : computeDescribitionText();
    }

    public String prefix() {
        return prefix;
    }

    public void prefix(String prefix) {
        this.prefix = prefix;
    }

    public Description superDescription() {
        return super.description();
    }

    private String computeDescribitionText() {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix.trim());

        Description d = super.description();
        if (d != null) {
            sb.append(" ").append(d.value());
        }
        return sb.toString();
    }

}
