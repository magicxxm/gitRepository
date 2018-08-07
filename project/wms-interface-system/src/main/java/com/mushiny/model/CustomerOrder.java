package com.mushiny.model;


import com.mushiny.common.entity.BaseClientAssignedEntity;
import com.mushiny.constants.State;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/5/2.
 */
@Entity
@Table(name = "OB_CUSTOMERORDER")
public class CustomerOrder extends BaseClientAssignedEntity {

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_NO")
    private String customerNo;

    @Column(name = "DELIVERY_DATE")
    private LocalDateTime deliveryDate;

    @Column(name = "SORT_CODE")
    private String sortCode;

    @Column(name = "ORDER_NO")
    private String orderNo;

    @Column(name = "PRIORITY")
    private int priority = State.RAW;

    @Column(name = "STATE")
    private int state;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "CONTAINER_TYPE")
    private String containerType;

    @ManyToOne
    @JoinColumn(name = "BOXTYPE_ID")
    private BoxType boxType;

    @Column(name = "EXSD_TIME")
    private LocalDateTime exsdTime;

    @OrderBy("positionNo")
    @OneToMany(mappedBy = "customerOrder", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<CustomerOrderPosition> positions = new ArrayList<>();

    public void addPosition(CustomerOrderPosition position) {
        getPositions().add(position);
        position.setCustomerOrder(this);
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getExsdTime() {
        return exsdTime;
    }

    public void setExsdTime(LocalDateTime exsdTime) {
        this.exsdTime = exsdTime;
    }

    public List<CustomerOrderPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<CustomerOrderPosition> positions) {
        this.positions = positions;
    }
 
}
