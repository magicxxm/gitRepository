package com.mushiny.wms.outboundproblem.domain.common;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "INV_STOCKUNITRECORD")
public class StockUnitRecord extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "AMOUNT_STOCK")
    private BigDecimal amountStock;

    @Column(name = "FROM_STOCKUNIT")
    private String fromStockUnit;

    @Column(name = "FROM_STORAGELOCATION")
    private String fromStorageLocation;

    @Column(name = "FROM_UNITLOAD")
    private String fromUnitLoad;

    @Column(name = "LOT")
    private String lot;

    @Column(name = "ITEMDATA_ITEMNO")
    private String itemNo;

    @Column(name = "ITEMDATA_SKU")
    private String sku;

    @Column(name = "OPERATOR", nullable = false)
    private String operator;

    @Column(name = "ADJUSTREASON")
    private String adjustReason;

    @Column(name = "RECORD_CODE")
    private String recordCode;

    @Column(name = "RECORD_TOOL", nullable = false)
    private String recordTool;

    @Column(name = "RECORD_TYPE", nullable = false)
    private String recordType;

    @Column(name = "TO_STOCKUNIT")
    private String toStockUnit;

    @Column(name = "TO_STORAGELOCATION")
    private String toStorageLocation;

    @Column(name = "TO_UNITLOAD")
    private String toUnitLoad;

    @Column(name = "FROM_STATE")
    private String fromState;

    @Column(name = "TO_STATE")
    private String toState;

    @Column(name = "THOSERESPONSIBLE")
    private String thoseResponsible;

    @Column(name = "PROBLEMDESTINATION")
    private String problemDestination;

    @ManyToOne
    @JoinColumn(name = "CLIENT_ID",insertable = false,updatable = false)
    private Client client;

    @Column(name="SERIAL_NUMBER")
    private String serialNo;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountStock() {
        return amountStock;
    }

    public void setAmountStock(BigDecimal amountStock) {
        this.amountStock = amountStock;
    }

    public String getFromStockUnit() {
        return fromStockUnit;
    }

    public void setFromStockUnit(String fromStockUnit) {
        this.fromStockUnit = fromStockUnit;
    }

    public String getFromStorageLocation() {
        return fromStorageLocation;
    }

    public void setFromStorageLocation(String fromStorageLocation) {
        this.fromStorageLocation = fromStorageLocation;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getToStockUnit() {
        return toStockUnit;
    }

    public void setToStockUnit(String toStockUnit) {
        this.toStockUnit = toStockUnit;
    }

    public String getToStorageLocation() {
        return toStorageLocation;
    }

    public void setToStorageLocation(String toStorageLocation) {
        this.toStorageLocation = toStorageLocation;
    }

    public String getToUnitLoad() {
        return toUnitLoad;
    }

    public void setToUnitLoad(String toUnitLoad) {
        this.toUnitLoad = toUnitLoad;
    }

    public String getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode;
    }

    public String getRecordTool() {
        return recordTool;
    }

    public void setRecordTool(String recordTool) {
        this.recordTool = recordTool;
    }

    public String getFromState() {
        return fromState;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public String getToState() {
        return toState;
    }

    public void setToState(String toState) {
        this.toState = toState;
    }

    public String getAdjustReason() {
        return adjustReason;
    }

    public void setAdjustReason(String adjustReason) {
        this.adjustReason = adjustReason;
    }

    public String getFromUnitLoad() {
        return fromUnitLoad;
    }

    public void setFromUnitLoad(String fromUnitLoad) {
        this.fromUnitLoad = fromUnitLoad;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getThoseResponsible() {
        return thoseResponsible;
    }

    public void setThoseResponsible(String thoseResponsible) {
        this.thoseResponsible = thoseResponsible;
    }

    public String getProblemDestination() {
        return problemDestination;
    }

    public void setProblemDestination(String problemDestination) {
        this.problemDestination = problemDestination;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
