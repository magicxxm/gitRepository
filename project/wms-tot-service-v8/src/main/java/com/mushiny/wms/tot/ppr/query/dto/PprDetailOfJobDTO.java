package com.mushiny.wms.tot.ppr.query.dto;

import com.mushiny.wms.tot.ppr.query.enums.NormalSize;

/**
 * Created by X1 on 2017/8/18.
 * 工作详情界面除第一个表格数据对应DTO
 */
public class PprDetailOfJobDTO {
    private String jobCode;
    private String jobName;//工作名
    private String size;
    private double hours;
    private long times;
    private long amount;
    private double unitAmount;     // 一次扫描操作商品数 amount/times
    private double unitHourTimes;  // 扫描商品的效率 times/hours
    private double unitHourAmount; // 操作商品的效率 amount/hours
    private String jobType;
    private String categoryName; //项目名
    private String employeeCode;
    private String employeeName;

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public double getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(double unitAmount) {
        this.unitAmount = unitAmount;
    }

    public double getUnitHourTimes() {
        return unitHourTimes;
    }

    public void setUnitHourTimes(double unitHourTimes) {
        this.unitHourTimes = unitHourTimes;
    }

    public double getUnitHourAmount() {
        return unitHourAmount;
    }

    public void setUnitHourAmount(double unitHourAmount) {
        this.unitHourAmount = unitHourAmount;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
}
