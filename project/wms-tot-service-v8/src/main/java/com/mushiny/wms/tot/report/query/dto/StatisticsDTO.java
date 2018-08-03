package com.mushiny.wms.tot.report.query.dto;

import java.io.Serializable;

/**
 * Created by Laptop-8 on 2017/6/27.
 * 暂无用
 */
public class StatisticsDTO implements Serializable{
    private String employeeCode;
    private String employeeName;
    private String ecWorkTime;     //有效工作时间
    private String totalClockTime; //打卡总时间
    private String ecWorkRate;     //有效工作率

    private String clockTime1;
    private String clockTime2;
    private String clockTime3;
    private String clockTime4;

    private String clockType1;
    private String clockType2;
    private String clockType3;
    private String clockType4;
    private String minClockTime;
    private String maxJobActionTime;


    public String getMinClockTime() {
        return minClockTime;
    }

    public void setMinClockTime(String minClockTime) {
        this.minClockTime = minClockTime;
    }

    public String getMaxJobActionTime() {
        return maxJobActionTime;
    }

    public void setMaxJobActionTime(String maxJobActionTime) {
        this.maxJobActionTime = maxJobActionTime;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEcWorkTime() {
        return ecWorkTime;
    }

    public void setEcWorkTime(String ecWorkTime) {
        this.ecWorkTime = ecWorkTime;
    }

    public String getTotalClockTime() {
        return totalClockTime;
    }

    public void setTotalClockTime(String totalClockTime) {
        this.totalClockTime = totalClockTime;
    }

    public String getEcWorkRate() {
        return ecWorkRate;
    }

    public void setEcWorkRate(String ecWorkRate) {
        this.ecWorkRate = ecWorkRate;
    }

    public String getClockTime1() {
        return clockTime1;
    }

    public void setClockTime1(String clockTime1) {
        String clockTime_one = clockTime1.substring(0,(clockTime1.indexOf(".")));
        this.clockTime1 = clockTime_one;
    }

    public String getClockTime2() {
        return clockTime2;
    }

    public void setClockTime2(String clockTime2) {
        String clockTime_two = clockTime2.substring(0,(clockTime2.indexOf(".")));
        this.clockTime2 = clockTime_two;
    }

    public String getClockTime3() {
        return clockTime3;
    }

    public void setClockTime3(String clockTime3) {
        String clockTime_three = clockTime3.substring(0,(clockTime3.indexOf(".")));
        this.clockTime3 = clockTime_three;
    }

    public String getClockTime4() {
        return clockTime4;
    }

    public void setClockTime4(String clockTime4) {
        String clockTime_four = clockTime4.substring(0,(clockTime4.indexOf(".")));
        this.clockTime4 = clockTime_four;
    }

    public String getClockType1() {
        return clockType1;
    }

    public void setClockType1(String clockType1) {
        this.clockType1 = clockType1;
    }

    public String getClockType2() {
        return clockType2;
    }

    public void setClockType2(String clockType2) {
        this.clockType2 = clockType2;
    }

    public String getClockType3() {
        return clockType3;
    }

    public void setClockType3(String clockType3) {
        this.clockType3 = clockType3;
    }

    public String getClockType4() {
        return clockType4;
    }

    public void setClockType4(String clockType4) {
        this.clockType4 = clockType4;
    }
}
