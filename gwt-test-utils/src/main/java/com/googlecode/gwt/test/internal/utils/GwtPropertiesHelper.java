package com.googlecode.gwt.test.internal.utils;

import com.googlecode.gwt.test.exceptions.GwtTestI18NException;
import com.googlecode.gwt.test.internal.AfterTestCallback;
import com.googlecode.gwt.test.internal.AfterTestCallbackManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Some .properties files utility methods. It enables the caching of resources. <strong>For internal
 * use only.</strong>
 *
 * @author Bertrand Paquet
 * @author Gael Lazzari
 */
public class GwtPropertiesHelper implements AfterTestCallback {

    /*
     * Read in a "logical line" from an InputStream/Reader, skip all comment and blank lines and
     * filter out those leading whitespace characters ( , and ) from the beginning of a
     * "natural line". Method returns the char length of the "logical line" and stores the line in
     * "lineBuf".
     */
    static class LineReader {
        byte[] inByteBuf;

        char[] inCharBuf;

        int inLimit = 0;
        int inOff = 0;
        InputStream inStream;
        char[] lineBuf = new char[1024];
        Reader reader;

        public LineReader(InputStream inStream) {
            this.inStream = inStream;
            inByteBuf = new byte[8192];
        }

        public LineReader(Reader reader) {
            this.reader = reader;
            inCharBuf = new char[8192];
        }

        int readLine() throws IOException {
            int len = 0;
            char c = 0;

            boolean skipWhiteSpace = true;
            boolean isCommentLine = false;
            boolean isNewLine = true;
            boolean appendedLineBegin = false;
            boolean precedingBackslash = false;
            boolean skipLF = false;

            while (true) {
                if (inOff >= inLimit) {
                    inLimit = inStream == null ? reader.read(inCharBuf) : inStream.read(inByteBuf);
                    inOff = 0;
                    if (inLimit <= 0) {
                        if (len == 0 || isCommentLine) {
                            return -1;
                        }
                        return len;
                    }
                }
                if (inStream != null) {
                    // The line below is equivalent to calling a
                    // ISO8859-1 decoder.
                    c = (char) (0xff & inByteBuf[inOff++]);
                } else {
                    c = inCharBuf[inOff++];
                }
                if (skipLF) {
                    skipLF = false;
                    if (c == '\n') {
                        continue;
                    }
                }
                if (skipWhiteSpace) {
                    if (c == ' ' || c == '\t' || c == '\f') {
                        continue;
                    }
                    if (!appendedLineBegin && (c == '\r' || c == '\n')) {
                        continue;
                    }
                    skipWhiteSpace = false;
                    appendedLineBegin = false;
                }
                if (isNewLine) {
                    isNewLine = false;
                    if (c == '#' || c == '!') {
                        isCommentLine = true;
                        continue;
                    }
                }

                if (c != '\n' && c != '\r') {
                    lineBuf[len++] = c;
                    if (len == lineBuf.length) {
                        int newLength = lineBuf.length * 2;
                        if (newLength < 0) {
                            newLength = Integer.MAX_VALUE;
                        }
                        char[] buf = new char[newLength];
                        System.arraycopy(lineBuf, 0, buf, 0, lineBuf.length);
                        lineBuf = buf;
                    }
                    // flip the preceding backslash flag
                    if (c == '\\') {
                        precedingBackslash = !precedingBackslash;
                    } else {
                        precedingBackslash = false;
                    }
                } else {
                    // reached EOL
                    if (isCommentLine || len == 0) {
                        isCommentLine = false;
                        isNewLine = true;
                        skipWhiteSpace = true;
                        len = 0;
                        continue;
                    }
                    if (inOff >= inLimit) {
                        inLimit = inStream == null ? reader.read(inCharBuf) : inStream.read(inByteBuf);
                        inOff = 0;
                        if (inLimit <= 0) {
                            return len;
                        }
                    }
                    if (precedingBackslash) {
                        len -= 1;
                        // skip the leading whitespace characters in following line
                        skipWhiteSpace = true;
                        appendedLineBegin = true;
                        precedingBackslash = false;
                        if (c == '\r') {
                            skipLF = true;
                        }
                    } else {
                        return len;
                    }
                }
            }
        }
    }

    private static class SequenceReplacement {

        private final String regex;

        private final String to;

        public SequenceReplacement(String regex, String to) {
            this.regex = regex;
            this.to = to;
        }

        public String treat(String s) {
            return s.replaceAll(regex, to);
        }

    }

    private static final GwtPropertiesHelper INSTANCE = new GwtPropertiesHelper();

