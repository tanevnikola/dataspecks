package com.dataspecks.proxy.core.handler.interceptor;

import com.dataspecks.proxy.core.handler.InvocationHandler;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * This specialized invocation handler can be configured with {@link ArgumentsInterceptor} and {@link ResultInterceptor}
 * that will be called before/after the actual {@link InvocationHandler} invocation (respectively). The actual invocation
 * handler that is going to be invoked must be additionally provided.
 *
 * {@link ArgumentsInterceptor} result will be provided as arguments to the actual invocation and the result returned
 * by the invocation will be passed through the {@link ResultInterceptor} before returned. This allows modification of
 * the type or number of arguments for the actual invocation as well as changing the result before it is returned.
 *
 * @param <T> proxy type
 */
final class InterceptableInvocationHandler<T> implements InvocationHandler<T> {
    private InvocationHandler<T> iHandler = InvocationHandler.DeadEnd();
    private ArgumentsInterceptor aInterceptor = null;
    private ResultInterceptor<Object> rInterceptor = null;

    /**
     * get invocation handler
     * @return {@link InvocationHandler}
     */
    public InvocationHandler<T> getiHandler() {
        return iHandler;
    }

    /**
     * set invocation handler
     * @param iHandler {@link InvocationHandler}
     */
    public void setiHandler(InvocationHandler<T> iHandler) {
        this.iHandler = iHandler;
    }

    /**
     * get arguments interceptor
     * @return {@link ArgumentsInterceptor}
     */
    public ArgumentsInterceptor getaInterceptor() {
        return aInterceptor;
    }

    /**
     * set arguments interceptor
     * @param aInterceptor {@link ArgumentsInterceptor}
     */
    public void setaInterceptor(ArgumentsInterceptor aInterceptor) {
        this.aInterceptor = aInterceptor;
    }

    /**
     * get result interceptor
     * @return {@link ResultInterceptor}
     */
    public ResultInterceptor<Object> getrInterceptor() {
        return rInterceptor;
    }

    /**
     * set result interceptor
     * @param rInterceptor {@link ResultInterceptor}
     */
    public void setrInterceptor(ResultInterceptor<Object> rInterceptor) {
        this.rInterceptor = rInterceptor;
    }

    /**
     * Implementation of the {@link java.lang.reflect.InvocationHandler#invoke(Object, Method, Object[])} contract
     * See the class description for more info.
     *
     * @param proxy the proxy instance that the method was invoked on
     * @param method a {@link Method} instance corresponding to the interface method invoked on the proxy instance.
     * @param args the arguments passed when the method was invoked
     *
     * @return result of the invocation
     * @throws Throwable ex
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ArgumentsInterceptor aInterceptor = Optional.ofNullable(this.aInterceptor)
                .orElse(ArgumentsInterceptor.PassThrough);
        ResultInterceptor<Object> rInterceptor = Optional.ofNullable(this.rInterceptor)
                .orElse(ResultInterceptor.PassThrough);

        return rInterceptor.intercept(proxy, method,
                iHandler.invoke(proxy, method, aInterceptor.intercept(proxy, method, args)));
    }

}
