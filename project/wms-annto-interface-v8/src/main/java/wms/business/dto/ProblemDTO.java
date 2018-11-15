package wms.business.dto;

import wms.domain.ItemData;

import java.io.Serializable;

/**
 * Created by 123 on 2018/1/15.
 */
public class ProblemDTO implements Serializable {

     private  String description;

     private String problemType;

     private int amount;

     private String jobType = "Pack";

     private String reportBy;

     private String reportDate;

     private String problemStoragelocation; //问题商品所在的cell格子,String 必填

    private String containerCode;//问题周转箱Code,必填

    private String expirationDate;

    private String serialNo;

    private String barCode;

    private String itemCode;

    private String shipmentCode;

    private String itemDataId;

    private String shipmentId;

    private String lotNo;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getReportBy() {
        return reportBy;
    }

    public void setReportBy(String reportBy) {
        this.reportBy = reportBy;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getProblemStoragelocation() {
        return problemStoragelocation;
    }

    public void setProblemStoragelocation(String problemStoragelocation) {
        this.problemStoragelocation = problemStoragelocation;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getShipmentCode() {
        return shipmentCode;
    }

    public void setShipmentCode(String shipmentCode) {
        this.shipmentCode = shipmentCode;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }
}
