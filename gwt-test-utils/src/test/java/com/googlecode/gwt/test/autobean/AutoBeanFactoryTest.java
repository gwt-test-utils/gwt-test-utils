package com.googlecode.gwt.test.autobean;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.autobean.MyAutoBeanFactory.Address;
import com.googlecode.gwt.test.autobean.MyAutoBeanFactory.Person;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AutoBeanFactoryTest extends GwtTestTest {

    public static class AddressImpl implements Address {

    }

    public static class PersonImpl implements Person {

        private Address address;
        private String name;

        public Address getAddress() {
            return address;
        }

        public String getName() {
            return name;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    private final MyAutoBeanFactory myFactory = GWT.create(MyAutoBeanFactory.class);

    @Before
    public void before() {
        new TreeItem();
        setIsClient(false);
    }

    @Test
    public void createComplex() {
        // When
        Person person = myFactory.person().as();
        Address address = myFactory.create(Address.class).as();
        person.setAddress(address);

        // Then
        assertThat(person.getAddress()).isEqualTo(address);
        assertThat(person.getName()).isNull();
    }

    @Test
    public void createFromExisting() {
        // Given
        Person person = new PersonImpl();
        person.setName("Benjamin Linus");
        Address address = new AddressImpl();
        person.setAddress(address);

        // When
        AutoBean<Person> bean = myFactory.create(Person.class, person);

        // When
        String json = AutoBeanCodex.encode(bean).getPayload();

        // Then
        assertThat(person.getName()).isEqualTo("Benjamin Linus");
        assertThat(person.getAddress()).isEqualTo(address);
    }

    @Test
    public void createSimple() {
        // When
        Person person = myFactory.person().as();
        person.setName("John Locke");

        // Then
        assertThat(person.getName()).isEqualTo("John Locke");
        assertThat(person.getAddress()).isNull();
    }

}
