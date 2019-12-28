package com.dataspecks.resman.local.a;

import org.junit.Test;

public class LocalRepositoryTest {

    <T> T[] polluter(T... things) {
        return things;
    }
    <T> T[] caster(T thing) {
        return polluter(thing);
    }

    @Test
    public void testPlayground() {
        Object[] a = caster(3);
    }
}
