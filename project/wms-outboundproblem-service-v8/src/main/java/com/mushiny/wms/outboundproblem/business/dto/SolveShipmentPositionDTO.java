package com.mushiny.wms.outboundproblem.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class SolveShipmentPositionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String itemNo;

    private String itemName;

    private String itemDataId;

    private String shipmentId;

    private BigDecimal amountScaned = BigDecimal.ZERO;

    private BigDecimal amount = BigDecimal.ZERO;

    private String scaned;

    private boolean lotMandatory;

    private String lotType;

    private String lotUnit;

    private String serialRecordType;

    private List<String> serialNo;

    private BigDecimal stockUnitAmount;

    private String solveKey;

    private String solveId;

    private String skuNo;

    private String location;

    private String description;

    private BigDecimal width = BigDecimal.ZERO;

    private BigDecimal depth = BigDecimal.ZERO;

    private BigDecimal height = BigDecimal.ZERO;

    private BigDecimal weight = BigDecimal.ZERO;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String problemType;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal amountProblem = BigDecimal.ZERO;

    private BigDecimal amountDelete =BigDecimal.ZERO;

    public String getSolveId() {
        return solveId;
    }

    public void setSolveId(String solveId) {
        this.solveId = solveId;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getAmountScaned() {
        return amountScaned;
    }

    public void setAmountScaned(BigDecimal amountScaned) {
        this.amountScaned = amountScaned;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getScaned() {
        return scaned;
    }

    public void setScaned(String scaned) {
        this.scaned = scaned;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public BigDecimal getAmountProblem() {
        return amountProblem;
    }

    public void setAmountProblem(BigDecimal amountProblem) {
        this.amountProblem = amountProblem;
    }

    public boolean isLotMandatory() {
        return lotMandatory;
    }

    public void setLotMandatory(boolean lotMandatory) {
        this.lotMandatory = lotMandatory;
    }

    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
    }

    public BigDecimal getStockUnitAmount() {
        return stockUnitAmount;
    }

    public void setStockUnitAmount(BigDecimal stockUnitAmount) {
        this.stockUnitAmount = stockUnitAmount;
    }

    public String getSerialRecordType() {
        return serialRecordType;
    }

    public void setSerialRecordType(String serialRecordType) {
        this.serialRecordType = serialRecordType;
    }

    public List<String> getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(List<String> serialNo) {
        this.serialNo = serialNo;
    }

    public String getSolveKey() {
        return solveKey;
    }

    public void setSolveKey(String solveKey) {
        this.solveKey = solveKey;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getDepth() {
        return depth;
    }

    public void setDepth(BigDecimal depth) {
        this.depth = depth;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmountDelete() {
        return amountDelete;
    }

    public void setAmountDelete(BigDecimal amountDelete) {
        this.amountDelete = amountDelete;
    }

    public String getLotUnit() {
        return lotUnit;
    }

    public void setLotUnit(String lotUnit) {
        this.lotUnit = lotUnit;
    }
}
