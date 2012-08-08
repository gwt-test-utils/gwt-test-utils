package com.googlecode.gwt.test.gxt2;

import org.junit.After;
import org.junit.Before;

import com.googlecode.gwt.test.GwtTest;
import com.googlecode.gwt.test.gxt2.internal.handlers.BeanModelLookupCreateHandler;

/**
 * <p>
 * Base class for test classes which need to manipulate (directly or indirectly) GXT 2.x components.
 * </p>
 */
public abstract class GxtTest extends GwtTest {

   @Before
   public void setupGxtTest() {
      addGwtCreateHandler(new BeanModelLookupCreateHandler());
   }

   @After
   public final void tearDownGxTest() throws Exception {
      GxtReset.get().reset();
   }

}
