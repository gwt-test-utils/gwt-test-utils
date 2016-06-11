package com.googlecode.gwt.test.csv.tools;

import com.google.gwt.user.client.ui.*;

public class DefaultWidgetVisitor implements WidgetVisitor {

    public void visitHasHTML(HasHTML hasHTML, WidgetRepository repository) {
        if (hasHTML.getHTML() != null && hasHTML.getHTML().length() > 0) {
            repository.addAlias(hasHTML.getHTML(), hasHTML);
        }
    }

    public void visitHasName(HasName hasName, WidgetRepository repository) {
        if (hasName.getName() != null && hasName.getName().length() > 0) {
            repository.addAlias(hasName.getName(), hasName);
        }
    }

    public void visitHasText(HasText hasText, WidgetRepository repository) {
        if (hasText.getText() != null && hasText.getText().length() > 0) {
            repository.addAlias(hasText.getText(), hasText);
        }
    }

    public void visitWidget(Widget widget, WidgetRepository repository) {
        if (widget.getElement() == null) {
            return;
        }
        String id = widget.getElement().getId();
        if (id != null && id.length() > 0) {
            // check if the widget to add is the inner textbox of a suggestbox :
            // keep
            // the suggestbox
            Object old = repository.addAlias(id, widget);
            if (old != null && SuggestBox.class.isInstance(old)) {
                repository.addAlias(id, old);
            }

        }
    }
}
