package com.googlecode.gwt.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.googlecode.gwt.test.utils.events.Browser;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class MainGwtTest extends GwtTestTest {

    private String sToday;
    private boolean success;

    @Before
    public void beforeGWTTest() {
        setLocale(new Locale("fr", "FR"));
        Calendar cal = new GregorianCalendar();
        cal.set(2010, 10, 24);
        sToday = DateTimeFormat.getFormat("EEE dd MMM").format(cal.getTime());
        success = false;
    }

    @Test
    public void create() {
        // Act
        AnchorElement e = GWT.create(AnchorElement.class);

        // Assert
        assertThat(e).isNotNull();
    }

    @Test
    public void getHostPageBase() {
        // Act & Assert
        assertEquals("http://127.0.0.1:8888/", GWT.getHostPageBaseURL());
    }

    @Test
    public void getModuleBaseURL() {
        // Act & Assert
        assertEquals("http://127.0.0.1:8888/gwt_test_utils_module/", GWT.getModuleBaseURL());
    }

    @Test
    public void getVersion() {
        // Act & Assert
        assertEquals("GWT by gwt-test-utils", GWT.getVersion());
    }

    @Test
    public void initialiseOccursBeforeTheJUnitInitialisationOfTheClass() {
        // Act & Assert
        assertEquals("mer. 24 nov.", sToday);
    }

    @Test
    public void isClient() {
        // Act & Assert
        assertTrue(GWT.isClient());
    }

    @Test
    public void isScript() {
        // Act & Assert
        assertFalse(GWT.isScript());
    }

    @Test
    public void moduleName() {
        // Act & Assert
        assertEquals("gwt_test_utils_module", GWT.getModuleName());
    }

    @Test
    public void runAsync() {
        // Arrange
        Button b = new Button();
        b.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                GWT.runAsync(new RunAsyncCallback() {

                    public void onFailure(Throwable reason) {
                        fail("GWT.runAsync() has called \"onFailure\" callback");

                    }

                    public void onSuccess() {
                        success = true;
                    }
                });

            }
        });

        // Act
        Browser.click(b);

        // Assert
        assertTrue(success);

    }
}
