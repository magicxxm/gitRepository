package com.mushiny.wcs.application.domain.enums;

public enum TripState {

    NEW("New"),
    AVAILABLE("Available"),
    PROCESS("Process"),
    LEAVING("Leaving"),
    FINISH("Finish");

    private String name;//定义自定义的变量

    TripState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
