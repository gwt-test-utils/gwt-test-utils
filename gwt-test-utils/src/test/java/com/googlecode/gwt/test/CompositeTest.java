package com.googlecode.gwt.test;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.test.utils.events.Browser;
import com.googlecode.gwt.test.utils.events.EventBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CompositeTest extends GwtTestTest {

    private Composite composite;
    private int compositeCount;
    private Label label;
    private int labelCount;

    @Before
    public void beforeCompositeTest() {
        label = new Label("wrapped label");
        composite = new MyComposite(label);
        RootPanel.get().add(composite);

        labelCount = 0;
        compositeCount = 0;
    }

    @Test
    public void click_Wrapped() {
        // Given
        label.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                labelCount++;

                assertThat(event.getSource()).isEqualTo(label);
                assertThat(event.getRelativeElement()).isEqualTo(label.getElement());

                // composite handler should be trigger first
                assertThat(compositeCount).isEqualTo(labelCount);
            }
        });

        composite.addDomHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                compositeCount++;

                assertThat(event.getSource()).isEqualTo(composite);
                assertThat(event.getRelativeElement()).isEqualTo(label.getElement());
            }

        }, ClickEvent.getType());

        // When
        Browser.click(label);

        // Then
        assertThat(labelCount).isEqualTo(1);
        assertThat(compositeCount).isEqualTo(1);
    }

    @Test
    public void click_Wrapper() {
        // Given
        label.addClickHandler(event -> {
            labelCount++;

            assertThat(event.getSource()).isEqualTo(label);
            assertThat(event.getRelativeElement()).isEqualTo(label.getElement());
        });

        composite.addDomHandler(event -> {
            compositeCount++;

            assertThat(event.getSource()).isEqualTo(composite);
            assertThat(event.getRelativeElement()).isEqualTo(label.getElement());

            // composite handler should be trigger first
            assertThat(compositeCount).isEqualTo(labelCount + 1);
        }, ClickEvent.getType());

        // When
        Browser.click(composite);

        // Then
        assertThat(labelCount).isEqualTo(1);
        assertThat(compositeCount).isEqualTo(1);
    }

    @Test
    public void fireNativeEvent_Wrapped() {
        // Given
        label.addClickHandler(event -> {
            labelCount++;

            assertThat(event.getSource()).isEqualTo(label);
            assertThat(event.getRelativeElement()).isEqualTo(label.getElement());
        });

        composite.addDomHandler(event -> {
            compositeCount++;

            assertThat(event.getSource()).isEqualTo(composite);
            assertThat(event.getRelativeElement()).isEqualTo(label.getElement());
        }, ClickEvent.getType());

        Event clickEvent = EventBuilder.create(Event.ONCLICK).build();

        // When
        DomEvent.fireNativeEvent(clickEvent, label, label.getElement());

        // Then
        assertThat(labelCount).isEqualTo(1);
        assertThat(compositeCount).isEqualTo(0);

    }

    @Test
    public void fireNativeEvent_Wrapper() {
        // Given
        label.addClickHandler(event -> {
            labelCount++;

            assertThat(event.getSource()).isEqualTo(label);
            assertThat(event.getRelativeElement()).isEqualTo(label.getElement());
        });

        composite.addDomHandler(event -> {
            compositeCount++;

            assertThat(event.getSource()).isEqualTo(composite);
            assertThat(event.getRelativeElement()).isEqualTo(label.getElement());
        }, ClickEvent.getType());

        Event clickEvent = EventBuilder.create(Event.ONCLICK).build();

        // When
        DomEvent.fireNativeEvent(clickEvent, composite, composite.getElement());

        // Then
        assertThat(labelCount).isEqualTo(0);
        assertThat(compositeCount).isEqualTo(1);

    }

    private class MyComposite extends Composite {
        MyComposite(Label label) {
            initWidget(label);
        }
    }

}
