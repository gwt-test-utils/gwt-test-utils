package com.googlecode.gwt.test.internal.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some Gwt String utility methods. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
public class GwtStringUtils {

    private static Pattern DOUBLE_PATTERN = Pattern.compile("^\\s*(\\d+\\.?(\\d*)).*$");

    private static Pattern NUMBER_PATTERN = Pattern.compile("^\\s*(\\d+).*$");

    public static String camelize(String s) {
        String[] strings = s.split("[-|_|\\s]");

        if (strings.length <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(strings[0].toLowerCase());

        for (int i = 1; i < strings.length; i++) {
            String string = strings[i];
            if (string.length() > 0) {
                sb.append(Character.toUpperCase(string.charAt(0))).append(
                        string.substring(1).toLowerCase());
            }
        }

        return sb.toString();
    }

    public static String dehyphenize(String string) {
        StringBuilder buffer = new StringBuilder(string);

        for (int c = 0; c < buffer.length(); c++) {
            char character = buffer.charAt(c);
            if (character == '-') {
                buffer.deleteCharAt(c);
                character = buffer.charAt(c);
                buffer.setCharAt(c, Character.toUpperCase(character));
            }
        }

        return buffer.toString();
    }

    public static String hyphenize(String string) {
        StringBuilder sb = new StringBuilder(string);

        for (int c = 0; c < sb.length(); c++) {
            char character = sb.charAt(c);
            if (Character.isUpperCase(character)) {
                sb.setCharAt(c, Character.toLowerCase(character));
                sb.insert(c, '-');
                c++;
            }
        }

        return sb.toString();
    }

    public static double parseDouble(String value, double defaultValue) {
        Matcher m = DOUBLE_PATTERN.matcher(value);
        if (m.matches()) {
            return Double.parseDouble(m.group(1));
        }
        return defaultValue;

    }

    public static int parseInt(String value, int defaultValue) {
        Matcher m = NUMBER_PATTERN.matcher(value);
        if (m.matches()) {
            return Integer.parseInt(m.group(1));
        }
        return defaultValue;

    }

    public static String resolveBackSlash(String input) {
        if (input == null || "".equals(input.trim())) {
            return input;
        }

        StringBuffer b = new StringBuffer();
        boolean backSlashSeen = false;
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            if (!backSlashSeen) {
                if (c == '\\') {
                    backSlashSeen = true;
                } else {
                    b.append(c);
                }
            } else {
                switch (c) {
                    case '\\':
                        b.append('\\');
                        break;
                    case 'n':
                        b.append('\n');
                        break;
                    case 'r':
                        b.append('\r');
                        break;
                    case 't':
                        b.append('\t');
                        break;
                    case 'f':
                        b.append('\f');
                        break;
                    case 'b':
                        b.append('\b');
                        break;
                    default:
                        b.append(c);
                }
                backSlashSeen = false;
            }
        }
        return b.toString();
    }

    /**
     * 250px => 250px 250.1px => 250.1px 250.0px => 250px
     *
     * @param string
     * @return The transformed value
     */
    public static String treatDoubleValue(String string) {

        if (string == null || "".equals(string)) {
            return string;
        }

        Matcher m = DOUBLE_PATTERN.matcher(string);
        if (m.matches() && !"".equals(m.group(2)) && Double.valueOf(m.group(2)) == 0) {
            return string.replace("." + m.group(2), "");
        } else {
            return string;
        }
    }

    private GwtStringUtils() {

    }
}
