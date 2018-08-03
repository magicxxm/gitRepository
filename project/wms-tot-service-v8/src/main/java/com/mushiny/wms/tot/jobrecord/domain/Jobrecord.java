package com.mushiny.wms.tot.jobrecord.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TOT_JOBRECORD")
public class Jobrecord extends BaseEntity {
    @Column(name = "RECORD_TIME")
    private String recordTime;
    @Column(name = "EMPLOYEE_CODE")
    private  String employeeCode;
    @Column(name = "EMPLOYEE_NAME")
    private  String employeeName;
    @Column(name = "TOOL")
    private String tool;
    @Column(name = "JOB_ACTION")
    private String jobAction;
    @Column(name = "JOB_CODE")
    private String jobCode;
    @Column(name = "JOB_NAME")
    private String jobName;
    @Column(name = "JOB_TYPE")
    private String jobType;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "SKU_NO")
    private String skuNo;
    @Column(name = "ITEM_NO")
    private String newBarcode;
    @Column(name = "UNIT_TYPE")
    private String unitType;
    @Column(name = "SIZE")
    private String size;
    @Column(name = "QUANTITY")
    private Integer quantity;
    @Column(name = "FROM_STORAGELOCATION")
    private String fromStoragelocation;
    @Column(name = "TO_STORAGELOCATION")
    private String toStoragelocation;
    @Column(name = "CLIENT_ID", nullable = false)
    private  String clientId;
    @Column(name = "WAREHOUSE_ID",nullable = false)
    private String warehouseId;
    @Column(name = "INDIRECT_TYPE")
    private String indirectType;
    @Column(name = "OPERATION")
    private String operation;
    @Column(name = "CATEGORY_NAME")
    private String categoryName;
    @Column(name = "SHIPMENTNO")
    private String shipmentNo;

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
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

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public String getJobAction() {
        return jobAction;
    }

    public void setJobAction(String jobAction) {
        this.jobAction = jobAction;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getFromStoragelocation() {
        return fromStoragelocation;
    }

    public void setFromStoragelocation(String fromStoragelocation) {
        this.fromStoragelocation = fromStoragelocation;
    }

    public String getToStoragelocation() {
        return toStoragelocation;
    }

    public void setToStoragelocation(String toStoragelocation) {
        this.toStoragelocation = toStoragelocation;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNewBarcode() {
        return newBarcode;
    }

    public void setNewBarcode(String newBarcode) {
        this.newBarcode = newBarcode;
    }

    public String getIndirectType() {
        return indirectType;
    }

    public void setIndirectType(String indirectType) {
        this.indirectType = indirectType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }
}
