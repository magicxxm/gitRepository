package com.mushiny.model;


import com.mushiny.common.entity.BaseClientAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by 123 on 2017/5/4.
 */
@Entity
@Table(name="INV_STOCKUNITRECORD")
public class StockUnitRecord extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name="RECORD_CODE")
    private String recordCode;

    @Column(name="RECORD_TOOL",nullable = false)
    private String recordTool;

    @Column(name="RECORD_TYPE",nullable = false)
    private String recordType;

    @Column(name="AMOUNT")
    private BigDecimal amount;

    @Column(name="AMOUNT_STOCK")
    private BigDecimal amountStock  = BigDecimal.ZERO;

    @Column(name = "ITEMDATA_ITEMNO")
    private String itemDataItemNo;

    @Column(name="ITEMDATA_SKU")
    private String itemDataSKU;

    @Column(name="TO_STATE")
    private String toState;

    @Column(name = "LOT")
    private String lot;

    @Column(name="OPERATOR")
    private String operator;

    @Column(name = "TO_STOCKUNIT")
    private String toStockUnit;

    @Column(name = "TO_UNITLOAD")
    private String toUnitLoad;

    @Column(name="TO_STORAGELOCATION")
    private String toStorageLoaction;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
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

    public String getItemDataItemNo() {
        return itemDataItemNo;
    }

    public void setItemDataItemNo(String itemDataItemNo) {
        this.itemDataItemNo = itemDataItemNo;
    }

    public String getItemDataSKU() {
        return itemDataSKU;
    }

    public void setItemDataSKU(String itemDataSKU) {
        this.itemDataSKU = itemDataSKU;
    }

    public String getToState() {
        return toState;
    }

    public void setToState(String toState) {
        this.toState = toState;
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

    public String getToStockUnit() {
        return toStockUnit;
    }

    public void setToStockUnit(String toStockUnit) {
        this.toStockUnit = toStockUnit;
    }

    public String getToUnitLoad() {
        return toUnitLoad;
    }

    public void setToUnitLoad(String toUnitLoad) {
        this.toUnitLoad = toUnitLoad;
    }

    public String getToStorageLoaction() {
        return toStorageLoaction;
    }

    public void setToStorageLoaction(String toStorageLoaction) {
        this.toStorageLoaction = toStorageLoaction;
    }

}
