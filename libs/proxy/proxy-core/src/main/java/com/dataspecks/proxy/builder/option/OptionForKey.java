package com.dataspecks.proxy.builder.option;

public interface OptionForKey<B, K> extends Option {
    B forKey(K m);
}