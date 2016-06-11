package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.exceptions.GwtTestJSONException;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.JavaScriptObjects;
import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.IOException;
import java.util.List;

@PatchClass(JsonUtils.class)
class JsonUtilsPatcher {

    private static JsonFactory factory;

    @PatchMethod
    public static <T extends JavaScriptObject> T safeEval(String json) {
        return eval(json);
    }

    @PatchMethod
    public static <T extends JavaScriptObject> T unsafeEval(String json) {
        return eval(json);
    }

    @PatchMethod
    static String escapeValue(String toEscape) {
        return "\"" + StringEscapeUtils.escapeEcmaScript(toEscape) + "\"";
    }

    @PatchMethod
    static boolean hasJsonParse() {
        return true;
    }

    @PatchMethod
    static JavaScriptObject initEscapeTable() {
        return null;
    }

    private static <T extends JavaScriptObject> T eval(String json) {
        JsonParser jp = null;

        try {
            jp = getFactory().createJsonParser(json);
            jp.nextToken(); // will return JsonToken.START_OBJECT (verify?)
            return extractJso(json, jp.getCurrentToken(), jp).<T>cast();
        } catch (Exception e) {
            if (e instanceof GwtTestException) {
                throw (GwtTestException) e;
            }
            throw new GwtTestJSONException("Error while parsing JSON string '" + json + "'", e);
        } finally {
            if (jp != null) {
                try {
                    // ensure resources get cleaned up timely and properly
                    jp.close();
                } catch (IOException e) {
                    // should never happen
                }
            }
        }
    }

    private static JavaScriptObject extractArray(String json, JsonToken currentToken, JsonParser jp)
            throws GwtTestJSONException, IOException {

        JavaScriptObject jsArray = JavaScriptObject.createArray();
        List<Object> list = JsArrayHelper.getWrappedList(jsArray);

        while ((currentToken = jp.nextToken()) != JsonToken.END_ARRAY) {
            Object value = extractValue(json, jp.getCurrentToken(), jp);
            list.add(value);
        }

        return jsArray;
    }

    private static JavaScriptObject extractJso(String json, JsonToken currentToken, JsonParser jp)
            throws JsonParseException, GwtTestJSONException, IOException {

        JavaScriptObject jso = JavaScriptObject.createObject();

        while ((currentToken = jp.nextToken()) != JsonToken.END_OBJECT) {
            JavaScriptObjects.setProperty(jso, jp.getCurrentName(),
                    extractValue(json, jp.nextToken(), jp));
        }

        return jso;
    }

    private static Object extractValue(String json, JsonToken currentToken, JsonParser jp)
            throws GwtTestJSONException, IOException {
        switch (currentToken) {
            case VALUE_NULL:
                return null;
            case VALUE_STRING:
                return jp.getText();
            case VALUE_NUMBER_INT:
                return jp.getIntValue();
            case VALUE_NUMBER_FLOAT:
                return jp.getFloatValue();
            case VALUE_TRUE:
                return true;
            case VALUE_FALSE:
                return false;
            case START_ARRAY:
                return extractArray(json, currentToken, jp);
            case START_OBJECT:
                return extractJso(json, currentToken, jp);
            default:
                throw new GwtTestJSONException("Error while parsing JSON string '" + json
                        + "' : gwt-test-utils does not handle token '" + jp.getText() + "', line "
                        + jp.getTokenLocation().getLineNr() + " column "
                        + jp.getTokenLocation().getColumnNr());
        }
    }

    private static JsonFactory getFactory() {
        if (factory == null) {
            factory = new JsonFactory();
            factory.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            factory.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            factory.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            factory.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
            factory.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            factory.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            factory.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        }

        return factory;
    }

}
