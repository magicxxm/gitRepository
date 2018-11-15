package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="INV_PENDING_ADJUSTRECORD")
public class PendingAdjustRecord  extends BaseClientAssignedEntity {

    private static final long serialVersionUID=1L;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "RECORD_CODE")
    private String recordCode;

    @Column(name = "RECORD_TOOL", nullable = false)
    private String recordTool;

    @Column(name = "RECORD_TYPE", nullable = false)
    private String recordType;

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

    @Column(name = "TO_STOCKUNIT")
    private String toStockUnit;

    @Column(name = "TO_UNITLOAD")
    private String toUnitLoad;

    @Column(name = "TO_STORAGELOCATION")
    private String toStorageLocation;

    @Column(name = "FROM_STATE")
    private String fromState;

    @Column(name = "STATE")
    private int state;

    @Column(name = "TOUCH_TOOL")
    private String touchTool;

    @Column(name = "SURE_DATE")
    private LocalDateTime sureDate ;

    @Column(name = "ADJUST_DATE")
    private LocalDateTime adjustDate ;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getFromUnitLoad() {
        return fromUnitLoad;
    }

    public void setFromUnitLoad(String fromUnitLoad) {
        this.fromUnitLoad = fromUnitLoad;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
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

    public String getToStorageLocation() {
        return toStorageLocation;
    }

    public void setToStorageLocation(String toStorageLocation) {
        this.toStorageLocation = toStorageLocation;
    }

    public String getFromState() {
        return fromState;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTouchTool() {
        return touchTool;
    }

    public void setTouchTool(String touchTool) {
        this.touchTool = touchTool;
    }

    public LocalDateTime getSureDate() {
        return sureDate;
    }

    public void setSureDate(LocalDateTime sureDate) {
        this.sureDate = sureDate;
    }

    public LocalDateTime getAdjustDate() {
        return adjustDate;
    }

    public void setAdjustDate(LocalDateTime adjustDate) {
        this.adjustDate = adjustDate;
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

}
