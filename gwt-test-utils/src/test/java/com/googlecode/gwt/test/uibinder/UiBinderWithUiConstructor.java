package com.googlecode.gwt.test.uibinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class UiBinderWithUiConstructor extends Composite{

	private static UiBinderWithUiConstructorUiBinder uiBinder = GWT
			.create(UiBinderWithUiConstructorUiBinder.class);
	
	@UiField
	UiConstructorWidget myWidget;

	interface UiBinderWithUiConstructorUiBinder extends
			UiBinder<Widget, UiBinderWithUiConstructor> {
	}

	public UiBinderWithUiConstructor() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
