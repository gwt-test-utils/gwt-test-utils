package com.googlecode.gwt.test.deferred;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import java.io.PrintWriter;
import java.util.HashMap;

public class MyGenerator extends Generator {

    @Override
    public String generate(TreeLogger logger, GeneratorContext context, String typeName)
            throws UnableToCompleteException {

        String packageName = "com.slazzer";
        String className = "MyGeneratedClass";

        ClassSourceFileComposerFactory factory = new ClassSourceFileComposerFactory(packageName,
                className);

        PrintWriter pw = context.tryCreate(logger, packageName, className);
        if (pw != null) {
            factory.addImplementedInterface(IGenerateWith.class.getCanonicalName());
            factory.addImport(GWT.class.getCanonicalName());
            factory.addImport(JavaScriptObject.class.getCanonicalName());
            factory.addImport(HashMap.class.getCanonicalName());
            SourceWriter writer = factory.createSourceWriter(context, pw);
            writer.println("public String getMessage() {");
            writer.println("   return \"generated with MyGenerator class\";");
            writer.println("}");
            writer.commit(logger);
        }

        return factory.getCreatedClassName();
    }

}
