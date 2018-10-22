package com.googlecode.gwt.test;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.MockValueChangeHandler;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RadioButtonTest extends GwtTestTest {

    private boolean tested;

    @Test
    public void changeName() {
        // Given
        RadioButton rb0 = new RadioButton("myRadioGroup", "foo");
        RootPanel.get().add(rb0);
        MockValueChangeHandler<Boolean> rb0MockChangeHandler = new MockValueChangeHandler<Boolean>();
        rb0.addValueChangeHandler(rb0MockChangeHandler);

        RadioButton rb1 = new RadioButton("myRadioGroup", "bar");
        RootPanel.get().add(rb1);
        MockValueChangeHandler<Boolean> rb1MockChangeHandler = new MockValueChangeHandler<Boolean>();
        rb1.addValueChangeHandler(rb1MockChangeHandler);

        // When 1
        rb0.setName("changedGroup");
        Browser.click(rb0);

        // Then 1
        assertThat(rb0.getValue()).isTrue();
        assertThat(rb0MockChangeHandler.getCallCount()).isEqualTo(1);

        // When 2
        rb1.setName("changedGroup");
        Browser.click(rb1);

        assertThat(rb0.getValue()).isFalse();
        assertThat(rb0MockChangeHandler.getCallCount()).isEqualTo(2);
        assertThat(rb0MockChangeHandler.getLast()).isFalse();

        assertThat(rb1.getValue()).isTrue();
        assertThat(rb1MockChangeHandler.getCallCount()).isEqualTo(1);
        assertThat(rb1MockChangeHandler.getLast()).isTrue();
    }

    @Test
    public void click_ClickHandler() {
        // Given
        tested = false;
        RadioButton r = new RadioButton("myRadioGroup", "foo");
        r.addClickHandler(event -> tested = !tested);
        // Preconditions
        assertThat(tested).isEqualTo(false);

        // When
        Browser.click(r);

        // Then
        assertThat(tested).isEqualTo(true);
        assertThat(r.getValue()).isEqualTo(true);
    }

    @Test
    public void click_Twice_ClickHandler() {
        // Given
        tested = false;
        RadioButton r1 = new RadioButton("myRadioGroup", "r1");
        RadioButton r2 = new RadioButton("myRadioGroup", "r2");
        r1.addClickHandler(event -> tested = !tested);

        r2.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                tested = !tested;
            }

        });
        // Preconditions
        assertThat(tested).isEqualTo(false);

        // When
        Browser.click(r1);
        Browser.click(r1);

        // Then
        assertThat(tested).isEqualTo(false);
        assertThat(r1.getValue()).isEqualTo(true);
    }

    @Test
    public void clickNotDetachedRadioButton() {
        // Given
        RadioButton rb0 = new RadioButton("myRadioGroup", "foo");
        MockValueChangeHandler<Boolean> rb0MockChangeHandler = new MockValueChangeHandler<Boolean>();
        rb0.addValueChangeHandler(rb0MockChangeHandler);

        RadioButton rb1 = new RadioButton("myRadioGroup", "bar");
        MockValueChangeHandler<Boolean> rb1MockChangeHandler = new MockValueChangeHandler<Boolean>();
        rb1.addValueChangeHandler(rb1MockChangeHandler);

        // When
        Browser.click(rb0);
        Browser.click(rb1);

        // Then
        assertThat(rb0.getValue()).isTrue();
        assertThat(rb0MockChangeHandler.getCallCount()).isEqualTo(1);
        assertThat(rb1.getValue()).isTrue();
        assertThat(rb1MockChangeHandler.getCallCount()).isEqualTo(1);
    }

    @Test
    public void clickOnDetachedRadionButton() {
        // Given
        RadioButton rb0 = new RadioButton("myRadioGroup", "foo");
        rb0.setValue(true);
        RootPanel.get().add(rb0);
        MockValueChangeHandler<Boolean> rb0MockChangeHandler = new MockValueChangeHandler<Boolean>();
        rb0.addValueChangeHandler(rb0MockChangeHandler);

        RadioButton rb1 = new RadioButton("myRadioGroup", "bar");
        RootPanel.get().add(rb1);
        MockValueChangeHandler<Boolean> rb1MockChangeHandler = new MockValueChangeHandler<Boolean>();
        rb1.addValueChangeHandler(rb1MockChangeHandler);

        // Preconditions
        assertThat(rb0.getValue()).isTrue();
        assertThat(rb0MockChangeHandler.getCallCount()).isEqualTo(0);
        assertThat(rb1.getValue()).isFalse();
        assertThat(rb1MockChangeHandler.getCallCount()).isEqualTo(0);

        // When
        RootPanel.get().remove(rb1);
        Browser.click(rb1);

        // Then
        assertThat(rb1.getValue()).isTrue();
        assertThat(rb1MockChangeHandler.getCallCount()).isEqualTo(1);
        assertThat(rb0.getValue()).isTrue();
        assertThat(rb0MockChangeHandler.getCallCount()).isEqualTo(0);
    }

    @Test
    public void html() {
        // Given
        RadioButton rb = new RadioButton("myRadioGroup", "<h1>foo</h1>", true);
        // Preconditions
        assertThat(rb.getHTML()).isEqualTo("<h1>foo</h1>");

        // When
        rb.setHTML("<h1>test</h1>");

        // Then
        assertThat(rb.getHTML()).isEqualTo("<h1>test</h1>");
        assertThat(rb.getElement().getChild(1).getChildCount()).isEqualTo(1);
        HeadingElement h1 = rb.getElement().getChild(1).getChild(0).cast();
        assertThat(h1.getTagName()).isEqualTo("H1");
        assertThat(h1.getInnerText()).isEqualTo("test");
    }

    @Test
    public void name() {
        // Given
        RadioButton rb = new RadioButton("myRadioGroup", "foo");
        // Preconditions
        assertThat(rb.getName()).isEqualTo("myRadioGroup");

        // When
        rb.setName("name");

        // Then
        assertThat(rb.getName()).isEqualTo("name");
    }

    @Test
    public void radioButton_Group() {
        // Given
        RadioButton rb0 = new RadioButton("myRadioGroup", "foo");
        RootPanel.get().add(rb0);
        MockValueChangeHandler<Boolean> rb0MockChangeHandler = new MockValueChangeHandler<Boolean>();
        rb0.addValueChangeHandler(rb0MockChangeHandler);

        RadioButton rb1 = new RadioButton("myRadioGroup", "bar");
        RootPanel.get().add(rb1);
        MockValueChangeHandler<Boolean> rb1MockChangeHandler = new MockValueChangeHandler<Boolean>();
        rb1.addValueChangeHandler(rb1MockChangeHandler);

        RadioButton rb2 = new RadioButton("myRadioGroup", "baz");
        RootPanel.get().add(rb2);
        MockValueChangeHandler<Boolean> rb2MockChangeHandler = new MockValueChangeHandler<Boolean>();
        rb2.addValueChangeHandler(rb2MockChangeHandler);

        // When 1
        Browser.click(rb1);

        // Then 1
        assertThat(rb0.getValue()).isEqualTo(false);
        assertThat(rb0MockChangeHandler.getCallCount()).isEqualTo(0);

        assertThat(rb1.getValue()).isEqualTo(true);
        assertThat(rb1MockChangeHandler.getCallCount()).isEqualTo(1);
        assertThat(rb1MockChangeHandler.getLast()).isTrue();

        assertThat(rb2.getValue()).isEqualTo(false);
        assertThat(rb2MockChangeHandler.getCallCount()).isEqualTo(0);

        // When 2
        Browser.click(rb2);

        // Then 2
        assertThat(rb0.getValue()).isEqualTo(false);
        assertThat(rb0MockChangeHandler.getCallCount()).isEqualTo(0);

        assertThat(rb1.getValue()).isEqualTo(false);
        assertThat(rb1MockChangeHandler.getCallCount()).isEqualTo(2);
        assertThat(rb1MockChangeHandler.getLast()).isFalse();

        assertThat(rb2.getValue()).isEqualTo(true);
        assertThat(rb2MockChangeHandler.getCallCount()).isEqualTo(1);
        assertThat(rb2MockChangeHandler.getLast()).isTrue();
    }

    @Test
    public void text() {
        // Given
        RadioButton rb = new RadioButton("myRadioGroup", "foo");
        // Preconditions
        assertThat(rb.getText()).isEqualTo("foo");

        // When
        rb.setText("text");

        // Then
        assertThat(rb.getText()).isEqualTo("text");
    }

    @Test
    public void title() {
        // Given
        RadioButton rb = new RadioButton("myRadioGroup", "foo");
        // Preconditions
        assertThat(rb.getTitle()).isEqualTo("");

        // When
        rb.setTitle("title");

        // Then
        assertThat(rb.getTitle()).isEqualTo("title");
    }

    @Test
    public void visible() {
        // Given
        RadioButton rb = new RadioButton("myRadioGroup", "foo");
        // Preconditions
        assertThat(rb.isVisible()).isEqualTo(true);

        // When
        rb.setVisible(false);

        // Then
        assertThat(rb.isVisible()).isEqualTo(false);
    }

}
