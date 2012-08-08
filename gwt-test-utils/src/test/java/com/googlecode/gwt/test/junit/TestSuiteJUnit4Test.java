package com.googlecode.gwt.test.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.googlecode.gwt.test.dom.AnchorElementTest;
import com.googlecode.gwt.test.dom.AreaElementTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({AnchorElementTest.class, AreaElementTest.class})
public class TestSuiteJUnit4Test {

}
