package com.mushiny.wms.internaltool.common.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "IB_ADVICEREQUESTPOSITION")
public class AdviceRequestPosition extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_NO", nullable = false)
    private int positionNo;

    @Column(name = "NOTIFIED_AMOUNT")
    private BigDecimal notifiedAmount;

    @Column(name = "RECEIPT_AMOUNT")
    private BigDecimal receiptAmount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ADVICE_ID")
    private AdviceRequest adviceRequest;

    public int getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(int positionNo) {
        this.positionNo = positionNo;
    }

    public BigDecimal getNotifiedAmount() {
        return notifiedAmount;
    }

    public void setNotifiedAmount(BigDecimal notifiedAmount) {
        this.notifiedAmount = notifiedAmount;
    }

    public BigDecimal getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(BigDecimal receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

    public AdviceRequest getAdviceRequest() {
        return adviceRequest;
    }

    public void setAdviceRequest(AdviceRequest adviceRequest) {
        this.adviceRequest = adviceRequest;
    }
}
