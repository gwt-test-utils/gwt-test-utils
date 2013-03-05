package com.googlecode.gwt.test.jukito;

import org.jukito.JukitoModule;
import org.junit.Test;
import org.mockito.Mockito;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.googlecode.gwt.test.GwtModule;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Przemysław Gałązka
 * @since 04-03-2013
 */

@GwtModule("com.googlecode.gwt.test.jukito.JukitoGwtTest")
public class JukitoGwtTestRunnerTest extends JukitoGwtTest {


  @Inject
  IsSuperButton someMockButton;


  @Test
  public void shouldInjectBoundImplementation(@Real IsSuperButton someRealButton) throws Exception {
    assertThat(someRealButton, instanceOf(SupperButtonImpl.class));
  }


  @Test
  public void shouldWorkWithMocks() throws Exception {
    //-------------------- GIVEN -------------------------------------------------------------------

    //-------------------- WHEN --------------------------------------------------------------------
    someMockButton.setVisible(false);

    //-------------------- THEN --------------------------------------------------------------------
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
