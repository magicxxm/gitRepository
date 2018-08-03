package com.mushiny.wms.tot.ppr.query.dto;

import java.io.Serializable;

/**
 * Created by Laptop-8 on 2017/6/29.
 * CTimeDetailDTO的详细版供PPR使用
 * 因为PPR需求在TOT之后 所以开发TOT没有考虑PPR
 * 故没有修改CTimeDetailDTO 而是新建该类 算是CTimeDetailDTO的更详细版本
 */
public class PprCTimeDetailDTO implements Serializable{
    private  String  message; //JobAction or no work or Onclock or Offclock
    private  String  activityStartTime; //开始时间
    private  String  activityEndTime; //结束时间
    private  double  total; //工作时间
    private  String  map;  //JobAction
    private  String  actionType;//直接工作,普通间接，超级间接
    private  String  categoryName; //jobrecord里面 的项目名
    private  String  size; //jobrecord里面 的size
    private  String  jobCode;//jobrecord里面 的工作条码(唯一)
    private  String  employeeCode; //jobrecord里面 的员工code
    private  String  employeeName; //jobrecord里面 的员工name
    private String warehouseId;
    private String clientId;
    private String jobName;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(String activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public String getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(String activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
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

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
