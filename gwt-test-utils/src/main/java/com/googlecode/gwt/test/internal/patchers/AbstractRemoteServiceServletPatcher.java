package com.googlecode.gwt.test.internal.patchers;

import com.google.gwt.user.server.rpc.AbstractRemoteServiceServlet;
import com.googlecode.gwt.test.exceptions.GwtTestRpcException;
import com.googlecode.gwt.test.internal.GwtConfig;
import com.googlecode.gwt.test.patchers.InitMethod;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.rpc.ServletMockProvider;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@PatchClass(AbstractRemoteServiceServlet.class)
public class AbstractRemoteServiceServletPatcher {

    public static Method currentCalledMethod;

    public static ServletMockProvider ensureServletMockProvider(
            AbstractRemoteServiceServlet servlet, String methodName) {
        ServletMockProvider servletMockProvider = GwtConfig.get().getModuleRunner().getServletMockProvider();
        if (servletMockProvider == null) {
            throw new GwtTestRpcException("Illegal call to " + servlet.getClass().getName() + "."
                    + methodName + " : You have to set a valid "
                    + ServletMockProvider.class.getSimpleName() + " instance through "
                    + GwtConfig.get().getModuleRunner().getClass().getSimpleName()
                    + ".setServletMockProvider(..) method");
        }

        return servletMockProvider;
    }

    @InitMethod
    static void addMockedGetServletConfigMethod(CtClass c) throws CannotCompileException {

        StringBuilder sb = new StringBuilder();
        sb.append("public javax.servlet.ServletConfig getServletConfig() { return ");
        sb.append(AbstractRemoteServiceServletPatcher.class.getName()).append(
                ".ensureServletMockProvider(this, \"getServletConfig()\").getMockedConfig(this); }");

        CtMethod m = CtMethod.make(sb.toString(), c);
        c.addMethod(m);
    }

    @PatchMethod
    static HttpServletRequest getThreadLocalRequest(AbstractRemoteServiceServlet servlet) {

        return ensureServletMockProvider(servlet, "getThreadLocalRequest()").getMockedRequest(
                servlet, currentCalledMethod);
    }

    @PatchMethod
    static HttpServletResponse getThreadLocalResponse(AbstractRemoteServiceServlet servlet) {

        return ensureServletMockProvider(servlet, "getThreadLocalResponse()").getMockedResponse(
                servlet, currentCalledMethod);
    }

}
