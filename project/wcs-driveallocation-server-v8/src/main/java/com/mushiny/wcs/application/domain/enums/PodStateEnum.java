package com.mushiny.wcs.application.domain.enums;

public enum PodStateEnum {

    AVAILABLE("Available"),
    RESERVED("Reserved");

    private String name;//定义自定义的变量

    PodStateEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
