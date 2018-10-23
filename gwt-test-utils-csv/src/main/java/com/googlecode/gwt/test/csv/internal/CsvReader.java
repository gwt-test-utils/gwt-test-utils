package com.googlecode.gwt.test.csv.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

class CsvReader {

    private static final Pattern REPLACE_PATTERN = Pattern.compile("\\\\;");

    private static final String SEPARATOR = ";";
    /**
     * any ';' character which has not been escaped with a '\'
     */
    private static final Pattern SEPARATOR_PATTERN = Pattern.compile("(?<!\\\\);");

    public static List<List<String>> readCsv(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        List<List<String>> l = new ArrayList<>();
        while (true) {
            String line = br.readLine();
            if (line == null) {
                break;
            }
            line = new String(line.getBytes(), "UTF-8").trim();
            if ("".equals(line)) {
                l.add(new ArrayList<>());
            } else if (!line.startsWith("//")) {
                String[] tab = SEPARATOR_PATTERN.split(line);
                l.add(treatParams(tab));
            }
        }
        return l;
    }

    private static List<String> treatParams(String[] csvParams) {
        List<String> list = new ArrayList<>(csvParams.length);
        for (String csvParam : csvParams) {
            list.add(REPLACE_PATTERN.matcher(csvParam).replaceAll(SEPARATOR));
        }

        return list;
    }
}
