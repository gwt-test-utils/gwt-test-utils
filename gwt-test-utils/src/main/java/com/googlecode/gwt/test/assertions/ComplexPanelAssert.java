package com.googlecode.gwt.test.assertions;

import com.google.gwt.user.client.ui.ComplexPanel;

/**
 * Assertions for generic {@link ComplexPanel} instances.
 * 
 * @author Gael Lazzari
 * 
 */
public class ComplexPanelAssert extends BaseComplexPanelAssert<ComplexPanelAssert, ComplexPanel> {

   /**
    * Creates a new <code>{@link ComplexPanelAssert}</code>.
    * 
    * @param actual the actual value to verify.
    */
   protected ComplexPanelAssert(ComplexPanel actual) {
      super(actual, ComplexPanelAssert.class);
   }

}
