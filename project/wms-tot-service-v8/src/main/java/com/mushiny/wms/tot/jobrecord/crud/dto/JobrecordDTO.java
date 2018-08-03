package com.mushiny.wms.tot.jobrecord.crud.dto;
import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.tot.jobrecord.domain.Jobrecord;

public class JobrecordDTO extends BaseDTO {
    private String recordTime;
    private String employeeCode;
    private String employeeName;
    private String tool;
    private String jobAction;
    private String jobCode;
    private String jobName;
    private String jobType;
    private String description;
    private String skuNo;
    private String unitType;
    private String size;
    private Integer quantity;
    private String fromStoragelocation;
    private String toStoragelocation;
    private String clientId;
    private String warehouseId;
    private String newBarcode;
    private String indirectType;
    private String operation;
    private String categoryName;
    private String shipmentNo;


    public JobrecordDTO() {
    }

    public String getNewBarcode() {
        return newBarcode;
    }

    public void setNewBarcode(String newBarcode) {
        this.newBarcode = newBarcode;
    }

    public JobrecordDTO(Jobrecord entity) {
        super(entity);
    }

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
