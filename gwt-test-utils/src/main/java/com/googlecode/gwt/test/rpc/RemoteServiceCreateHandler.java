package com.googlecode.gwt.test.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.exceptions.GwtTestRpcException;
import com.googlecode.gwt.test.internal.GwtClassPool;
import com.googlecode.gwt.test.utils.GwtReflectionUtils;
import javassist.CtClass;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.bytecode.annotation.StringMemberValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * <p>
 * The {@link GwtCreateHandler} class used to create {@link RemoteService} sub-interface instances.
 * It returns an asynchrone proxy of the service and handles the simulation of GWT-RPC serialization
 * through a {@link GwtRpcInvocationHandler} and the exception management through a
 * {@link GwtRpcExceptionHandler}.
 * </p>
 * <p>
 * <p>
 * Before delegating the remote service object instantiation to the abstract
 * {@link RemoteServiceCreateHandler#findService(Class, String)} method, this class ensure that a
 * corresponding async interface is available for the RemoteService subinterface to create.
 * </p>
 *
 * @author Bertrand Paquet
 * @author Gael Lazzari
 */
public abstract class RemoteServiceCreateHandler implements GwtCreateHandler {

    private class DefaultGwtRpcExceptionHandler implements GwtRpcExceptionHandler {

        public void handle(Throwable t, AsyncCallback<?> callback) {
            callback.onFailure(t);
        }

    }

    private class DefaultGwtRpcSerializerHandler implements GwtRpcSerializerHandler {

        public <T> T serializeUnserialize(T o) throws Exception {
            return DeepCopy.copy(o);
        }

    }

    private static final Logger logger = LoggerFactory.getLogger(RemoteServiceCreateHandler.class);

    private final GwtRpcExceptionHandler exceptionHandler;
    private final GwtRpcSerializerHandler serializerHander;

    public RemoteServiceCreateHandler() {
        exceptionHandler = getExceptionHandler();
        serializerHander = getSerializerHandler();
    }

    public Object create(Class<?> classLiteral) throws Exception {

        if (!RemoteService.class.isAssignableFrom(classLiteral)) {
            return null;
        }

        String className = classLiteral.getName();
        logger.debug("Try to create Remote service class " + className);

        String asyncName = className + "Async";
        String relativePath = getRemoveServiceRelativePath(classLiteral);
        Class<?> asyncClazz = GwtReflectionUtils.getClass(asyncName);
        if (asyncClazz == null) {
            throw new GwtTestRpcException("Remote serivce Async class not found : " + asyncName);
        }
        logger.debug("Searching remote service implementing " + className);

        // try to find
        Object service = findService(classLiteral, relativePath);

        if (service == null) {
            return null;
        }

        GwtRpcInvocationHandler handler = new GwtRpcInvocationHandler(asyncClazz, service,
                exceptionHandler, serializerHander);

        return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{asyncClazz}, handler);
    }

    /**
     * Method which actually is responsible for getting the {@link RemoteService} sub-interface
     * implementation.
     *
     * @param remoteServiceClass        {@link RemoteService} sub-interface to get an implementation.
     * @param remoteServiceRelativePath the associated relative path, which is provided in
     *                                  {@link RemoteServiceRelativePath} annotation.
     * @return The corresponding remote service implementation.
     */
    protected abstract Object findService(Class<?> remoteServiceClass,
                                          String remoteServiceRelativePath);

    /**
     * Specify the handler to use to handle GWT-RPC errors.
     *
     * @return The handler to use to handle GWT-RPC errors.
     */
    protected GwtRpcExceptionHandler getExceptionHandler() {
        return new DefaultGwtRpcExceptionHandler();
    }

    /**
     * Specify the handler to use to simulate the GWT-RPC serialization
     *
     * @return The handler to use to simulate the GWT-RPC serialization.
     */
    protected GwtRpcSerializerHandler getSerializerHandler() {
        return new DefaultGwtRpcSerializerHandler();
    }

    private String getRemoveServiceRelativePath(Class<?> clazz) {
        CtClass ctClass = GwtClassPool.getCtClass((clazz));
        Object[] annotations = ctClass.getAvailableAnnotations();
        for (Object o : annotations) {
            if (Proxy.isProxyClass(o.getClass())) {
                AnnotationImpl annotation = (AnnotationImpl) Proxy.getInvocationHandler(o);
                if (annotation.getTypeName().equals(RemoteServiceRelativePath.class.getName())) {
                    return ((StringMemberValue) annotation.getAnnotation().getMemberValue("value")).getValue();
                }
            }
        }
        throw new GwtTestRpcException("Cannot find the '@"
                + RemoteServiceRelativePath.class.getSimpleName()
                + "' annotation on RemoteService interface '" + clazz.getName() + "'");
    }

}
