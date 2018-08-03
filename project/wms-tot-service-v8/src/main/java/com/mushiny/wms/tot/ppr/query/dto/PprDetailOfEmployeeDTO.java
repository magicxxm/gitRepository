package com.mushiny.wms.tot.ppr.query.dto;

/**
 * Created by X1 on 2017/8/24.
 * 工作详情界面第一个表格数据对应DTO
 */
public class PprDetailOfEmployeeDTO {
    private String jobCode;
    private String jobName;
    private String employeeCode;
    private String employeeName;
    private double sHours;
    private double mHours;
    private double lHours;
    private double oHours;
    private double tHours;
    private long sAmount;
    private long mAmount;
    private long lAmount;
    private long oAmount;
    private long tAmount;
    private double sUnitHourAmount; // 操作商品的效率 amount/hours
    private double mUnitHourAmount;
    private double lUnitHourAmount;
    private double oUnitHourAmount;
    private double tUnitHourAmount;
    private long times;
    private double unitAmount;     // 一次扫描操作商品数 amount/times
    private double unitHourTimes;  // 扫描商品的效率 times/hours
    private String jobType;

    private double orderHours;  //准备工作时间

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

    public double getsHours() {
        return sHours;
    }

    public void setsHours(double sHours) {
        this.sHours = sHours;
    }

    public double getmHours() {
        return mHours;
    }

    public void setmHours(double mHours) {
        this.mHours = mHours;
    }

    public double getlHours() {
        return lHours;
    }

    public void setlHours(double lHours) {
        this.lHours = lHours;
    }

    public double getoHours() {
        return oHours;
    }

    public void setoHours(double oHours) {
        this.oHours = oHours;
    }

    public double gettHours() {
        return tHours;
    }

    public void settHours(double tHours) {
        this.tHours = tHours;
    }

    public long getsAmount() {
        return sAmount;
    }

    public void setsAmount(long sAmount) {
        this.sAmount = sAmount;
    }

    public long getmAmount() {
        return mAmount;
    }

    public void setmAmount(long mAmount) {
        this.mAmount = mAmount;
    }

    public long getlAmount() {
        return lAmount;
    }

    public void setlAmount(long lAmount) {
        this.lAmount = lAmount;
    }

    public long getoAmount() {
        return oAmount;
    }

    public void setoAmount(long oAmount) {
        this.oAmount = oAmount;
    }

    public long gettAmount() {
        return tAmount;
    }

    public void settAmount(long tAmount) {
        this.tAmount = tAmount;
    }

    public double getsUnitHourAmount() {
        return sUnitHourAmount;
    }

    public void setsUnitHourAmount(double sUnitHourAmount) {
        this.sUnitHourAmount = sUnitHourAmount;
    }

    public double getmUnitHourAmount() {
        return mUnitHourAmount;
    }

    public void setmUnitHourAmount(double mUnitHourAmount) {
        this.mUnitHourAmount = mUnitHourAmount;
    }

    public double getlUnitHourAmount() {
        return lUnitHourAmount;
    }

    public void setlUnitHourAmount(double lUnitHourAmount) {
        this.lUnitHourAmount = lUnitHourAmount;
    }

    public double getoUnitHourAmount() {
        return oUnitHourAmount;
    }

    public void setoUnitHourAmount(double oUnitHourAmount) {
        this.oUnitHourAmount = oUnitHourAmount;
    }

    public double gettUnitHourAmount() {
        return tUnitHourAmount;
    }

    public void settUnitHourAmount(double tUnitHourAmount) {
        this.tUnitHourAmount = tUnitHourAmount;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
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

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public double getOrderHours() {
        return orderHours;
    }

    public void setOrderHours(double orderHours) {
        this.orderHours = orderHours;
    }
}
