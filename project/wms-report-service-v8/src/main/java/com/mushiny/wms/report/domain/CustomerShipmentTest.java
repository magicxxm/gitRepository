package com.mushiny.wms.report.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "OB_CUSTOMERSHIPMENT")
public class CustomerShipmentTest extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_NO")
    private String customerNo;

    @Column(name = "DELIVERY_DATE")
    private Date deliveryDate;//传递时间

    @Column(name = "SORT_CODE")
    private String sortCode;

    @Column(name = "SHIPMENT_NO", nullable = false, unique = true)
    private String shipmentNo;

    @Column(name = "PRIORITY")
    private int priority;

    @Column(name = "STATE")
    private int state = 0;

    @Column(name = "PICK_MODE")
    private String pickMode;


    @Column(name = "PASSED_OVER_COUNT")
    private int passedOverCount = 0;//未选中的次数  not null

    @Column(name = "ACTIVATED")
    private boolean activated = false; //是否激活  not null

    @Column(name = "ACTIVATION_DATE")
    private LocalDateTime activationDate = LocalDateTime.now();//激活时间 not null

    @Column(name = "SELECTED")
    private boolean selected = false;//是否进入selected not null

    @Column(name = "COMPLETED")
    private boolean completed = false;//是否已全部分配拣货任务 not null


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

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
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


    public int getPassedOverCount() {
        return passedOverCount;
    }

    public void setPassedOverCount(int passedOverCount) {
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

//    public List<CustomerShipmentPosition> getPositions() {
//        return positions;
//    }
//
//    public void setPositions(List<CustomerShipmentPosition> positions) {
//        this.positions = positions;
//    }
}
