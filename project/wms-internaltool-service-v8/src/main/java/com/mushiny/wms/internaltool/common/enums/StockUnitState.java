package com.mushiny.wms.internaltool.common.enums;

public enum StockUnitState {

    INVENTORY("Inventory"),
    DAMAGE("Damage"),
    PENDING("Pending");

    private String name;//定义自定义的变量

    StockUnitState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
