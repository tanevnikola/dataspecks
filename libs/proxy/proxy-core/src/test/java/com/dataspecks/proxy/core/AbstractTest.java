package com.dataspecks.proxy.core;

import com.dataspecks.commons.function.UnsafeRunnable;
import org.junit.Assert;

import java.util.UUID;

public abstract class AbstractTest {
    public static final String TESTCLASS_TESTVALUE_DEFAULTVALUE = UUID.randomUUID().toString();

    public interface TestInterface {
        String getTestValue();
        void setTestValue(String val);
        void rewireMe(Integer i, Boolean b, String s);
        void rewireMe(Integer i, String s);
        String rewireMe(Boolean b, String s);
    }

    public static class TestClass {
        private String testValue = TESTCLASS_TESTVALUE_DEFAULTVALUE;

        public String getTestValue() {
            return testValue;
        }

        public void setTestValue(String val) {
            this.testValue = testValue;
        }
    }

    public static class TestInterfaceImpl implements TestInterface {
        private String testValue;

        @Override
        public String getTestValue() {
            return testValue;
        }

        @Override
        public void setTestValue(String val) {
            this.testValue = val;
        }

        @Override
        public void rewireMe(Integer i, Boolean b, String s) {
            System.out.printf("i: '%s', b: '%s', s: '%s'\n", i, b, s);
        }

        @Override
        public void rewireMe(Integer i, String s) {
            System.out.printf("i: '%s', s: '%s'\n", i, s);

        }

        @Override
        public String rewireMe(Boolean b, String s) {
            System.out.printf("b: '%s', s: '%s'\n", b, s);
            return String.format("%s%s", b, s);
        }

    }

    public static <E extends Throwable> void logException(UnsafeRunnable<E> r) throws E {
        try {
            r.run();
        } catch (Throwable ex) {
            @SuppressWarnings("unchecked")
            E e = (E) ex;
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public static <E extends Throwable> void validateExceptionMessage(String expectedMsg, UnsafeRunnable<E> r) throws E {
        try {
            logException(r);
        } catch (Throwable ex) {
            @SuppressWarnings("unchecked")
            E e = (E) ex;
            Assert.assertEquals(e.getMessage(), expectedMsg);
            throw e;
        }
    }

}
