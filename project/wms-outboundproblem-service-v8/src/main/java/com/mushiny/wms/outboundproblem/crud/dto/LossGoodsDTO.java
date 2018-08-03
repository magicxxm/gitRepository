package com.mushiny.wms.outboundproblem.crud.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class LossGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sourceId;

    private String itemDataId;

    private BigDecimal amount;

    private String adjustReason;

    private String thoseResponsible;

    private String problemDestination;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getProblemDestination() {
        return problemDestination;
    }

    public void setProblemDestination(String problemDestination) {
        this.problemDestination = problemDestination;
    }
}
