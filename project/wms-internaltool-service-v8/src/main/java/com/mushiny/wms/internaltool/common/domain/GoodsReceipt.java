package com.mushiny.wms.internaltool.common.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "IB_GOODSRECEIPT")
public class GoodsReceipt extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "GR_NO", nullable = false, unique = true)
    private String grNo;

    @Column(name = "DELIVERY_NOTE")
    private String deliveryNote;

    @Column(name = "RECEIPT_DATE")
    private LocalDate receiptDate;

    @Column(name = "RECEIPT_STATE")
    private String receiptState;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RELATEDADVICE_ID")
    private AdviceRequest relatedAdvice;

    @OrderBy("positionNo")
    @OneToMany(mappedBy = "goodsReceipt")
    private List<GoodsReceiptPosition> positions = new ArrayList<>();

    public String getGrNo() {
        return grNo;
    }

    public void setGrNo(String grNo) {
        this.grNo = grNo;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getReceiptState() {
        return receiptState;
    }

    public void setReceiptState(String receiptState) {
        this.receiptState = receiptState;
    }

    public AdviceRequest getRelatedAdvice() {
        return relatedAdvice;
    }

    public void setRelatedAdvice(AdviceRequest relatedAdvice) {
        this.relatedAdvice = relatedAdvice;
    }

    public List<GoodsReceiptPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<GoodsReceiptPosition> positions) {
        this.positions = positions;
    }
}
