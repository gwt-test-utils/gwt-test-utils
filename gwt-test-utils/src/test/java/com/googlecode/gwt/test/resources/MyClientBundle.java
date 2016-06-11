package com.googlecode.gwt.test.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.impl.WindowImplIE.Resources;

public interface MyClientBundle extends CellTable.Resources, Resources {

    public static final MyClientBundle INSTANCE = GWT.create(MyClientBundle.class);

    @Source("css/myCssResource.css")
    public MyCssResource cssResource();

    @Source("textResourceXml.xml")
    public DataResource dataResource();

    public ImageResource doubleShouldThrowException();

    public ImageResource imageResource();

    @Source({"css/myCssResource.css", "css/addedStyles.css"})
    public MultipleFileCssResource multipleFileCssResource();

    @Source("root-classpath-img.png")
    public ImageResource rootClasspathImg();

    public TextResource textResourceTxt();

    @Source("textResourceXml.xml")
    public TextResource textResourceXml();

}
