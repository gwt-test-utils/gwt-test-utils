package org.mockito.configuration;

import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.DefaultAnnotationEngine;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.FieldAnnotationProcessor;
import org.mockito.internal.configuration.InjectingAnnotationEngine;
import org.mockito.internal.configuration.injection.scanner.InjectMocksScanner;

import com.googlecode.gwt.test.Mock;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;

/**
 * Copied from {@link InjectingAnnotationEngine} to be able to inject our customized
 * {@link FieldAnnotationProcessor}.
 */
public class GwtInjectingAnnotationEngine implements AnnotationEngine {

   private final AnnotationEngine delegate;
   private final AnnotationEngine spyAnnotationEngine;

   public GwtInjectingAnnotationEngine() {
      delegate = new DefaultAnnotationEngine();

      registerAnnotationProcessor(Mock.class, new GwtMockAnnotationProcessor());
      registerAnnotationProcessor(org.mockito.Mock.class, new MockitoMockAnnotationProcessor());

      spyAnnotationEngine = new GwtSpyAnnotationEngine();
   }

   /***
    * Create a mock using {@link DefaultAnnotationEngine}
    * 
    * @see org.mockito.internal.configuration.DefaultAnnotationEngine
    * @see org.mockito.configuration.AnnotationEngine#createMockFor(java.lang.annotation.Annotation,
    *      java.lang.reflect.Field)
    */
   @Deprecated
   public Object createMockFor(Annotation annotation, Field field) {
      return delegate.createMockFor(annotation, field);
   }

   /**
    * Initializes mock/spies dependencies for objects annotated with &#064;InjectMocks for given
    * testClassInstance.
    * <p>
    * See examples in javadoc for {@link MockitoAnnotations} class.
    * 
    * @param testClassInstance Test class, usually <code>this</code>
    */
   public void injectMocks(final Object testClassInstance) {
      Class<?> clazz = testClassInstance.getClass();
      Set<Field> mockDependentFields = new HashSet<Field>();
      Set<Object> mocks = newMockSafeHashSet();

      while (clazz != Object.class) {
         new InjectMocksScanner(clazz).addTo(mockDependentFields);
         new GwtMockScanner(testClassInstance, clazz).addPreparedMocks(mocks);
         clazz = clazz.getSuperclass();
      }

      new DefaultInjectionEngine().injectMocksOnFields(mockDependentFields, mocks,
               testClassInstance);
   }

   /**
    * Process the fields of the test instance and create Mocks, Spies, Captors and inject them on
    * fields annotated &#64;InjectMocks.
    * 
    * <p>
    * This code process the test class and the super classes.
    * <ol>
    * <li>First create Mocks, Spies, Captors.</li>
    * <li>Then try to inject them.</li>
    * </ol>
    * 
    * @param clazz Not used
    * @param testInstance The instance of the test, should not be null.
    * 
    * @see org.mockito.configuration.AnnotationEngine#process(Class, Object)
    */
   public void process(Class<?> clazz, Object testInstance) {
      processIndependentAnnotations(testInstance.getClass(), testInstance);
      processInjectMocks(testInstance.getClass(), testInstance);
   }

   protected <A extends Annotation> void registerAnnotationProcessor(Class<A> annotationClass,
            FieldAnnotationProcessor<A> fieldAnnotationProcessor) {
      GwtReflectionUtils.callPrivateMethod(delegate, "registerAnnotationProcessor",
               annotationClass, fieldAnnotationProcessor);
   }

   private void processIndependentAnnotations(final Class<?> clazz, final Object testInstance) {
      Class<?> classContext = clazz;
      while (classContext != Object.class) {
         // this will create @Mocks, @Captors, etc:
         delegate.process(classContext, testInstance);
         // this will create @Spies:
         spyAnnotationEngine.process(classContext, testInstance);

         classContext = classContext.getSuperclass();
      }
   }

   private void processInjectMocks(final Class<?> clazz, final Object testInstance) {
      Class<?> classContext = clazz;
      while (classContext != Object.class) {
         injectMocks(testInstance);
         classContext = classContext.getSuperclass();
      }
   }

}
