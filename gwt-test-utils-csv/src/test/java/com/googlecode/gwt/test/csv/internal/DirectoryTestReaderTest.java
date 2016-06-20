package com.googlecode.gwt.test.csv.internal;

import com.googlecode.gwt.test.csv.Rep1;
import com.googlecode.gwt.test.csv.Rep2;
import com.googlecode.gwt.test.csv.internal.DirectoryTestReader.CsvReader;
import org.junit.Test;

import java.io.InputStreamReader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DirectoryTestReaderTest {

    @Test
    public void csvReader() throws Exception {
        InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/test.csv"));
        List<List<String>> l = CsvReader.readCsv(reader);
        assertThat(l).hasSize(7);
        assertThat(l.get(0)).containsExactly("toto");
        assertThat(l.get(1)).isEmpty();
        assertThat(l.get(2)).containsExactly("toto1", "toto2");
        assertThat(l.get(3)).containsExactly("toto3");
        assertThat(l.get(4)).containsExactly("toto4", "toto 5", "toto6");
        assertThat(l.get(5)).isEmpty();
        assertThat(l.get(6)).containsExactly("", "toto7");
    }

    @Test
    public void rep1() throws Exception {
        DirectoryTestReader reader = new DirectoryTestReader(Rep1.class);
        assertThat(reader.getTestList()).hasSize(4);
        assertThat(reader.getTestMethods()).hasSize(4);
        assertThat(reader.getMacroFileList()).hasSize(2);
    }

    @Test
    public void rep2() throws Exception {
        DirectoryTestReader reader = new DirectoryTestReader(Rep2.class);
        assertThat(reader.getTestList()).hasSize(1);
        assertThat(reader.getTestMethods()).hasSize(1);
        // because we set the "pattern" attribute on @CsvMacros
        assertThat(reader.getMacroFileList()).hasSize(1);
    }
}
