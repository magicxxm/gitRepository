package com.mushiny.wcs.application.domain.enums;

public enum TripType {

    STOW_POD("StowPod");

    private String name;//定义自定义的变量

    TripType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
