package com.mushiny.wms.report.query.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

//worklow 明细·
public class WorkFlowDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String shipmentID;

    private String orderID;

    private String boxType;

    private String SKUNO;

    private String SKUID;

    private BigDecimal quality;

    private Timestamp planDepartTime;

    private String stockPosition1;

    private String stockPosition2;

    private String workFlowStatus;

    private String ppName;

    private String batchNo;

    private String lotDate;

    public WorkFlowDetailDTO(String shipmentID,
                             String orderID,
                             String boxType,
                             String SKUNO,
                             String SKUID,
                             BigDecimal quality,
                             Timestamp planDepartTime,
                             String stockPosition1,
                             String stockPosition2,
                             String workFlowStatus,
                             String ppName,
                             String batchNo,
                             String lotDate) {
        this.shipmentID = shipmentID;
        this.orderID = orderID;
        this.boxType = boxType;
        this.SKUNO = SKUNO;
        this.SKUID = SKUID;
        this.quality = quality;
        this.planDepartTime = planDepartTime;
        this.stockPosition1 = stockPosition1;
        this.stockPosition2 = stockPosition2;
        this.workFlowStatus = workFlowStatus;
        this.ppName = ppName;
        this.batchNo = batchNo;
        this.lotDate = lotDate;
    }

    public WorkFlowDetailDTO() {
    }

    public String getShipmentID() {
        return shipmentID;
    }

    public void setShipmentID(String shipmentID) {
        this.shipmentID = shipmentID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public String getSKUNO() {
        return SKUNO;
    }

    public void setSKUNO(String SKUNO) {
        this.SKUNO = SKUNO;
    }

    public String getSKUID() {
        return SKUID;
    }

    public void setSKUID(String SKUID) {
        this.SKUID = SKUID;
    }

    public BigDecimal getQuality() {
        return quality;
    }

    public void setQuality(BigDecimal quality) {
        this.quality = quality;
    }

    public Timestamp getPlanDepartTime() {
        return planDepartTime;
    }

    public void setPlanDepartTime(Timestamp planDepartTime) {
        this.planDepartTime = planDepartTime;
    }

    public String getStockPosition1() {
        return stockPosition1;
    }

    public void setStockPosition1(String stockPosition1) {
        this.stockPosition1 = stockPosition1;
    }

    public String getStockPosition2() {
        return stockPosition2;
    }

    public void setStockPosition2(String stockPosition2) {
        this.stockPosition2 = stockPosition2;
    }

    public String getWorkFlowStatus() {
        return workFlowStatus;
    }

    public void setWorkFlowStatus(String workFlowStatus) {
        this.workFlowStatus = workFlowStatus;
    }

    public String getPpName() {
        return ppName;
    }

    public void setPpName(String ppName) {
        this.ppName = ppName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getLotDate() {
        return lotDate;
    }

    public void setLotDate(String lotDate) {
        this.lotDate = lotDate;
    }

    @Override
    public String toString() {
        return "WorkFlowDetailDTO{" +
                "shipmentID='" + shipmentID + '\'' +
                ", orderID='" + orderID + '\'' +
                ", boxType='" + boxType + '\'' +
                ", SKUNO='" + SKUNO + '\'' +
                ", SKUID='" + SKUID + '\'' +
                ", quality=" + quality +
                ", planDepartTime=" + planDepartTime +
                ", stockPosition1='" + stockPosition1 + '\'' +
                ", stockPosition2='" + stockPosition2 + '\'' +
                ", workFlowStatus='" + workFlowStatus + '\'' +
                ", ppName='" + ppName + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", lotDate='" + lotDate + '\'' +
                '}';
    }
}
