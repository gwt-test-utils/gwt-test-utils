package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class FormPanelTest extends GwtTestTest {

    private boolean completeSubmitted;
    private FormPanel form;
    private boolean submitted;

    @Test
    public void add() {
        // Given
        Button b1 = new Button();
        Button b2 = new Button();
        assertThat(b1.isAttached()).isFalse();
        assertThat(b2.isAttached()).isFalse();

        // When 1
        form.add(b1);

        // Then 1
        assertThat(form.getWidget()).isEqualTo(b1);
        assertThat(b1.isAttached()).isTrue();
        assertThat(b2.isAttached()).isFalse();

        // When 2
        try {
            form.add(b2);
            fail("Simple panel can only contain one child widget");
        } catch (Exception e) {
            // Then 2
            assertThat(e instanceof IllegalStateException).isTrue();
        }
    }

    @Before
    public void beforeFormPanel() {
        form = new FormPanel();
        assertThat(form.isAttached()).isFalse();
        RootPanel.get().add(form);
        assertThat(form.isAttached()).isTrue();
    }

    @Test
    public void dimensions() {
        // When 1
        form.setHeight("10");
        form.setWidth("20");
        // Then 1
        assertThat(form.getElement().getStyle().getHeight()).isEqualTo("10");
        assertThat(form.getElement().getStyle().getWidth()).isEqualTo("20");

        // When 2
        form.setSize("30", "40");
        // Then 2
        assertThat(form.getElement().getStyle().getHeight()).isEqualTo("40");
        assertThat(form.getElement().getStyle().getWidth()).isEqualTo("30");

        // When 3
        form.setPixelSize(30, 40);
        // Then 3
        assertThat(form.getElement().getStyle().getHeight()).isEqualTo("40px");
        assertThat(form.getElement().getStyle().getWidth()).isEqualTo("30px");
    }

    @Test
    public void removeFromParent() {
        // When
        form.removeFromParent();

        // Then
        assertThat(form.isAttached()).isFalse();
    }

    @Test
    public void setup() {
        // When
        form.setAction("/myFormHandler");
        form.setEncoding(FormPanel.ENCODING_MULTIPART);

        form.setMethod(FormPanel.METHOD_POST);
        form.setTitle("formTitle");
        form.setStyleName("formStyleName");
        form.addStyleName("addition");

        // Then
        assertThat(form.getAction()).isEqualTo("/myFormHandler");
        assertThat(form.getEncoding()).isEqualTo(FormPanel.ENCODING_MULTIPART);
        assertThat(form.getMethod()).isEqualTo(FormPanel.METHOD_POST);
        assertThat(form.getTitle()).isEqualTo("formTitle");
        assertThat(form.getStyleName()).isEqualTo("formStyleName addition");
    }

    @Test
    public void setWidget() {
        // Given
        Button b1 = new Button();
        Button b2 = new Button();
        assertThat(b1.isAttached()).isFalse();
        assertThat(b2.isAttached()).isFalse();

        // When 1
        form.setWidget(b1);

        // Then 1
        assertThat(form.getWidget()).isEqualTo(b1);
        assertThat(b1.isAttached()).isTrue();
        assertThat(b2.isAttached()).isFalse();

        // When 2
        form.setWidget(b2);

        // Then 2
        assertThat(form.getWidget()).isEqualTo(b2);
        assertThat(b1.isAttached()).isFalse();
        assertThat(b2.isAttached()).isTrue();
    }

    @Test
    public void submit() {
        // Given
        TextBox tb = new TextBox();
        setupFormForSubmitTest(tb);
        submitted = false;
        completeSubmitted = false;

        // When
        form.submit();

        // Then
        assertThat(submitted).isTrue();
        assertThat(completeSubmitted).isFalse();
    }

    @Test
    public void submitComplete() {
        // Given
        TextBox tb = new TextBox();
        tb.setText("some text");
        setupFormForSubmitTest(tb);
        submitted = false;
        completeSubmitted = false;

        // When
        form.submit();

        // Then
        assertThat(submitted).isTrue();
        assertThat(completeSubmitted).isTrue();
    }

    private void setupFormForSubmitTest(final TextBox tb) {
        form.setAction("/myFormHandler");

        // Because we're going to add a FileUpload widget, we'll need to set the
        // form to use the POST method, and multipart MIME encoding.
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);

        // Create a panel to hold all of the form widgets.
        VerticalPanel panel = new VerticalPanel();
        form.setWidget(panel);

        // Create a TextBox, giving it a name so that it will be submitted.
        tb.setName("textBoxFormElement");
        panel.add(tb);

        // Create a ListBox, giving it a name and some values to be associated
        // with
        // its options.
        ListBox lb = new ListBox();
        lb.setName("listBoxFormElement");
        lb.addItem("foo", "fooValue");
        lb.addItem("bar", "barValue");
        lb.addItem("baz", "bazValue");
        panel.add(lb);

        // Create a FileUpload widget.
        FileUpload upload = new FileUpload();
        upload.setName("uploadFormElement");
        panel.add(upload);

        // Add an event handler to the form.
        form.addSubmitHandler(event -> {
            // This event is fired just before the form is submitted. We can
            // take
            // this opportunity to perform validation.
            if (tb.getText() == null || tb.getText().length() == 0) {
                event.cancel();
            }

            submitted = true;
        });
        form.addSubmitCompleteHandler(event -> {
            // When the form submission is successfully completed, this event is
            // fired. Assuming the service returned a response of type
            // text/html,
            // we can get the result text here (see the FormPanel documentation
            // for
            // further explanation).
            completeSubmitted = true;
        });

    }

}
