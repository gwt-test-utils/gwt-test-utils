package com.googlecode.gwt.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;

import com.google.gwt.user.client.rpc.RemoteService;
import com.googlecode.gwt.test.internal.handlers.GwtTestGWTBridge;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

/**
 * <p>
 * Base class for test classes which make use of a mocking framework, such as
 * {@link org.easymock.EasyMock EasyMock} or {@link org.mockito.Mockito Mockito} .
 * </p>
 * 
 * <p>
 * This class provides methods to register mock objects into the context of a test class. This is
 * required so that application calls to {@link com.google.gwt.core.client.GWT#create(Class)
 * GWT.Create(MyClassToMock)} will return the corresponding mock object of type MyClassToMock.
 * </p>
 * 
 * @author Eric Therond
 */
public abstract class GwtTestWithMocks extends GwtTest {

   private class MockCreateHandler implements GwtCreateHandler {

      private final Map<Class<?>, Object> mockObjects;

      public MockCreateHandler(Map<Class<?>, Object> mockObjects) {
         this.mockObjects = mockObjects;
      }

      public Object create(Class<?> classLiteral) throws Exception {
         if (RemoteService.class.isAssignableFrom(classLiteral)) {
            String asyncName = classLiteral.getName() + "Async";
            classLiteral = GwtReflectionUtils.getClass(asyncName);
         }
         return mockObjects.get(classLiteral);
      }

   }

   protected List<Class<?>> mockedClasses = new ArrayList<Class<?>>();
   protected Set<Field> mockFields;
   protected Map<Class<?>, Object> mockObjects = new HashMap<Class<?>, Object>();

   public GwtTestWithMocks() {
      GwtTestGWTBridge.get().setMockCreateHandler(new MockCreateHandler(mockObjects));
      mockFields = getMockFields();
      for (Field f : mockFields) {
         mockedClasses.add(f.getType());
      }
   }

   @After
   public void teardownGwtTestWithMocks() {
      mockObjects.clear();
   }

   /**
    * Adds a mock object to the list of mocks used in the context of this test class.
    * 
    * @param createClass The class for which a mock object is being defined
    * @param mock the mock instance
    */
   protected Object addMockedObject(Class<?> createClass, Object mock) {
      return mockObjects.put(createClass, mock);
   }

   protected Set<Field> getMockFields() {
      Set<Field> set = new HashSet<Field>();
      set.addAll(GwtReflectionUtils.getAnnotatedField(this.getClass(), Mock.class).keySet());
      return set;
   }

}
