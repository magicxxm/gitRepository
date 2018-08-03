package com.mushiny.wms.internaltool.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ItemPurchasingRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String itemNo;

    private String skuNo;

    private String dn;

    private LocalDate expectedDelivery;

    private BigDecimal amount = BigDecimal.ZERO;

    private String clientName;

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public LocalDate getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(LocalDate expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }
}
