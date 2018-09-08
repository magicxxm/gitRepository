package com.mushiny.wms.application.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum TripType {
    POD_RUN("PodRun"),
    PICK_POD("PickPod"),
    STOW_POD("StowPod"),
    IBP_POD("IBPPod"),
    OBP_POD("OBPPod"),
    ICQA_POD("ICQAPod"),
    POD_SCAN("PodScan"),
    MOVE_DRIVE("EmptyRun"),
    ANNTO_MVIN("ANNTOMVIN"),
    FINISHESWAREHOUSING("FINISHESWAREHOUSING"),
    ANNTOMVOUT("ANNTOMVOUT"),
    LMGETMATERIAL("LMGETMATERIAL"),
    CARRY_POD("CarryPod"),
    CHARGER_DRIVE("ChargerDrive");

    private String name;//定义自定义的变量
    private static Map<String,TripType> cash=new HashMap<>();

    static {
        for(TripType tt: values())
        {
            cash.put(tt.getName(),tt);
        }
    }
    TripType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static TripType parserTripType(String type){
       return cash.get(type);
    }
}
