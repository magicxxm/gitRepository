package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

/**
 * Created by Laptop-11 on 2017/6/8.
 */
@Entity
@Table(name = "OB_DELIVERYPOINT")
public class DeliveryPoint extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;


    @ManyToOne
    @JoinColumn(name = "SORTCODE_ID")
    private DeliverySortCode deliverySortCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "DELIVERYTIME_ID")
    private DeliveryTime deliveryTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CARRIER_ID")
    private Carrier carrier;

    public DeliverySortCode getDeliverySortCode() {
        return deliverySortCode;
    }

    public void setDeliverySortCode(DeliverySortCode deliverySortCode) {
        this.deliverySortCode = deliverySortCode;
    }

    public DeliveryTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(DeliveryTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }

}
