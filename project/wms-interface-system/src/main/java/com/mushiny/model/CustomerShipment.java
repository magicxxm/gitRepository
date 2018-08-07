package com.mushiny.model;


import com.mushiny.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/4/25.
 */
@Entity
@Table(name = "OB_CUSTOMERSHIPMENT")
public class CustomerShipment extends BaseClientAssignedEntity {

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_NO")
    private String customerNo;

    @Column(name = "EXSD_TIME")
    private LocalDateTime exsdTime;

    @Column(name = "DELIVERY_DATE")
    private LocalDateTime deliveryDate;//传递的发货时间

    @Column(name = "SORT_CODE")
    private String sortCode;

    @Column(name = "SHIPMENT_NO", unique = true)
    private String shipmentNo;

    @Column(name = "PRIORITY")
    private int priority =  0;

    @Column(name = "STATE")
    private int state = 0;

    @Column(name = "PASSED_OVER_COUNT", nullable = false)
    private Integer passedOverCount = 0;

    @Column(name = "ACTIVATED", nullable = false)
    private boolean activated = false;

    @Column(name = "ACTIVATION_DATE")
    private LocalDateTime activationDate;

    @Column(name = "SELECTED", nullable = false)
    private boolean selected = false;

    @Column(name = "COMPLETED", nullable = false)
    private boolean completed = false;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "CONTAINER_TYPE")
    private String containerType;

    @ManyToOne
    @JoinColumn(name = "BOXTYPE_ID")
    private BoxType boxType;

    @OrderBy("positionNo")
    @OneToMany(mappedBy = "customerShipment", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<CustomerShipmentPosition> positions = new ArrayList<>();

    public void addPosition(CustomerShipmentPosition position) {
        getPositions().add(position);
        position.setCustomerShipment(this);
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

    public LocalDateTime getExsdTime() {
        return exsdTime;
    }

    public void setExsdTime(LocalDateTime exsdTime) {
        this.exsdTime = exsdTime;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public BoxType getBoxType() {
        return boxType;
    }

    public void setBoxType(BoxType boxType) {
        this.boxType = boxType;
    }

    public Integer getPassedOverCount() {
        return passedOverCount;
    }

    public void setPassedOverCount(Integer passedOverCount) {
        this.passedOverCount = passedOverCount;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public LocalDateTime getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(LocalDateTime activationDate) {
        this.activationDate = activationDate;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
