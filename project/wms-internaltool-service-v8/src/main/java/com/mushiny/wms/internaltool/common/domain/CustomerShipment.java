package com.mushiny.wms.internaltool.common.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_CUSTOMERSHIPMENT")
public class CustomerShipment extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_NO")
    private String customerNo;

    @Column(name = "DELIVERY_DATE")
    private ZonedDateTime deliveryDate;

    @Column(name = "SORT_CODE")
    private String sortCode;

    @Column(name = "SHIPMENT_NO", nullable = false, unique = true)
    private String shipmentNo;

    @Column(name = "PRIORITY", nullable = false)
    private int priority;

    @Column(name = "STATE", nullable = false)
    private int state;

    @OrderBy("positionNo")
    @OneToMany(mappedBy = "customerShipment")
    private List<CustomerShipmentPosition> positions = new ArrayList<>();

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public ZonedDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(ZonedDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<CustomerShipmentPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<CustomerShipmentPosition> positions) {
        this.positions = positions;
    }
}
