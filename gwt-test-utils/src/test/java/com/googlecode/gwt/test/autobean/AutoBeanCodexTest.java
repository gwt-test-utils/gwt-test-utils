package com.googlecode.gwt.test.autobean;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.autobean.MyAutoBeanFactory.Address;
import com.googlecode.gwt.test.autobean.MyAutoBeanFactory.Person;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AutoBeanCodexTest extends GwtTestTest {

    private final MyAutoBeanFactory myFactory = GWT.create(MyAutoBeanFactory.class);

    @Before
    public void before() {
        new TreeItem();
        setIsClient(false);
    }

    @Test
    public void deserializeFromJson() {
        // Given
        String json = "{\"address\":{},\"name\":\"John Locke\"}";

        // When
        AutoBean<Person> bean = AutoBeanCodex.decode(myFactory, Person.class, json);

        // Then
        assertThat(bean.as().getAddress()).isNotNull();
        assertThat(bean.as().getName()).isEqualTo("John Locke");
    }

    @Test
    public void serializeToJsonWithAutoBeanUtils() {
        // Given
        Person person = myFactory.person().as();
        person.setName("John Locke");
        Address address = myFactory.address().as();
        person.setAddress(address);
        // Retrieve the AutoBean controller
        AutoBean<Person> bean = AutoBeanUtils.getAutoBean(person);

        // When
        String json = AutoBeanCodex.encode(bean).getPayload();

        // Then
        assertThat(json).isEqualTo("{\"address\":{},\"name\":\"John Locke\"}");
    }

}
