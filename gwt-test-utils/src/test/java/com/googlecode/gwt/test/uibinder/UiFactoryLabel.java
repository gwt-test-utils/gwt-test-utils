package com.googlecode.gwt.test.uibinder;

import com.google.gwt.user.client.ui.Label;

public class UiFactoryLabel extends Label {

   private String customText;
   final String uiFactoryString;

   UiFactoryLabel(String uiFactoryString) {
      this.uiFactoryString = uiFactoryString;
   }

   public String getCustomText() {
      return customText;
   }

   public void setCustomText(String customText) {
      this.customText = customText;
   }

}
