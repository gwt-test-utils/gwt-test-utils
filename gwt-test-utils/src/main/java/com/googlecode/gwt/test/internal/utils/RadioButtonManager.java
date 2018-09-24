package com.googlecode.gwt.test.internal.utils;

import com.google.gwt.user.client.ui.RadioButton;
import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * RadioButton instance Manager, to emulate radigroup behaviour. <strong>For internal use
 * only.<strong>
 *
 * @author Gael Lazzari
 */
public class RadioButtonManager implements AfterTestCallback {

    private static RadioButtonManager INSTANCE;

    public static void beforeSetName(RadioButton rb, String newName) {

        Set<RadioButton> rbs = get().getRadioButtons(rb);

        if (rbs == null) {
            // not attached, do nothing
            return;
        }

        // 1. remove from the old radiogroup
        rbs.remove(rb);

        // 3. register in the new radiogroup
        get().registerWithNewName(rb, newName);

    }

    public static RadioButtonManager get() {
        if (INSTANCE == null) {
            INSTANCE = new RadioButtonManager();
        }

        return INSTANCE;
    }

    public static void onLoad(RadioButton rb) {
        get().register(rb);
    }

    public static void onRadioGroupChanged(RadioButton rb, Boolean value, boolean fireEvents) {
        if (value != null && value) {
            Set<RadioButton> rbs = get().getRadioButtons(rb);

            if (rbs == null) {
                // not attached, do nothing
                return;
            }
            for (RadioButton radioButton : rbs) {
                if (!rb.equals(radioButton) && radioButton.getValue()) {
                    radioButton.setValue(false, fireEvents);
                }
            }
        }
    }

    public static void onUnload(RadioButton rb) {
        get().deregister(rb);
    }

    private final Map<String, Set<RadioButton>> map = new HashMap<>();

    private RadioButtonManager() {
        AfterTestCallbackManager.get().registerCallback(this);
    }

    public void afterTest() {
        map.clear();
    }

    private void deregister(RadioButton rb) {
        Set<RadioButton> rbs = getRadioButtons(rb);
        if (rbs != null) {
            rbs.remove(rb);
        }
    }

    private Set<RadioButton> getRadioButtons(RadioButton rb) {
        if (!rb.isAttached()) {
            return null;
        }

        return getRadioButtons(rb.getName());
    }

    private Set<RadioButton> getRadioButtons(String groupName) {
        Set<RadioButton> set = map.get(groupName);
        if (set == null) {
            set = new HashSet<>();
            map.put(groupName, set);
        }

        return set;
    }

    private void register(RadioButton rb) {
        getRadioButtons(rb).add(rb);
    }

    private void registerWithNewName(RadioButton rb, String newName) {
        getRadioButtons(newName).add(rb);
    }

}
