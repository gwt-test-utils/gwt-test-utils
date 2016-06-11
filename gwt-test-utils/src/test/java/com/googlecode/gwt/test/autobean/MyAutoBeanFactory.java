package com.googlecode.gwt.test.autobean;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface MyAutoBeanFactory extends AutoBeanFactory {

    interface Address {
        // Other properties, as above
    }

    // Declare any bean-like interface with matching getters and setters, no base type is necessary
    interface Person {
        Address getAddress();

        String getName();

        void setAddress(Address a);

        void setName(String name);
    }

    AutoBean<Address> address();

    AutoBean<Person> person();

}
