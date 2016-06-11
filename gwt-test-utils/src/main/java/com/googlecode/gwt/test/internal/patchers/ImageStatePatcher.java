package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Image;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.events.Browser;
import com.googlecode.gwt.test.utils.events.EventBuilder;

@PatchClass(target = "com.google.gwt.user.client.ui.Image$State")
class ImageStatePatcher {

    @PatchMethod
    static void fireSyntheticLoadEvent(Object state, Image image) {
        Event loadEvent = EventBuilder.create(Event.ONLOAD).build();
        Browser.dispatchEvent(image, loadEvent);
    }

}
