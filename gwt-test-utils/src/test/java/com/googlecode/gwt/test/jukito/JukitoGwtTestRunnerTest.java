package com.googlecode.gwt.test.jukito;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.googlecode.gwt.test.GwtTestTest;
import org.jukito.JukitoModule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * @author Przemysław Gałązka
 * @since 04-03-2013
 */
@RunWith(JukitoGwtTestRunner.class)
public class JukitoGwtTestRunnerTest extends GwtTestTest {

    @Inject
    IsSuperButton someMockButton;

    @Test
    public void shouldInjectBoundImplementation(@Real
                                                        IsSuperButton someRealButton) throws Exception {
        assertThat(someRealButton).isInstanceOf(SupperButtonImpl.class);
    }

    @Test
    public void shouldWorkWithMocks() throws Exception {
        // -------------------- GIVEN
        // -------------------------------------------------------------------

        // -------------------- WHEN
        // --------------------------------------------------------------------
        someMockButton.setVisible(false);

        // -------------------- THEN
        // --------------------------------------------------------------------
        verify(someMockButton).setVisible(false);
    }

    public static class Module extends JukitoModule {

        @Override
        protected void configureTest() {
        }

        @Provides
        @Real
        IsSuperButton realButton() {
            return new SupperButtonImpl();
        }

    }

}
