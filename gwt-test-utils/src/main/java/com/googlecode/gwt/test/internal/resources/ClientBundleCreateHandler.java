package com.googlecode.gwt.test.internal.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.googlecode.gwt.test.GwtCreateHandler;

/**
 * Class in charge of the instanciation of all {@link ClientBundle} sub-interfaces through deferred
 * binding. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
@SuppressWarnings("unchecked")
public class ClientBundleCreateHandler implements GwtCreateHandler {

    public Object create(Class<?> classLiteral) throws Exception {
        if (!ClientBundle.class.isAssignableFrom(classLiteral)) {
            return null;
        }
        return ClientBundleProxyFactory.getFactory((Class<? extends ClientBundle>) classLiteral).createProxy();
    }

}
