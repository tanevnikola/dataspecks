package com.dataspecks.proxy.core.handler;

import com.dataspecks.builder.Builder;
import com.dataspecks.builder.GenericBuilder;
import com.dataspecks.commons.exception.DException;
import com.dataspecks.commons.reflection.Methods;
import com.dataspecks.proxy.core.builder.contract.handler.RedirectInvocationHandlerBuildContract;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Concrete builder
 * @param <T> proxy type
 * @param <U> instance type
 */
public class RedirectInvocationHandlerBuilder<T, U>
        extends GenericBuilder<RedirectInvocationHandler<T, U>>
        implements Builder<RedirectInvocationHandler<T, U>>, RedirectInvocationHandlerBuildContract<T, RedirectInvocationHandlerBuilder<T, U>> {

    public RedirectInvocationHandlerBuilder(U targetI) {
        super(RedirectInvocationHandler::new);
        configure(rIHandler -> rIHandler.setTargetI(targetI));
    }

    public RedirectInvocationHandlerBuilder<T, U> setMethod(String name, Class<?>... args) {
        configure(rIHandler -> rIHandler.setTargetM(Methods.lookup(rIHandler.getTargetI().getClass(), name, args)));
        return this;
    }

    public RedirectInvocationHandlerBuilder<T, U> setMethod(Method method) {
        configure(rIHandler -> rIHandler.setTargetM(method));
        return this;
    }

    /**
     * Perform instance validation. Both target instance and target method must no be null. Additionally the target
     * method's declaring class my be the target instance class.
     *
     * @param rIHandler {@link RedirectInvocationHandler}
     * @return {@link RedirectInvocationHandler}
     */
    @Override
    protected RedirectInvocationHandler<T, U> validate(RedirectInvocationHandler<T, U> rIHandler) {
        DException.argue(Objects.nonNull(rIHandler.getTargetI()), "Target instance cannot be null");
        DException.argue(Objects.nonNull(rIHandler.getTargetM()), "Target method cannot be null");
        DException.argue(rIHandler.getTargetM().getDeclaringClass().equals(rIHandler.getTargetI().getClass()),
                "Target target method's declaring class is different than the target's instance class");
        return super.validate(rIHandler);
    }

//    public static <T, U> RedirectInvocationHandlerBuilder<T, U> create(U targetI) {
//        return new RedirectInvocationHandlerBuilder<T, U>(targetI);
//    }
}
