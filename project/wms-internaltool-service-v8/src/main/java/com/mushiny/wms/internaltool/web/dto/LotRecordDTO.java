package com.mushiny.wms.internaltool.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LotRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime createDate;

    private String createBy;

    private String itemNo;

    private String sku;

    private String itemName;

    private String fromStorageLocation;

    private BigDecimal amount;

    private LocalDate fromUseNotAfter;

    private LocalDate toUseNotAfter;

    private String clientName;

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
