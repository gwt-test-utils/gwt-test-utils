package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.*;
import com.googlecode.gwt.test.exceptions.GwtTestException;
import com.googlecode.gwt.test.exceptions.GwtTestJSONException;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.IOException;

@PatchClass(JSONParser.class)
class JSONParserPatcher {

    @PatchMethod
    static JSONValue evaluate(String json, boolean strict) {

        JsonParser jp = null;
        try {
            JsonFactory f = new JsonFactory();
            if (!strict) {
                f.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
                f.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
                f.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
                f.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
                f.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
                f.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
                f.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
            }
            jp = f.createJsonParser(json);
            JsonToken token = jp.nextToken();
            if(JsonToken.START_ARRAY == token)
            {
                JSONArray jsonArray = extractJSONArray(json, null, jp);
                return jsonArray;
            } else
            {
                JSONObject jsonObject = extractJSONObject(json, jp);
                return jsonObject;
            }
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

    @PatchMethod
    static JavaScriptObject initTypeMap() {
        return null;
    }

    private static JSONArray extractJSONArray(String json, JsonToken currentToken, JsonParser jp)
            throws GwtTestJSONException, IOException {
        JSONArray jsonArray = new JSONArray();
        int i = 0;

        while ((currentToken = jp.nextToken()) != JsonToken.END_ARRAY) {
            JSONValue value = extractNextValue(json, currentToken, jp);
            jsonArray.set(i++, value);
        }

        return jsonArray;
    }

    private static JSONObject extractJSONObject(String json, JsonParser jp) throws IOException,
            JsonParseException {
        JSONObject jsonObject = new JSONObject();
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            JSONValue value = extractNextValue(json, jp.nextToken(), jp);
            jsonObject.put(fieldName, value);
        }
        return jsonObject;
    }

    private static JSONValue extractNextValue(String json, JsonToken currentToken, JsonParser jp)
            throws GwtTestJSONException, IOException {
        JSONValue fieldValue;
        switch (currentToken) {
            case VALUE_NULL:
                fieldValue = JSONNull.getInstance();
                break;
            case VALUE_STRING:
                fieldValue = new JSONString(jp.getText());
                break;
            case VALUE_NUMBER_INT:
                fieldValue = new JSONNumber(jp.getIntValue());
                break;
            case VALUE_NUMBER_FLOAT:
                fieldValue = new JSONNumber(jp.getDoubleValue());
                break;
            case VALUE_TRUE:
                fieldValue = JSONBoolean.getInstance(true);
                break;
            case VALUE_FALSE:
                fieldValue = JSONBoolean.getInstance(false);
                break;
            case START_ARRAY:
                fieldValue = extractJSONArray(json, currentToken, jp);
                break;
            case START_OBJECT:
                fieldValue = extractJSONObject(json, jp);
                break;
            default:
                throw new GwtTestJSONException("Error while parsing JSON string '" + json
                        + "' : gwt-test-utils does not handle token '" + jp.getText() + "', line "
                        + jp.getTokenLocation().getLineNr() + " column "
                        + jp.getTokenLocation().getColumnNr());
        }

        return fieldValue;
    }

}
