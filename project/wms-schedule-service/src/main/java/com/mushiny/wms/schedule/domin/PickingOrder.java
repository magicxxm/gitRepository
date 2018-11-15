package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "OB_PICKINGORDER")
public class PickingOrder extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "PICKINGORDER_NO", nullable = false)
    private String pickingOrderNo;

    @Column(name = "CUSTOMERSHIPMENT_NO")
    private String customerShipmentNo;

    @Column(name = "MANUAL_CREATION", nullable = false)
    private boolean manualCreation;

    @Column(name = "PRIORITY", nullable = false)
    private int priority;

    @Column(name = "STATE", nullable = false)
    private int state;

    @ManyToOne
    @JoinColumn(name = "DESTINATION_ID")
    @NotFound(action = NotFoundAction.IGNORE)
    private StorageLocation storageLocation;

    @ManyToOne
    @JoinColumn(name = "OPERATOR_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "STRATEGY_ID")
    private OrderStrategy orderStrategy;

    @ManyToOne
    @JoinColumn(name = "PROCESSPATH_ID")
    private ProcessPath processPath;

    @OneToMany(mappedBy = "pickingOrder")
    private List<PickingOrderPosition> pickingOrderPositions;

    @OneToMany(mappedBy = "pickingOrder")
    private List<PickingUnitLoad> pickingUnitLoads;

    @Column(name="COMPLETED")
    private boolean completed;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPickingOrderNo() {
        return pickingOrderNo;
    }

    public void setPickingOrderNo(String pickingOrderNo) {
        this.pickingOrderNo = pickingOrderNo;
    }

    public String getCustomerShipmentNo() {
        return customerShipmentNo;
    }

    public void setCustomerShipmentNo(String customerShipmentNo) {
        this.customerShipmentNo = customerShipmentNo;
    }

    public boolean isManualCreation() {
        return manualCreation;
    }

    public void setManualCreation(boolean manualCreation) {
        this.manualCreation = manualCreation;
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

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderStrategy getOrderStrategy() {
        return orderStrategy;
    }

    public void setOrderStrategy(OrderStrategy orderStrategy) {
        this.orderStrategy = orderStrategy;
    }

    public ProcessPath getProcessPath() {
        return processPath;
    }

    public void setProcessPath(ProcessPath processPath) {
        this.processPath = processPath;
    }

    public List<PickingOrderPosition> getPickingOrderPositions() {
        return pickingOrderPositions;
    }

    public void setPickingOrderPositions(List<PickingOrderPosition> pickingOrderPositions) {
        this.pickingOrderPositions = pickingOrderPositions;
    }

    public List<PickingUnitLoad> getPickingUnitLoads() {
        return pickingUnitLoads;
    }

    public void setPickingUnitLoads(List<PickingUnitLoad> pickingUnitLoads) {
        this.pickingUnitLoads = pickingUnitLoads;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
