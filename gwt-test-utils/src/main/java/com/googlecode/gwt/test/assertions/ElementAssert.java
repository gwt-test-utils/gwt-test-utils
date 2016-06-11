package com.googlecode.gwt.test.assertions;

import com.google.gwt.dom.client.Element;

/**
 * Assertions for generic {@link Element} instances.
 *
 * @author Gael Lazzari
 */
public class ElementAssert extends BaseElementAssert<ElementAssert, Element> {

    /**
     * Creates a new <code>{@link ElementAssert}</code>.
     *
     * @param actual the actual value to verify.
     */
    protected ElementAssert(Element actual) {
        super(actual, ElementAssert.class);
    }

}
