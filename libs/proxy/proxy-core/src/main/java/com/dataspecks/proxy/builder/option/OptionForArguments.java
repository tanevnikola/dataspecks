package com.dataspecks.proxy.builder.option;

public interface OptionForArguments<B> extends Option {
    B forArguments(Integer... indexes);
}
