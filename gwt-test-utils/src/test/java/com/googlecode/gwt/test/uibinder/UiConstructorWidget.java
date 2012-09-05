package com.googlecode.gwt.test.uibinder;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class UiConstructorWidget extends Composite {
	public static enum Type {
		T1, T2;
	}
	
	protected int size;
	protected Type type;
	
	@UiConstructor
	public UiConstructorWidget(int size, Type type){
		this.size = size;
		this.type = type;
		
		initWidget(new HTML(String.valueOf(this.size) + ":" ));
	}
	
	public void setSize(int size) {
		// setter required when setSize is overloaded
		this.size = size;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
}
