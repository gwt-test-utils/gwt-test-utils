[![Build Status](https://buildhive.cloudbees.com/job/gwt-test-utils/job/gwt-test-utils/badge/icon)](https://buildhive.cloudbees.com/job/gwt-test-utils/job/gwt-test-utils/)

**gwt-test-utils** is a Java testing framework for [GWT](http://code.google.com/intl/fr-FR/webtoolkit/) applications. It provides a simple way to write **fast** Java tests for your GWT client code, **without GWTTestCase** or any servlet container instance ! This means you are able to use any Java tool without restriction : JUnit, reflection, Easymock, Mockito, etc.

Writing tests looks like:

```java
@Test
public void clickOnButtonShouldDisplayMessageInLabel() {
  // Arrange
  SampleView view = new SampleView();
  // ensure the label is not visible at init
  assertThat(view.label).isNotVisible();
  
  // Act : simulate a click event
  Browser.click(view.button);
  
  // Assert: label should be visible and filled
  assertThat(view.label).isVisible().textEquals("The button was clicked !");
}
```

If you want to write **fast** tests which will still deal with your GWT view layout and simulate browser events on your widgets easily, you really should consider this framework. 
The [Getting Started](https://github.com/gwt-test-utils/gwt-test-utils/wiki/Getting-started) page would be a good way to start ;-) 

## Features

* GWT code unit testing with small execution time (no hosted mode / browser launched in the background)
* Simulation for browser's events (click, blur, change, ...)
* Fluent interface for assertions on widgets, based on [fest-assert](https://github.com/alexruiz/fest-assert-2.x)
* Mocks handling using [Mockito](http://mockito.org/) or [EasyMock](http://easymock.org/)
* Standard maven-surefire-plugin support for testing with [Maven](http://maven.apache.org/)
* Support for GWT + [Spring](http://www.springsource.org/) application testing
* Support for GWT + [Guice](http://code.google.com/p/google-guice/) application testing
* Support for GWT + [GIN](http://code.google.com/p/google-gin/) application testing
* Support for [JUnitParams](http://code.google.com/p/junitparams/)
* Support for [GXT](http://www.sencha.com/products/gxt/) library (currently only version 2.x, version 3.x is coming !)
* Complexe use-case testing using CSV-based scenarios
* Extensibility through the use of custom patchers

## Documentation

Whether you want to...
* Use gwt-test-utils to test your GWT application
* Make your custom widgets testable with the framework
* Contributing code

... you'll find everything you need in the [wiki](https://github.com/gwt-test-utils/gwt-test-utils/wiki).

## Roadmap

Here is the next features we are working on, ordered by priority (which is not frozen) :

* Support for [GWT-Bootstrap](https://github.com/gwtbootstrap/gwt-bootstrap)
* Support for [GXT 3.0](http://www.sencha.com/products/gxt/)
* Support for [RequestFactory](http://code.google.com/intl/fr-FR/webtoolkit/doc/latest/DevGuideRequestFactory.html)
* Support for [PowerMock](http://code.google.com/p/powermock/)
* Support for [TestNG](http://testng.org/)

In addition, we are daily improving **gwt-test-utils** existing features. To perfect it, we need your feedback !

## Community

For any question, feedback or contribution, please contact use through the [user group](http://groups.google.com/group/gwt-test-utils-users).

You can also follow the day-to-day evolution of the framework and communicate on it through Twitter : **#GwtTestUtils**

[![githalytics.com alpha](https://cruel-carlota.pagodabox.com/f46d3a68545b8811a5f5c1d837704e6d "githalytics.com")](http://githalytics.com/gwt-test-utils/gwt-test-utils)
