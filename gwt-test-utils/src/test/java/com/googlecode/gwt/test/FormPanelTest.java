package com.googlecode.gwt.test;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormPanelTest extends GwtTestTest {

    private boolean completeSubmitted;
    private FormPanel form;
    private boolean submitted;

    @Test
    public void add() {
        // Arrange
        Button b1 = new Button();
        Button b2 = new Button();
        assertFalse(b1.isAttached());
        assertFalse(b2.isAttached());

        // Act 1
        form.add(b1);

        // Assert 1
        assertEquals(b1, form.getWidget());
        assertTrue(b1.isAttached());
        assertFalse(b2.isAttached());

        // Act 2
        try {
            form.add(b2);
            fail("Simple panel can only contain one child widget");
        } catch (Exception e) {
            // Assert 2
            assertTrue(e instanceof IllegalStateException);
        }
    }

    @Before
    public void beforeFormPanel() {
        form = new FormPanel();
        assertFalse(form.isAttached());
        RootPanel.get().add(form);
        assertTrue(form.isAttached());
    }

    @Test
    public void dimensions() {
        // Act 1
        form.setHeight("10");
        form.setWidth("20");
        // Assert 1
        assertEquals("10", form.getElement().getStyle().getHeight());
        assertEquals("20", form.getElement().getStyle().getWidth());

        // Act 2
        form.setSize("30", "40");
        // Assert 2
        assertEquals("40", form.getElement().getStyle().getHeight());
        assertEquals("30", form.getElement().getStyle().getWidth());

        // Act 3
        form.setPixelSize(30, 40);
        // Assert 3
        assertEquals("40px", form.getElement().getStyle().getHeight());
        assertEquals("30px", form.getElement().getStyle().getWidth());
    }

    @Test
    public void removeFromParent() {
        // Act
        form.removeFromParent();

        // Assert
        assertFalse(form.isAttached());
    }

    @Test
    public void setup() {
        // Act
        form.setAction("/myFormHandler");
        form.setEncoding(FormPanel.ENCODING_MULTIPART);

        form.setMethod(FormPanel.METHOD_POST);
        form.setTitle("formTitle");
        form.setStyleName("formStyleName");
        form.addStyleName("addition");

        // Assert
        assertEquals("/myFormHandler", form.getAction());
        assertEquals(FormPanel.ENCODING_MULTIPART, form.getEncoding());
        assertEquals(FormPanel.METHOD_POST, form.getMethod());
        assertEquals("formTitle", form.getTitle());
        assertEquals("formStyleName addition", form.getStyleName());
    }

    @Test
    public void setWidget() {
        // Arrange
        Button b1 = new Button();
        Button b2 = new Button();
        assertFalse(b1.isAttached());
        assertFalse(b2.isAttached());

        // Act 1
        form.setWidget(b1);

        // Assert 1
        assertEquals(b1, form.getWidget());
        assertTrue(b1.isAttached());
        assertFalse(b2.isAttached());

        // Act 2
        form.setWidget(b2);

        // Assert 2
        assertEquals(b2, form.getWidget());
        assertFalse(b1.isAttached());
        assertTrue(b2.isAttached());
    }

    @Test
    public void submit() {
        // Arrange
        TextBox tb = new TextBox();
        setupFormForSubmitTest(tb);
        submitted = false;
        completeSubmitted = false;

        // Act
        form.submit();

        // Assert
        assertTrue(submitted);
        assertFalse(completeSubmitted);
    }

    @Test
    public void submitComplete() {
        // Arrange
        TextBox tb = new TextBox();
        tb.setText("some text");
        setupFormForSubmitTest(tb);
        submitted = false;
        completeSubmitted = false;

        // Act
        form.submit();

        // Assert
        assertTrue(submitted);
        assertTrue(completeSubmitted);
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
        form.addSubmitHandler(new FormPanel.SubmitHandler() {
            public void onSubmit(SubmitEvent event) {
                // This event is fired just before the form is submitted. We can
                // take
                // this opportunity to perform validation.
                if (tb.getText() == null || tb.getText().length() == 0) {
                    event.cancel();
                }

                submitted = true;
            }
        });
        form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            public void onSubmitComplete(SubmitCompleteEvent event) {
                // When the form submission is successfully completed, this event is
                // fired. Assuming the service returned a response of type
                // text/html,
                // we can get the result text here (see the FormPanel documentation
                // for
                // further explanation).
                completeSubmitted = true;
            }
        });

    }

}
