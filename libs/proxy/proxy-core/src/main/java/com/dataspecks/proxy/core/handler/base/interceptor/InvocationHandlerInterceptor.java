package com.dataspecks.proxy.core.handler.base.interceptor;

import com.dataspecks.proxy.utils.handler.InvocationHandlers;
import com.dataspecks.proxy.utils.handler.base.interceptor.ArgumentsInterceptors;
import com.dataspecks.proxy.utils.handler.base.interceptor.ResultInterceptors;

import java.lang.reflect.InvocationHandler;
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
 */
final class InvocationHandlerInterceptor implements InvocationHandler {
    private InvocationHandler invocationHandler = InvocationHandlers.DEAD_END;
    private ArgumentsInterceptor argumentsInterceptor = null;
    private ResultInterceptor resultInterceptor = null;

    public InvocationHandler getInvocationHandler() {
        return invocationHandler;
    }

    public void setInvocationHandler(InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    public ArgumentsInterceptor getArgumentsInterceptor() {
        return argumentsInterceptor;
    }

    public void setArgumentsInterceptor(ArgumentsInterceptor argumentsInterceptor) {
        this.argumentsInterceptor = argumentsInterceptor;
    }

    public ResultInterceptor getResultInterceptor() {
        return resultInterceptor;
    }

    public void setResultInterceptor(ResultInterceptor resultInterceptor) {
        this.resultInterceptor = resultInterceptor;
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
        ArgumentsInterceptor aInterceptor = Optional.ofNullable(this.argumentsInterceptor)
                .orElse(ArgumentsInterceptors.PassThrough);
        ResultInterceptor rInterceptor = Optional.ofNullable(this.resultInterceptor)
                .orElse(ResultInterceptors.PassThrough);

        Object[] args_final = aInterceptor.intercept(proxy, method, args);
        Object result = invocationHandler.invoke(proxy, method, args_final);
        return rInterceptor.intercept(proxy, method, result, args_final);
    }

}