package com.mushiny.wms.outboundproblem.crud.common.dto;

import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.outboundproblem.domain.common.StockUnitRecord;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class StockUnitRecordDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    private BigDecimal amount;

    private BigDecimal amountStock;

    private String fromStockUnit;

    private String fromStorageLocation;

    private String itemDataItemNo;

    private String itemDataSku;

    private String lot;

    @NotNull
    private String operator;

    private String recordCode;

    @NotNull
    private String recordTool;

    @NotNull
    private String recordType;

    private String toStockUnit;

    private String toStorageLocation;

    private String toUnitLoad;

    private String fromState;

    private String toState;

    public StockUnitRecordDTO(StockUnitRecord entity) {
        super(entity);
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

    public String getItemDataItemNo() {
        return itemDataItemNo;
    }

    public void setItemDataItemNo(String itemDataItemNo) {
        this.itemDataItemNo = itemDataItemNo;
    }

    public String getItemDataSku() {
        return itemDataSku;
    }

    public void setItemDataSku(String itemDataSku) {
        this.itemDataSku = itemDataSku;
    }
}
