package com.mushiny.wcs.application.domain.enums;

public enum SystemPropertyKeyEnum {

    DRIVE_CHARGER_Full_VALUE("DriveChargerFullValue"),
    DRIVE_OUT_CHARGER_MIN_VALUE("DriveOutChargerMinValue"),
    DRIVE_IN_CHARGER_MIN_VALUE("DriveInChargerMinValue"),
    DRIVE_CHARGER_CONSTANT("DriveChargerConstant"),
    Robot_Voltage_MaxValue("RobotVoltageMaxValue"),
    Robot_Voltage_MinValue("RobotVoltageMinValue"),
    DRIVE_CHARGER_TIME_VALUE("DriveChargerTimeValue");

    private String name;//定义自定义的变量

    SystemPropertyKeyEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
