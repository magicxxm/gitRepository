package com.mushiny.model;

import com.mushiny.common.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "OB_DELIVERYTIME")
public class DeliveryTime extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "DELIVERY_TIME")
    private LocalDateTime deliveryTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WAREHOUSE_ID")
    private Warehouse warehouse;

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
}
