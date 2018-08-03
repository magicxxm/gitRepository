package com.mushiny.wcs.application.business.enums;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/9/25.
 */
public enum StationType {
    IBP("IBPPod"),
    OBP("OBPPod"),
    ICQA("ICQAPod");
    private String name;

    StationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
