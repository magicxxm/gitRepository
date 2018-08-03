package com.mushiny.wms.masterdata.ibbasics.business.enums;

public enum AreaName {

    PICKING_ZONE("Picking_Zone"),
    DAMAGE_ZONE("Damage_Zone"),
    BUFFER_ZONE("Buffer_Zone");

    private String name;//定义自定义的变量

    AreaName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
