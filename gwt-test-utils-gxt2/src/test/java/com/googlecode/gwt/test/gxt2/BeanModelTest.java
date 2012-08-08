package com.googlecode.gwt.test.gxt2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.extjs.gxt.samples.resources.client.model.Customer;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;

public class BeanModelTest extends GwtGxtTest {

   private Customer customer;
   private BeanModelFactory factory;

   @Before
   public void beforeBeanModelTest() {
      factory = BeanModelLookup.get().getFactory(Customer.class);
      customer = new Customer("gael", "gael@gwt-test-utils.com", 25);

   }

   @Test
   public void get() {
      // Arrange
      BeanModel model = factory.createModel(customer);

      // Act
      String name = (String) model.get("name");
      String email = (String) model.get("email");
      int age = (Integer) model.get("age");

      // Assert
      assertEquals("gael", name);
      assertEquals("gael@gwt-test-utils.com", email);
      assertEquals(25, age);
   }

   @Test
   public void getProperties() {
      // Arrange
      BeanModel model = factory.createModel(customer);

      // Act
      Collection<String> propertyNames = model.getPropertyNames();

      // Assert
      assertTrue(propertyNames.contains("name"));
      assertTrue(propertyNames.contains("age"));
      assertTrue(propertyNames.contains("email"));
   }

}
