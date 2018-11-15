package wms.domain;


import wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by 123 on 2017/5/15.
 */
@Entity
@Table(name = "OB_DELIVERYPOINT")
public class DeliveryPoint extends BaseWarehouseAssignedEntity {

    @ManyToOne
    @JoinColumn(name = "CARRIER_ID", nullable = false)
    private Carrier carrier;

    @ManyToOne
    @JoinColumn(name = "SORTCODE_ID")
    private DeliverySortCode sortCode;

    @ManyToOne
    @JoinColumn(name = "DELIVERYTIME_ID", nullable = false)
    private DeliveryTime deliveryTime;


    public Carrier getCarrier() {
        return carrier;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }

    public DeliveryTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(DeliveryTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public DeliverySortCode getSortCode() {
        return sortCode;
    }

    public void setSortCode(DeliverySortCode sortCode) {
        this.sortCode = sortCode;
    }
}
