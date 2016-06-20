package com.googlecode.gwt.test.json;

import com.google.gwt.core.client.JsonUtils;
import com.googlecode.gwt.test.GwtTestTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilsTest extends GwtTestTest {

    @Test
    public void safeEval() {
        // Test
        MyJsonOverlay jso = JsonUtils.safeEval("{myString: 'json string', \"myDouble\": 3, myFloat: 3.1415, \"myBool\": true, 'myMixedArray': [\"John Locke\", \"Ben Linus\", 23, 42, true], myNumberArray: null, myObject: {myString: 'child object string', myNumberArray: [2.2, 3.3] } }");

        // Then
        assertThat(jso.getMyString()).isEqualTo("json string");
        assertThat(jso.getMyDouble()).isEqualTo(3);
        assertThat(jso.getMyFloat()).isEqualTo(3.1415f);
        assertThat(jso.getMyBool()).isTrue();
        assertThat(jso.getMyMixedArray().getString(0)).isEqualTo("John Locke");
        assertThat(jso.getMyMixedArray().getString(1)).isEqualTo("Ben Linus");
        assertThat(jso.getMyMixedArray().getNumber(2)).isEqualTo(23);
        assertThat(jso.getMyMixedArray().getNumber(3)).isEqualTo(42);
        assertThat(jso.getMyMixedArray().getBoolean(4)).isTrue();
        assertThat(jso.getMyNumberArray()).isNull();

        MyJsonOverlay child = jso.getMyObject();
        assertThat(child.getMyString()).isEqualTo("child object string");
        assertThat(child.getMyNumberArray().get(0)).isEqualTo(2.2);
        assertThat(child.getMyNumberArray().get(1)).isEqualTo(3.3);
    }

    @Test
    public void unsafeEval() {
        // Test
        MyJsonOverlay jso = JsonUtils.unsafeEval("{myString: 'json string', \"myDouble\": 3, myFloat: 3.1415, \"myBool\": true, 'myMixedArray': [\"John Locke\", \"Ben Linus\", 23, 42, true], myNumberArray: null, myObject: {myString: 'child object string', myNumberArray: [2.2, 3.3] } }");

        // Then
        assertThat(jso.getMyString()).isEqualTo("json string");
        assertThat(jso.getMyDouble()).isEqualTo(3);
        assertThat(jso.getMyFloat()).isEqualTo(3.1415f);
        assertThat(jso.getMyBool()).isTrue();
        assertThat(jso.getMyMixedArray().getString(0)).isEqualTo("John Locke");
        assertThat(jso.getMyMixedArray().getString(1)).isEqualTo("Ben Linus");
        assertThat(jso.getMyMixedArray().getNumber(2)).isEqualTo(23);
        assertThat(jso.getMyMixedArray().getNumber(3)).isEqualTo(42);
        assertThat(jso.getMyMixedArray().getBoolean(4)).isTrue();
        assertThat(jso.getMyNumberArray()).isNull();

        MyJsonOverlay child = jso.getMyObject();
        assertThat(child.getMyString()).isEqualTo("child object string");
        assertThat(child.getMyNumberArray().get(0)).isEqualTo(2.2);
        assertThat(child.getMyNumberArray().get(1)).isEqualTo(3.3);
    }

}
