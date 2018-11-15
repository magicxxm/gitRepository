package wms.domain.common;


import wms.common.entity.BaseClientAssignedEntity;

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

    @Column(name="FROM_STOCKUNIT")
    private String fromStockUnit;

    @Column(name = "FROM_UNITLOAD")
    private String fromUnitLoad;

    @Column(name="FROM_STORAGELOCATION")
    private String fromStorageLocation;

    @Column(name = "ITEMDATA_ITEMNO")
    private String itemDataItemNo;

    @Column(name="ITEMDATA_SKU")
    private String itemDataSKU;

    @Column(name = "FROM_STATE")
    private String fromState;

    @Column(name="TO_STATE")
    private String toState;

    @Column(name="SERIAL_NUMBER")
    private String serialNumber;

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

    @Column(name = "ADJUSTREASON")
    private String adjustReason;

    @Column(name = "THOSERESPONSIBLE")
    private String thoseResponible;

    @Column(name="PROBLEMDESTINATION")
    private String problemDestination;

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

    public String getFromStockUnit() {
        return fromStockUnit;
    }

    public void setFromStockUnit(String fromStockUnit) {
        this.fromStockUnit = fromStockUnit;
    }

    public String getFromUnitLoad() {
        return fromUnitLoad;
    }

    public void setFromUnitLoad(String fromUnitLoad) {
        this.fromUnitLoad = fromUnitLoad;
    }

    public String getFromStorageLocation() {
        return fromStorageLocation;
    }

    public void setFromStorageLocation(String fromStorageLocation) {
        this.fromStorageLocation = fromStorageLocation;
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

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

    public String getAdjustReason() {
        return adjustReason;
    }

    public void setAdjustReason(String adjustReason) {
        this.adjustReason = adjustReason;
    }

    public String getThoseResponible() {
        return thoseResponible;
    }

    public void setThoseResponible(String thoseResponible) {
        this.thoseResponible = thoseResponible;
    }

    public String getProblemDestination() {
        return problemDestination;
    }

    public void setProblemDestination(String problemDestination) {
        this.problemDestination = problemDestination;
    }
}
