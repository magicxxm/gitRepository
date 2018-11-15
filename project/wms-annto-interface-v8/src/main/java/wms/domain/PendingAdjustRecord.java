package wms.domain;


import wms.common.entity.BaseWarehouseAssignedEntity;
import wms.domain.common.Client;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "INV_PENDING_ADJUSTRECORD")
public class PendingAdjustRecord extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "RECORD_CODE")
    private String recordCode;

    @Column(name = "RECORD_TOOL")
    private String recordTool;

    @Column(name = "RECORD_TYPE")
    private String recordType;

    @Column(name = "AMOUNT")
    private BigDecimal amount;


    @Column(name = "FROM_STOCKUNIT")
    private String fromStockUnit;

    @Column(name = "FROM_UNITLOAD")
    private String fromUnitLoad;

    @Column(name = "FROM_STORAGELOCATION")
    private String fromStorageLocation;

    @Column(name = "ITEMDATA_ITEMNO")
    private String itemNo;

    @Column(name = "ITEMDATA_SKU")
    private String sku;

    @Column(name = "FROM_STATE")
    private String fromState;

    @Column(name = "STATE")
    private int state;

    @Column(name = "LOT")
    private String lot;

    @Column(name = "OPERATOR")
    private String operator;

    @Column(name = "TO_STOCKUNIT")
    private String toStockUnit;

    @Column(name = "TO_UNITLOAD")
    private String toUnitLoad;

    @Column(name = "TO_STORAGELOCATION")
    private String toStorageLocation;

    @ManyToOne
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    @Column(name = "ADJUST_DATE")
    private LocalDateTime adjustDate;

    @Column(name = "SURE_DATE")
    private LocalDateTime sureDate;

    @Column(name = "TOUCH_TOOL")
    private String touchTool;

    @Column(name = "STOCKUNIT_ID")
    private String stockUnitId;


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

    public String getFromState() {
        return fromState;
    }

    public void setFromState(String fromState) {
        this.fromState = fromState;
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

    public String getToStorageLocation() {
        return toStorageLocation;
    }

    public void setToStorageLocation(String toStorageLocation) {
        this.toStorageLocation = toStorageLocation;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public LocalDateTime getAdjustDate() {
        return adjustDate;
    }

    public void setAdjustDate(LocalDateTime adjustDate) {
        this.adjustDate = adjustDate;
    }

    public LocalDateTime getSureDate() {
        return sureDate;
    }

    public void setSureDate(LocalDateTime sureDate) {
        this.sureDate = sureDate;
    }

    public String getTouchTool() {
        return touchTool;
    }

    public void setTouchTool(String touchTool) {
        this.touchTool = touchTool;
    }

    public String getStockUnitId() {
        return stockUnitId;
    }

    public void setStockUnitId(String stockUnitId) {
        this.stockUnitId = stockUnitId;
    }

}
