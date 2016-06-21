package com.googlecode.gwt.test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

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
        // When
        AnchorElement e = GWT.create(AnchorElement.class);

        // Then
        assertThat(e).isNotNull();
    }

    @Test
    public void getHostPageBase() {
        // When & Then
        assertThat(GWT.getHostPageBaseURL()).isEqualTo("http://127.0.0.1:8888/");
    }

    @Test
    public void getModuleBaseURL() {
        // When & Then
        assertThat(GWT.getModuleBaseURL()).isEqualTo("http://127.0.0.1:8888/gwt_test_utils_module/");
    }

    @Test
    public void getVersion() {
        // When & Then
        assertThat(GWT.getVersion()).isEqualTo("GWT by gwt-test-utils");
    }

    @Test
    public void initialiseOccursBeforeTheJUnitInitialisationOfTheClass() {
        // When & Then
        assertThat(sToday).isEqualTo("mer. 24 nov.");
    }

    @Test
    public void isClient() {
        // When & Then
        assertThat(GWT.isClient()).isTrue();
    }

    @Test
    public void isScript() {
        // When & Then
        assertThat(GWT.isScript()).isFalse();
    }

    @Test
    public void moduleName() {
        // When & Then
        assertThat(GWT.getModuleName()).isEqualTo("gwt_test_utils_module");
    }

    @Test
    public void runAsync() {
        // Test
        GWT.runAsync(new RunAsyncCallback() {

            @Override
            public void onFailure(Throwable reason) {
                fail("GWT.runAsync() has called \"onFailure\" callback");

            }

            @Override
            public void onSuccess() {
                success = true;
            }
        });

        // Then
        assertThat(success).isTrue();
    }
}
