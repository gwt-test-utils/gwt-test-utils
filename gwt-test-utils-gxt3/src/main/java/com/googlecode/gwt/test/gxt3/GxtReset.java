package com.googlecode.gwt.test.gxt3;

/**
 * Class in charge of reseting all necessary GXT 3.x internal objects after the
 * execution of a unit test. <strong>For internal use only.</strong>
 * 
 * @author Gael Lazzari
 * 
 */
class GxtReset {

  private static final GxtReset INSTANCE = new GxtReset();

  /**
   * Return the GxtReset instance
   * 
   * @return
   */
  public static GxtReset get() {
    return INSTANCE;
  }

  private GxtReset() {

  }

  /**
   * Reset all necessary GXT internal objects.
   */
  public void reset() {
    // nothing to do yet
  }

}
