package wms.domain;

import wms.common.entity.BaseClientAssignedEntity;
import wms.domain.common.AdviceRequest;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    //显示对应的possition的数量。
    @Column(name = "SIZE")
    private int size;

    @Column(name = "RECEIPT_DATE")
    private LocalDateTime receiptDate;

    @Column(name = "RECEIPT_STATE")
    private String receiptState;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RELATEDADVICE_ID")
    private AdviceRequest relatedAdvice;

    @OrderBy("itemData")
    @OneToMany(mappedBy = "goodsReceipt", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<GoodsReceiptPosition> positions = new ArrayList<>();

    public void addPosition(GoodsReceiptPosition position) {
        getPositions().add(position);
        position.setGoodsReceipt(this);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

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

    public LocalDateTime getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDateTime receiptDate) {
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
