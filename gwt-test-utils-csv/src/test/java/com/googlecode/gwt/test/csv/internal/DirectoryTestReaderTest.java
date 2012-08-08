package com.googlecode.gwt.test.csv.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStreamReader;
import java.util.List;

import org.junit.Test;

import com.googlecode.gwt.test.csv.Rep1;
import com.googlecode.gwt.test.csv.Rep2;
import com.googlecode.gwt.test.csv.internal.DirectoryTestReader.CsvReader;

public class DirectoryTestReaderTest {

   @Test
   public void csvReader() throws Exception {
      InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/test.csv"));
      List<List<String>> l = CsvReader.readCsv(reader);
      assertNotNull(l);
      assertEquals(7, l.size());
      assertArrayEquals(new String[]{"toto"}, l.get(0).toArray());
      assertArrayEquals(new String[]{}, l.get(1).toArray());
      assertArrayEquals(new String[]{"toto1", "toto2"}, l.get(2).toArray());
      assertArrayEquals(new String[]{"toto3"}, l.get(3).toArray());
      assertArrayEquals(new String[]{"toto4", "toto 5", "toto6"}, l.get(4).toArray());
      assertArrayEquals(new String[]{}, l.get(5).toArray());
      assertArrayEquals(new String[]{"", "toto7"}, l.get(6).toArray());
   }

   @Test
   public void rep1() throws Exception {
      DirectoryTestReader reader = new DirectoryTestReader(Rep1.class);
      assertEquals(4, reader.getTestList().size());
      assertEquals(4, reader.getTestMethods().size());
      assertEquals(2, reader.getMacroFileList().size());
   }

   @Test
   public void rep2() throws Exception {
      DirectoryTestReader reader = new DirectoryTestReader(Rep2.class);
      assertEquals(1, reader.getTestList().size());
      assertEquals(1, reader.getTestMethods().size());
      // because we set the "pattern" attribute on @CsvMacros
      assertEquals(1, reader.getMacroFileList().size());
   }
}
