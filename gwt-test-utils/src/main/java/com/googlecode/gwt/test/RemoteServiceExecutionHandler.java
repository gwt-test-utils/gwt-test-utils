package com.googlecode.gwt.test;

import java.lang.reflect.Method;

/**
 * An interface to provide callbacks whenever an RPC invocation is processed.
 *
 * @author Gael Lazzari
 */
public interface RemoteServiceExecutionHandler {

    /**
     * Callback to be notified after RPC method invocation (server-side).
     *
     * @param rpcMethod The invoked method.
     * @param result    The method result (null for void return type).
     */
    void afterRpcMethodExecution(Method rpcMethod, Object result);

    /**
     * Callback to be notified after RPC method parameters serialization (client-side). serialization
     * simulation.
     *
     * @param rpcMethod         The invoked method.
     * @param requestParameters The serialized parameters.
     */
    void afterRpcRequestSerialization(Method rpcMethod, Object[] requestParameters);

    /**
     * Callback to be notified after RPC method result serialization (server-side).
     *
     * @param rpcMethod The invoked method.
     * @param response  The method result (null for void return type).
     */
    void afterRpcResponseSerialization(Method rpcMethod, Object response);

    /**
     * Callback to be notified before RPC method invocation (server-side).
     *
     * @param rpcMethod       The invoked method.
     * @param inputParameters Invocation's parameters.
     */
    void beforeRpcMethodExecution(Method rpcMethod, Object[] inputParameters);

    /**
     * Callback to be notified before RPC method parameters serialization (client-side).
     *
     * @param rpcMethod         The invoked method.
     * @param requestParameters The parameters to be serialized.
     */
    void beforeRpcRequestSerialization(Method rpcMethod, Object[] requestParameters);

    /**
     * Callback to be notified before RPC method result serialization (server-side).
     *
     * @param rpcMethod The invoked method.
     * @param response  The method result (null for void return type).
     */
    void beforeRpcResponseSerialization(Method rpcMethod, Object response);

}
