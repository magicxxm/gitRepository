package com.mushiny.wms.internaltool.common.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "INV_LOTRECORD")
public class LotRecord extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "ITEMDATA_ITEMNO")
    private String itemNo;

    @Column(name = "ITEMDATA_SKU")
    private String sku;

    @Column(name = "ITEMDATA_NAME")
    private String itemDateName;

    @Column(name = "FROM_STORAGELOCATION")
    private String fromStorageLocation;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "FROM_USE_NOT_AFTER")
    private LocalDate fromUseNotAfter;

    @Column(name = "TO_USE_NOT_AFTER")
    private LocalDate toUseNotAfter;

    @Column(name = "RECORD_TOOL")
    private String recordTool;

    @ManyToOne
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public String getItemDateName() {
        return itemDateName;
    }

    public void setItemDateName(String itemDateName) {
        this.itemDateName = itemDateName;
    }

    public String getFromStorageLocation() {
        return fromStorageLocation;
    }

    public void setFromStorageLocation(String fromStorageLocation) {
        this.fromStorageLocation = fromStorageLocation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getFromUseNotAfter() {
        return fromUseNotAfter;
    }

    public void setFromUseNotAfter(LocalDate fromUseNotAfter) {
        this.fromUseNotAfter = fromUseNotAfter;
    }

    public LocalDate getToUseNotAfter() {
        return toUseNotAfter;
    }

    public void setToUseNotAfter(LocalDate toUseNotAfter) {
        this.toUseNotAfter = toUseNotAfter;
    }

    public String getRecordTool() {
        return recordTool;
    }

    public void setRecordTool(String recordTool) {
        this.recordTool = recordTool;
    }
}
