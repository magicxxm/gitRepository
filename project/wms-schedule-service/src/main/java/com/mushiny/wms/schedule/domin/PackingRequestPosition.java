package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by 123 on 2017/5/15.
 */
@Entity
@Table(name = "OB_PACKINGREQUESTPOSITION")
public class PackingRequestPosition extends BaseClientAssignedEntity {

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "AMOUNT_PACKED")
    private BigDecimal amountPacked;

    @Column(name = "STATE")
    private String state;

    @ManyToOne
    @JoinColumn(name = "CUSTOMERSHIPMENTPOSITION_ID")
    private CustomerShipmentPosition customerShipmentPosition;

    @ManyToOne
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    @ManyToOne
    @JoinColumn(name = "LOT_ID")
    private Lot lot;

    @ManyToOne
    @JoinColumn(name = "PACKINGREQUEST_ID")
    private PackingRequest packingRequest;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountPacked() {
        return amountPacked;
    }

    public void setAmountPacked(BigDecimal amountPacked) {
        this.amountPacked = amountPacked;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public CustomerShipmentPosition getCustomerShipmentPosition() {
        return customerShipmentPosition;
    }

    public void setCustomerShipmentPosition(CustomerShipmentPosition customerShipmentPosition) {
        this.customerShipmentPosition = customerShipmentPosition;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public PackingRequest getPackingRequest() {
        return packingRequest;
    }

    public void setPackingRequest(PackingRequest packingRequest) {
        this.packingRequest = packingRequest;
    }
}