    public static GwtPropertiesHelper get() {
        return INSTANCE;
    }

    private final Map<String, Properties> cachedProperties;

    private final List<SequenceReplacement> sequenceReplacements;

    private GwtPropertiesHelper() {
        cachedProperties = new HashMap<>();
        sequenceReplacements = new ArrayList<>();
        initSequenceReplacements();

        AfterTestCallbackManager.get().registerCallback(this);
    }

    @Override
    public void afterTest() {
        // cachedProperties.clear();
        sequenceReplacements.clear();
        initSequenceReplacements();
    }

    public Properties getLocalizedProperties(String prefix, Locale locale) {
        if (locale != null) {
            prefix += "_" + locale.toString();
        }
        return getProperties(prefix);
    }

    public Properties getProperties(String path) {
        if (cachedProperties.containsKey(path)) {
            return cachedProperties.get(path);
        }
        String propertiesNameFile = "/" + path + ".properties";
        InputStream inputStream = this.getClass().getResourceAsStream(propertiesNameFile);
        if (inputStream == null) {
            cachedProperties.put(path, null);
            return null;
        }
        try {
            Properties properties = new Properties();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            load(properties, inputStreamReader);
            cachedProperties.put(path, properties);
            return properties;
        } catch (Exception e) {
            throw new GwtTestI18NException("Unable to load property file [" + path + "]", e);
        }
    }

    public void replaceSequence(String regex, String to) {
        sequenceReplacements.add(new SequenceReplacement(regex, to));
    }

    public String treatString(String string) {
        for (SequenceReplacement sequenceReplacement : sequenceReplacements) {
            string = sequenceReplacement.treat(string);
        }

        return string;
    }

    private void initSequenceReplacements() {
        // hardcoded to fix gwt "bug"
        sequenceReplacements.add(new SequenceReplacement("\\u00A0", " "));
    }

    /**
     * Method copied from JDK 1.6 Properties.load(Reader)..
     *
     * @param properties
     * @param reader
     * @throws IOException
     */
    private synchronized void load(Properties properties, Reader reader) throws IOException {
        load0(properties, new LineReader(reader));
    }

    private void load0(Properties properties, LineReader lr) throws IOException {
        char[] convtBuf = new char[1024];
        int limit;
        int keyLen;
        int valueStart;
        char c;
        boolean hasSep;
        boolean precedingBackslash;

        while ((limit = lr.readLine()) >= 0) {
            c = 0;
            keyLen = 0;
            valueStart = limit;
            hasSep = false;

            precedingBackslash = false;
            while (keyLen < limit) {
                c = lr.lineBuf[keyLen];
                // need check if escaped.
                if ((c == '=' || c == ':') && !precedingBackslash) {
                    valueStart = keyLen + 1;
                    hasSep = true;
                    break;
                } else if ((c == ' ' || c == '\t' || c == '\f') && !precedingBackslash) {
                    valueStart = keyLen + 1;
                    break;
                }
                if (c == '\\') {
                    precedingBackslash = !precedingBackslash;
                } else {
                    precedingBackslash = false;
                }
                keyLen++;
            }
            while (valueStart < limit) {
                c = lr.lineBuf[valueStart];
                if (c != ' ' && c != '\t' && c != '\f') {
                    if (!hasSep && (c == '=' || c == ':')) {
                        hasSep = true;
                    } else {
                        break;
                    }
                }
                valueStart++;
            }
            String key = loadConvert(lr.lineBuf, 0, keyLen, convtBuf);
            String value = loadConvert(lr.lineBuf, valueStart, limit - valueStart, convtBuf);
            properties.put(key, value);
        }
    }

    /*
     * Converts encoded &#92;uxxxx to unicode chars and changes special saved chars to their original
     * forms
     */
    private String loadConvert(char[] in, int off, int len, char[] convtBuf) {
        if (convtBuf.length < len) {
            int newLen = len * 2;
            if (newLen < 0) {
                newLen = Integer.MAX_VALUE;
            }
            convtBuf = new char[newLen];
        }
        char aChar;
        char[] out = convtBuf;
        int outLen = 0;
        int end = off + len;

        while (off < end) {
            aChar = in[off++];
            if (aChar == '\\') {
                aChar = in[off++];
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = in[off++];
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }
                    out[outLen++] = (char) value;
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    out[outLen++] = aChar;
                }
            } else {
                out[outLen++] = aChar;
            }
        }
        String line = new String(out, 0, outLen);

        // hack of the JDK 6 function
        return treatString(line);

    }

}
