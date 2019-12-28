package com.dataspecks.proxy.base.handler.interceptor.adapter;

import com.dataspecks.commons.exception.DException;
import com.dataspecks.proxy.base.exception.unchecked.DeadEndException;
import com.dataspecks.proxy.base.handler.interceptor.ResultInterceptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * The value adapter does exactly as the name suggests. It is extending the {@link ResultInterceptor} (so it can be
 * used as actual result interpreter) however it is not interested in the actual invocation. Instead it only serves to
 * adapt/convert values.
 *
 * @param <T> not the actual type of the value. It is used to make distinction between methods intended for Object[]
 *           v.s. Object
 */
public interface ValueAdapter<T> extends ResultInterceptor<T> {

    /**
     * Simply call the adapt method to adapt the arg value
     *
     * @param proxy the proxy instance that the method was invoked on
     * @param method a {@link Method} instance corresponding to the interface method invoked on the proxy instance.
     * @param arg the actual result as returned by an {@link java.lang.reflect.InvocationHandler}
     * @return adapted value
     * @throws Throwable ex
     */
    @Override
    default Object intercept(Object proxy, Method method, T arg) throws Throwable {
        return adapt(arg);
    }

    /**
     * The actual adapt contract
     *
     * @param val value to be adapted
     * @return the adapted value F(val)
     * @throws Throwable ex
     */
    Object adapt(T val) throws Throwable;

    /**
     * A pass-through {@link ValueAdapter}. It returns the same value passed to the adapter.
     */
    ValueAdapter<Object> PassThrough = arg -> arg;

    /**
     * Build a trivial {@link ValueAdapter} that simply returns the provided argument
     *
     * @param val this value will be the result of the call
     * @return same as the parameter val
     */
    static ValueAdapter<Object> trivial(Object val) {
        return _unused -> val;
    }

    /**
     * A {@link ValueAdapter} that calls the {@link Objects#toString(Object)} for the provided value
     */
    ValueAdapter<Object> ToString = Objects::toString;

    /**
     * A Dead-End {@link ValueAdapter} that raises a {@link DeadEndException}
     */
    ValueAdapter<Object> DeadEnd = arg -> {
        throw new DeadEndException("Dead End :( value adapter");
    };

    /**
     * A {@link ValueAdapter} that converts String to Integer.
     */
    ValueAdapter<Object> StringToInteger = arg -> {
        DException.argue(arg instanceof String,
                String.format("Expected type '%s', got '%s'", String.class, Optional.ofNullable(arg)
                        .map(o -> o.getClass().toString())
                        .orElse("null")));
        return Integer.parseInt((String)arg);
    };

    /**
     * A {@link ValueAdapter} that casts the input argument to the provided type.
     *
     * @param type type to cast to
     * @param <T> type to cast to
     * @return casted value
     */
    static <T> ValueAdapter<Object> castTo(Class<T> type) {
        return type::cast;
    }

    /**
     * A {@link ValueAdapter} that serializes a single value to ByteArray.
     */
    ValueAdapter<Object> Serialize = arg -> {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(arg);
        oos.flush();
        return bos.toByteArray();
    };

    /**
     * A {@link ValueAdapter} that deserializes a single value from ByteArray to an Object, it doesnt care what type
     * the object is.
     */
    ValueAdapter<Object> Deserialize = arg -> {
            DException.argue(arg instanceof byte[]);
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream((byte[])arg));
            return in.readObject();
    };

    /**
     * Build a multiple-value {@link ValueAdapter} using a single-value {@link ValueAdapter} for each of the provided
     * values
     *
     * @param adapter a single-value {@link ValueAdapter}
     * @return multiple-value {@link ValueAdapter}
     */
    static ValueAdapter<Object[]> using(ValueAdapter<Object> adapter) {
        return adapter::adapt;
    }

    ValueAdapter<Object[]> SerializeArray = using(Serialize);

    ValueAdapter<Object[]> DeserializeArray = using(Deserialize);

    /**
     * A pass-through multiple-value {@link ValueAdapter}
     */
    ValueAdapter<Object[]> PassThroughArray = using(PassThrough);

    /**
     * A multiple-value {@link ValueAdapter} that concatenates the provided arguments.
     */
    ValueAdapter<Object[]> Concatenate = concatenate("");

    /**
     * Build a multiple-value {@link ValueAdapter} that concatenates the provided arguments.
     *
     * @param delimiter the delimiter to be used for the concatenation
     * @return a multiple-value {@link ValueAdapter}
     */
    static ValueAdapter<Object[]> concatenate(String delimiter) {
        return concatenate("", "", delimiter);
    }

    /**
     * Build a multiple-value {@link ValueAdapter} that concatenates the provided arguments.
     *
     * @param delimiter the delimiter to be used for the concatenation
     * @param prefix the prefix to be used for the concatenation
     * @param suffix the suffix to be used for the concatenation
     * @return a multiple-value {@link ValueAdapter}
     */
    static ValueAdapter<Object[]> concatenate(String delimiter, String prefix, String suffix) {
        StringJoiner stringJoiner = new StringJoiner(delimiter, prefix, suffix);
        return val -> {
            Arrays.stream(val)
                    .map(Object::toString)
                    .forEach(stringJoiner::add);
            return stringJoiner.toString();
        };
    }
}