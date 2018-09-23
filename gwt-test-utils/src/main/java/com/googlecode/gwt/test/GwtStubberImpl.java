package com.googlecode.gwt.test;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.gwt.test.mockito.GwtStubber;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

class GwtStubberImpl extends StubberImpl implements GwtStubber {

    public GwtStubberImpl(Strictness strictness) {
        super(strictness);
    }

    private static class FailureAnswer<T> implements Answer<T> {

        private final Throwable result;

        public FailureAnswer(Throwable result) {
            this.result = result;
        }

        @SuppressWarnings("unchecked")
        public T answer(InvocationOnMock invocation) {
            Object[] arguments = invocation.getArguments();
            AsyncCallback<Object> callback = (AsyncCallback<Object>) arguments[arguments.length - 1];
            callback.onFailure(result);
            return null;
        }

    }

    private static class SuccessAnswer<T> implements Answer<T> {

        private final T result;

        public SuccessAnswer(T result) {
            this.result = result;
        }

        @SuppressWarnings("unchecked")
        public T answer(InvocationOnMock invocation) {
            Object[] arguments = invocation.getArguments();
            AsyncCallback<Object> callback = (AsyncCallback<Object>) arguments[arguments.length - 1];
            callback.onSuccess(result);
            return null;
        }

    }

    public <T> GwtStubber doFailureCallback(final Throwable exception) {
        doAnswer(new FailureAnswer<Object>(exception));
        return this;
    }

    public <T> GwtStubber doSuccessCallback(T object) {
        doAnswer(new SuccessAnswer<Object>(object));
        return this;
    }

}
