package com.googlecode.gwt.test.spring;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Helper for gwt-test-utils / Spring integration. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
class GwtSpringHelper {

    public static Object findRpcServiceInSpringContext(ApplicationContext applicationContext,
                                                       Class<?> remoteServiceClass, String remoteServiceRelativePath) {

        Map<String, ?> beans = applicationContext.getBeansOfType(remoteServiceClass);

        switch (beans.size()) {

            case 0:
                return null;
            case 1:
                return beans.values().iterator().next();
            default:
                for (Object bean : beans.values()) {
                    RemoteServiceRelativePath annotation = bean.getClass().getAnnotation(
                            RemoteServiceRelativePath.class);

                    if (annotation != null && remoteServiceRelativePath.equals(annotation.value())) {
                        return bean;
                    }
                }

                return null;
        }
    }

    private GwtSpringHelper() {

    }

}
