package com.googlecode.gwt.test.internal.patchers;

import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

import java.net.URLDecoder;
import java.net.URLEncoder;

@PatchClass(target = "com.google.gwt.user.client.History$HistoryTokenEncoder")
public class HistoryTokenEncoderPatcher {

  @PatchMethod
  static String encode(Object encoder, String input) {
    return URLEncoder.encode(input);
  }

  @PatchMethod
  static String decode(Object encoder, String input) {
    return URLDecoder.decode(input);
  }
}
