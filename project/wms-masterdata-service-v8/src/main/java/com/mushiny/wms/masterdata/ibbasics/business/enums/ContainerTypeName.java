package com.mushiny.wms.masterdata.ibbasics.business.enums;

public enum ContainerTypeName {

    RECEIVE("IB_Receive"),
    DAMAGE("Damage"),
    CUBI_SCAN("Cubi_Scan");

    private String name;//定义自定义的变量

    ContainerTypeName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
