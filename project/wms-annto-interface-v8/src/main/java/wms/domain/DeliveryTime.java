package wms.domain;


import wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by 123 on 2017/5/15.
 */
@Entity
@Table(name = "OB_DELIVERYTIME")
public class DeliveryTime extends BaseWarehouseAssignedEntity {

    @Column(name = "DELIVERY_TIME")
    private String deliveryTime;

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
