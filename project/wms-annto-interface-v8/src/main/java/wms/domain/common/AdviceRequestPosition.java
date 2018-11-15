package wms.domain.common;

import wms.common.entity.BaseClientAssignedEntity;
import wms.domain.ItemData;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "IB_ADVICEREQUESTPOSITION")
public class AdviceRequestPosition extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "POSITION_NO", nullable = false)
    private int positionNo = 0;

    @Column(name = "NOTIFIED_AMOUNT")
    private BigDecimal notifiedAmount;

    @Column(name = "RECEIPT_AMOUNT")
    private BigDecimal receiptAmount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

//    @ManyToOne
//    @JoinColumn(name = "LOT_ID")
//    private Lot lot;

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

//    public Lot getLot() {
//        return lot;
//    }
//
//    public void setLot(Lot lot) {
//        this.lot = lot;
//    }

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
