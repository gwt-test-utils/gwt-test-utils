package com.googlecode.gwt.test.rpc;

/**
 * <p>
 * An interface which exposes one callback method to be called in order to simulate GWT-RPC
 * serialization. It is called when simulating the serialization of RPC method parameters from the
 * client and when returning the method result from the server.
 * </p>
 * <p>
 * <p>
 * The used instance of GwtRpcSerializerHandler is specified by
 * {@link RemoteServiceCreateHandler#getSerializerHandler()}, which can be overriden.
 * </p>
 *
 * @author Gael Lazzari
 */
public interface GwtRpcSerializerHandler {

    /**
     * The callback method which is called during serialization form the client to the server and
     * from the server back to the client.
     *
     * @param <T> Type type of the object to serialize.
     * @param o   The object to serialized
     * @return The new instance of the object, after its serialization.
     * @throws Exception If any error occurs during the serialization process.
     */
    public <T> T serializeUnserialize(T o) throws Exception;

}
