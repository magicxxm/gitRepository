package com.mushiny.wms.outboundproblem.crud.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class OverageGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String destinationId;

    private String itemNo;

    private String clientId;

    private String sn;

    private String inventoryState;

    private BigDecimal amount;

    private LocalDate useNotAfter;

    private String adjustReason;

    private String thoseResponsible;

    private String problemDestination;

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getProblemDestination() {
        return problemDestination;
    }

    public void setProblemDestination(String problemDestination) {
        this.problemDestination = problemDestination;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getInventoryState() {
        return inventoryState;
    }

    public void setInventoryState(String inventoryState) {
        this.inventoryState = inventoryState;
    }

    public LocalDate getUseNotAfter() {
        return useNotAfter;
    }

    public void setUseNotAfter(LocalDate useNotAfter) {
        this.useNotAfter = useNotAfter;
    }

    public String getAdjustReason() {
        return adjustReason;
    }

    public void setAdjustReason(String adjustReason) {
        this.adjustReason = adjustReason;
    }

    public String getThoseResponsible() {
        return thoseResponsible;
    }

    public void setThoseResponsible(String thoseResponsible) {
        this.thoseResponsible = thoseResponsible;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
