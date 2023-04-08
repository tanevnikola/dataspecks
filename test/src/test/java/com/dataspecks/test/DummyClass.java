package com.dataspecks.test;

public class DummyClass implements DummyInterface {
    @Override
    public Integer getA() {
        return 123;
    }

    @Override
    public String foo(Integer integer) {
        return String.valueOf(integer);
    }
}
