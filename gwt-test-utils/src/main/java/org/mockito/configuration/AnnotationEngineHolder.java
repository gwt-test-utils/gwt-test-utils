package org.mockito.configuration;

public class AnnotationEngineHolder {

   private static AnnotationEngine customAnnotationEngine;
   private static AnnotationEngine defaultAnnotationEngine = new GwtInjectingAnnotationEngine();

   public static AnnotationEngine getAnnotationEngine() {
      return (customAnnotationEngine != null) ? customAnnotationEngine : defaultAnnotationEngine;
   }

   public static void reset() {
      setAnnotationEngine(null);
   }

   public static void setAnnotationEngine(AnnotationEngine annotationEngine) {
      customAnnotationEngine = annotationEngine;
   }

}
