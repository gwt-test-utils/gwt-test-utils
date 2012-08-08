**gwt-test-utils** is a Java testing framework for [GWT](http://code.google.com/intl/fr-FR/webtoolkit/) applications. It provides a simple way to test your GWT User Interfaces **without GWTTestCase** or any servlet container instance ! This means you are able to use any Java tool without restriction : JUnit, reflection, Easymock, Mockito, etc.

If you want to write **fast** tests which will still deal with your GWT view layout and simulate browser events on your widgets easily, you really should considere this framework. 
The [Getting Started](http://code.google.com/p/gwt-test-utils/wiki/GettingStarted) page would be a good way to start ;-) 

Features
========

 * GWT code unit testing with small execution time (no hosted mode / browser launched in the background)
 * Simulation for browser's events (click, blur, change, ...)
 * Mocks handling using [Mockito](http://mockito.org/) or [EasyMock](http://easymock.org/)
 * Standard maven-surefire-plugin support for testing with [Maven](http://maven.apache.org/)
 * Support for GWT + [Spring](http://www.springsource.org/) application testing
 * Support for GWT + [Guice](http://code.google.com/p/google-guice/) application testing
 * Support for GWT + [GIN](http://code.google.com/p/google-gin/) application testing
 * Support for [GXT](http://www.sencha.com/products/gxt/) library (currently only version 2.x, version 3.x is coming !)
 * Complexe use-case testing using CSV-based scenarios
 * Extensibility through the use of custom patchers

Documentation
=============

Whether you want to...
 * Use gwt-test-utils to test your GWT application
 * Get your custom widget library working on a standard JVM to make it compatible with the framework

... you'll find everything you need in the [User Guide](http://code.google.com/p/gwt-test-utils/wiki/UserGuide).

Roadmap
=======

Here is the next features we are working on, ordered by priority (which is not frozen) :

 * **COMING IN 0.40:** Nice support for [GWT overlay types](http://code.google.com/p/google-web-toolkit/wiki/OverlayTypes)
 * **COMING IN 0.40:** Support for [Editors](http://code.google.com/p/google-web-toolkit/wiki/Editors) 
 * **COMING IN 0.40:** Support for [AutoBean framework](http://code.google.com/p/google-web-toolkit/wiki/AutoBean)
 * Support for [GXT 3.0](http://www.sencha.com/products/gxt/)
 * Support for [RequestFactory](http://code.google.com/intl/fr-FR/webtoolkit/doc/latest/DevGuideRequestFactory.html)
 * Support for [PowerMock](http://code.google.com/p/powermock/)
 * Support for [TestNG](http://testng.org/)

In addition, we are daily improving **gwt-test-utils** existing features. To perfect it, we need your feedback !

Community
=========

For any question, feedback or contribution, please contact use through the [user group](http://groups.google.com/group/gwt-test-utils-users).

You can also follow the day-to-day evolution of the framework and communicate on it through Twitter : **#GwtTestUtils**