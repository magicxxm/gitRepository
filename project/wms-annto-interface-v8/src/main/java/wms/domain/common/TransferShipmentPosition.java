package wms.domain.common;

import wms.common.entity.BaseClientAssignedEntity;
import wms.domain.ItemData;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by 123 on 2017/8/22.
 */
@Entity
@Table(name = "OB_TRANSFERSHIPMENTPOSITION")
public class TransferShipmentPosition extends BaseClientAssignedEntity {

    @Column(name="AMOUNT")
    private BigDecimal amount;

    @Column(name="AMOUNT_PICKED")
    private BigDecimal amountPicked = BigDecimal.ZERO;

    @Column(name="ORDER_INDEX")
    private int orderIndex;

    @Column(name="POSITION_NO")
    private int positionNo;

    @Column(name="STATE")
    private int state;

    @ManyToOne
    @JoinColumn(name="ITEMDATA_ID")
    private ItemData itemData;

    @ManyToOne(optional = false)
    @JoinColumn(name="TRANSFERSHIPMENT_ID")
    private TransferShipment transferShipment;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountPicked() {
        return amountPicked;
    }

    public void setAmountPicked(BigDecimal amountPicked) {
        this.amountPicked = amountPicked;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public int getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(int positionNo) {
        this.positionNo = positionNo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

    public TransferShipment getTransferShipment() {
        return transferShipment;
    }

    public void setTransferShipment(TransferShipment transferShipment) {
        this.transferShipment = transferShipment;
    }
}
