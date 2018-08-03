package com.mushiny.wcs.application.business.enums;

public enum SystemPropertyKeyEnum {

    STOW_POD_STATION_MAX_POD("StowPodStationMaxPod"),
    STOW_POD_SELECTION_MIN_VOLUME("StowPodSelectionMinVolume"),
    STOW_POD_SELECTION_MIN_WEIGHT("StowPodSelectionMinWeight"),
    STOW_POD_SELECTION_MIN_ITEMS("StowPodSelectionMinItems"),
    STOW_POD_FACE_SELECTION_MIN_VOLUME("StowPodFaceSelectionMinVolume"),
    STOW_POD_FACE_SELECTION_MIN_WEIGHT("StowPodFaceSelectionMinWeight"),
    STOW_POD_FACE_SELECTION_MIN_ITEMS("StowPodFaceSelectionMinItems"),
    STOW_POD_SELECTION_WEIGHT_CONSTANT("StowPodSelectionWeightConstant"),
    STOW_POD_SELECTION_VOLUME_CONSTANT("StowPodSelectionVolumeConstant"),
    STOW_POD_SELECTION_ITEMS_CONSTANT("StowPodSelectionItemsConstant"),
    EN_ROUTE_MAX_PODS_PS("EN_ROUTE_MAX_PODS_PS"),
    EN_ROUTE_MAX_PODS_BINCHENK("EN_ROUTE_MAX_PODS_BINCHENK"),
    EN_ROUTE_MAX_WORK_PS("EN_ROUTE_MAX_WORK _PS"),
    EN_ROUTE_MAX_WORK_BINCHECK("EN_ROUTE_MAX_WORK_BINCHECK"),
    CYCLE_TIME_PS("CYCLE_TIME_PS"),
    CYCLE_TIME_BINCHECK("CYCLE_TIME_BINCHECK");
    private String name;//定义自定义的变量

    SystemPropertyKeyEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
