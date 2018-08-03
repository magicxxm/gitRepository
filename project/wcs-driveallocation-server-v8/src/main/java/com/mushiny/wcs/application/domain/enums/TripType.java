package com.mushiny.wcs.application.domain.enums;

public enum TripType {
    POD_RUN("PodRun"),
    PICK_POD("PickPod"),
    STOW_POD("StowPod"),
    IBP_POD("IBPPod"),
    OBP_POD("OBPPod"),
    ICQA_POD("ICQAPod"),
    POD_SCAN("PodScan"),
    MOVE_DRIVE("EmptyRun"),
    CHARGER_DRIVE("ChargerDrive");
    private String name;//定义自定义的变量

    TripType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
