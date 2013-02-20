package com.googlecode.gwt.test.csv;

import java.io.File;

import org.junit.Before;

import com.googlecode.gwt.test.csv.runner.CsvMethodInvocation;
import com.googlecode.gwt.test.csv.runner.CsvTestExecutionHandler;

@CsvDirectory("functional-tests/1")
@CsvMacros("functional-tests/macros")
public class Rep1 extends MyGwtShell {

   @Before
   public void before() {
      addCsvInvocationHandler(new CsvTestExecutionHandler() {

         public void afterCsvTestExecution(File testFile) {

         }

         public void beforeCsvTestExecution(File testFile) {
            System.out.println(testFile.getAbsolutePath());
         }

         public void onCsvMethodInvocation(CsvMethodInvocation data) {

         }
      });
   }

}
