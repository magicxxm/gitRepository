package com.mushiny.wms.masterdata.ibbasics.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataDTO;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemDataAmountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private ItemDataDTO itemData;

    private BigDecimal amount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal receiveAmount;

    private BigDecimal dnAmount;

    public ItemDataDTO getItemData() {
        return itemData;
    }

    public void setItemData(ItemDataDTO itemData) {
        this.itemData = itemData;
    }

    public BigDecimal getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(BigDecimal receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDnAmount() {
        return dnAmount;
    }

    public void setDnAmount(BigDecimal dnAmount) {
        this.dnAmount = dnAmount;
    }
}
