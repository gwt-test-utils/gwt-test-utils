package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.IncrementalCommand;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

@SuppressWarnings("deprecation")
@PatchClass(DeferredCommand.class)
class DeferredCommandPatcher {

    @PatchMethod
    static void addCommand(Command command) {
        command.execute();
    }

    @PatchMethod
    static void addCommand(IncrementalCommand command) {
        command.execute();
    }

}
