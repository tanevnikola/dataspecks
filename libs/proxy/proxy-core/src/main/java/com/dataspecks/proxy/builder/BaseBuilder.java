package com.dataspecks.proxy.builder;

public class BaseBuilder<I> {
    private final I buildingInstance;

    protected BaseBuilder(I buildingInstance) {
        this.buildingInstance = buildingInstance;
    }

    protected I getBuildingIstance() {
        return buildingInstance;
    }
}
