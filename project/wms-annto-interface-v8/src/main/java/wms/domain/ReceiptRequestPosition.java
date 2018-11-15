package wms.domain;

import wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by 123 on 2017/8/31.
 */
@Entity
@Table(name = "IB_ADVICEREQUESTPOSITION")
public class ReceiptRequestPosition extends BaseClientAssignedEntity {

    @Column(name = "POSITION_NO")
    private int positionNo;

    //应收货数量
    @Column(name = "NOTIFIED_AMOUNT")
    private BigDecimal notifiedAmount;

    //实际收货数量
    @Column(name = "RECEIPT_AMOUNT")
    private BigDecimal receiptAmount;

    @ManyToOne
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    @ManyToOne
    @JoinColumn(name = "ADVICE_ID")
    private ReceiptRequest receipt;

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

    public ReceiptRequest getReceipt() {
        return receipt;
    }

    public void setReceipt(ReceiptRequest receipt) {
        this.receipt = receipt;
    }
}
