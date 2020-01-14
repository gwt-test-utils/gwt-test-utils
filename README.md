[![Build Status](https://travis-ci.org/gwt-test-utils/gwt-test-utils.svg?branch=master)](https://travis-ci.org/gwt-test-utils/gwt-test-utils)

In loving memory of Gaël

**gwt-test-utils** is a Java testing framework for [GWT](http://code.google.com/intl/fr-FR/webtoolkit/) applications. It provides a simple way to write **fast** Java tests for your GWT client code, **without GWTTestCase** or any servlet container instance! This means you are able to use any Java tool without restriction: JUnit, reflection, Easymock, Mockito, etc.

Writing tests looks like:

```java
@Test
public void clickOnButtonShouldDisplayMessageInLabel() {
  // Arrange
  SampleView view = new SampleView();
  // ensure the label is not visible at init
  assertThat(view.label).isNotVisible();
  
  // Act: simulate a click event
  Browser.click(view.button);
  
  // Assert: label should be visible and filled
  assertThat(view.label).isVisible().textEquals("The button was clicked!");
}
```

If you want to write **fast** tests which will still deal with your GWT view layout and simulate browser events on your widgets easily, you really should consider this framework. 
The [Getting Started](https://github.com/gwt-test-utils/gwt-test-utils/wiki/Getting-started) page would be a good way to start ;-) 

## Features

* GWT code unit testing with small execution time (no hosted mode / browser launched in the background)
* Simulation for browser's events (click, blur, change, ...)
* Fluent interface for assertions on widgets, based on [assertj](http://joel-costigliola.github.io/assertj/index.html)
* Mocks handling using [Mockito](http://mockito.org/) or [EasyMock](http://easymock.org/)
* Standard maven-surefire-plugin support for testing with [Maven](http://maven.apache.org/)
* Support for GWT + [Spring](http://www.springsource.org/) application testing
* Support for GWT + [Guice](https://github.com/google/guice) application testing
* Support for GWT + [GIN](http://code.google.com/p/google-gin/) application testing, with [Jukito](https://github.com/ArcBees/Jukito) if wanted
* Support for [JUnitParams](https://github.com/Pragmatists/JUnitParams)
* Complex use-case testing using CSV-based scenarios
* Extensibility through the use of custom patchs

## Documentation

Whether you want to...
* Use gwt-test-utils to test your GWT application
* Make your custom widgets testable with the framework
* Contribute code

... you'll find everything you need in the [wiki](https://github.com/gwt-test-utils/gwt-test-utils/wiki).

## Roadmap

Here are the features we are working on, ordered by priority (which is not frozen) :

* Support for GWT 2.8.2
* Support for JUnit 5
* Support for [RequestFactory](http://www.gwtproject.org/doc/latest/DevGuideRequestFactory.html)
* Support for [PowerMock](https://github.com/jayway/powermock)
* Support for [TestNG](http://testng.org/)

In addition, we are daily improving the existing **gwt-test-utils** features. To perfect it, we need your feedback !

## Community

For any question, feedback or contribution, please contact use through the [user group](http://groups.google.com/group/gwt-test-utils-users).

You can also follow the day-to-day evolution of the framework and communicate on it through Twitter : **#GwtTestUtils**
