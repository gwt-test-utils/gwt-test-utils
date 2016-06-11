package com.googlecode.gwt.test.internal.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.ext.DefaultExtensions;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.ImageBundle.Resource;
import com.google.gwt.user.client.ui.impl.ClippedImagePrototype;
import com.googlecode.gwt.test.GwtCreateHandler;
import com.googlecode.gwt.test.exceptions.GwtTestResourcesException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Class in charge of the instanciation of all {@link ImageBundle} sub-interfaces through deferred
 * binding. <strong>For internal use only.</strong>
 *
 * @author Gael Lazzari
 */
@SuppressWarnings("deprecation")
public class ImageBundleCreateHandler implements GwtCreateHandler {

    private static final String[] IMAGE_DEFAULT_EXTENSIONS = ImageResource.class.getAnnotation(
            DefaultExtensions.class).value();

    public Object create(Class<?> classLiteral) throws Exception {
        if (!ImageBundle.class.isAssignableFrom(classLiteral)) {
            return null;
        }

        return generateImageWrapper(classLiteral);
    }

    private Object generateImageWrapper(Class<?> clazz) {
        InvocationHandler ih = new InvocationHandler() {

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getReturnType() == AbstractImagePrototype.class) {
                    String url = getImageUrl(method);
                    return new ClippedImagePrototype(url, 0, 0, 0, 0);
                }
                throw new GwtTestResourcesException("Not managed return type for image bundle : "
                        + method.getReturnType().getSimpleName());
            }

            private String computeFileSimpleName(Method method) {
                String packagePath = method.getDeclaringClass().getPackage().getName().replaceAll(
                        "\\.", "/");

                String relativePath = packagePath + "/" + method.getName();

                for (String extension : IMAGE_DEFAULT_EXTENSIONS) {
                    String possiblePath = relativePath + extension;
                    if (this.getClass().getResource("/" + possiblePath) != null) {
                        return method.getName() + extension;
                    }
                }

                // should never happened
                throw new GwtTestResourcesException("Cannot find an image with path relative to '"
                        + relativePath + "'");
            }

            private String getImageUrl(Method method) {
                String fileName;
                Resource resource = method.getAnnotation(Resource.class);
                if (resource != null) {
                    fileName = resource.value();
                } else {
                    fileName = computeFileSimpleName(method);
                }

                return GWT.getModuleBaseURL() + fileName;
            }

        };
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, ih);
    }
}
