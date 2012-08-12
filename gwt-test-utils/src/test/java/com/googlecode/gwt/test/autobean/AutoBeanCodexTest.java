package com.googlecode.gwt.test.autobean;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.googlecode.gwt.test.GwtTestTest;
import com.googlecode.gwt.test.autobean.MyAutoBeanFactory.Address;
import com.googlecode.gwt.test.autobean.MyAutoBeanFactory.Person;

public class AutoBeanCodexTest extends GwtTestTest {

   private final MyAutoBeanFactory myFactory = GWT.create(MyAutoBeanFactory.class);

   @Test
   public void deserializeFromJson() {
      // Arrange
      String json = "{\"address\":{},\"name\":\"John Locke\"}";

      // Act
      AutoBean<Person> bean = AutoBeanCodex.decode(myFactory, Person.class, json);

      // Assert
      assertThat(bean.as().getAddress()).isNotNull();
      assertThat(bean.as().getName()).isEqualTo("John Locke");
   }

   @Test
   public void serializeToJsonWithAutoBeanUtils() {
      // Arrange
      Person person = myFactory.person().as();
      person.setName("John Locke");
      Address address = myFactory.address().as();
      person.setAddress(address);
      // Retrieve the AutoBean controller
      AutoBean<Person> bean = AutoBeanUtils.getAutoBean(person);

      // Act
      String json = AutoBeanCodex.encode(bean).getPayload();

      // Assert
      assertThat(json).isEqualTo("{\"address\":{},\"name\":\"John Locke\"}");
   }

}
