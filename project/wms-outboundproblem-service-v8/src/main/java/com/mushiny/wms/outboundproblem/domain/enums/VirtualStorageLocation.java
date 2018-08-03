package com.mushiny.wms.outboundproblem.domain.enums;

public enum VirtualStorageLocation {

    PROBLEM_SOLVING("Problem Solving"),
    PROBLEM_SOLVED("Problem Solved"),
    PACKED("Packed");

    private String name;//定义自定义的变量

    VirtualStorageLocation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
