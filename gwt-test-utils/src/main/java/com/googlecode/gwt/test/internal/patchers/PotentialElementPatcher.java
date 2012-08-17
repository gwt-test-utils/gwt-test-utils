package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.user.client.ui.PotentialElement;

import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@PatchClass(PotentialElement.class)
public class PotentialElementPatcher
{

    //~ Methods ----------------------------------------------------------------
    // We are emulating the elements so there is no actual shim code.
    @PatchMethod static void declareShim() { }

}

