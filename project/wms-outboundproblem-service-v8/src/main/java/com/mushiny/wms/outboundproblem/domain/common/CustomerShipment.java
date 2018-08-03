package com.mushiny.wms.outboundproblem.domain.common;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.outboundproblem.domain.OBPSolve;

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

    @Column(name = "SHIPMENT_NO")
    private String shipmentNo;

    @Column(name = "PRIORITY")
    private int priority;

    @Column(name = "STATE")
    private int state;

    @Column(name = "PICK_MODE")
    private String pickMode;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private CustomerOrder customerOrder;

    @OrderBy("solveBy")
    @OneToMany(mappedBy = "customerShipment", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<OBPSolve> solves = new ArrayList<>();

    @OrderBy("positionNo")
    @OneToMany(mappedBy = "customerShipment", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<CustomerShipmentPosition> positions = new ArrayList<>();

    public void addPosition(CustomerShipmentPosition position) {
        getPositions().add(position);
        position.setCustomerShipment(this);
    }

    public void addSolve(OBPSolve solve) {
        getSolves().add(solve);
        solve.setCustomerShipment(this);
    }

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

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public List<OBPSolve> getSolves() {
        return solves;
    }

    public void setSolves(List<OBPSolve> solves) {
        this.solves = solves;
    }

    public String getPickMode() {
        return pickMode;
    }

    public void setPickMode(String pickMode) {
        this.pickMode = pickMode;
    }
}
