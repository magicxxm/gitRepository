package com.mushiny.wms.masterdata.obbasics.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Laptop-11 on 2017/6/8.
 */
@Entity
@Table(name = "OB_DELIVERYTIME")
public class DeliveryTime extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;
    @Column(name = "DELIVERY_TIME",nullable = false)
    private String deliveryTime;

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
