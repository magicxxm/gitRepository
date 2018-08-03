package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.mdbasics.domain.StorageLocation;
import com.mushiny.wms.masterdata.obbasics.constants.State;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "OB_PICKINGORDER")
public class PickingOrder extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "PICKINGORDER_NO", nullable = false, unique = true)
    private String number;

    @Column(name = "CUSTOMERSHIPMENT_NO", nullable = true)
    private String customerShipmentNumber;

    @Column(name = "STATE", nullable = false)
    private int state = State.RAW;

    @Column(name = "PRIORITY", nullable = false)
    private int priority = 50;

    @ManyToOne
    @JoinColumn(name = "OPERATOR_ID")
    private User operator;

    @Column(name = "MANUAL_CREATION", nullable = false)
    private boolean manualCreation = false;

    @OneToMany(mappedBy = "pickingOrder")
    @OrderBy("id")
    private List<PickingPosition> positions;

//    @OneToMany(mappedBy = "pickingOrder")
//    @OrderBy("id")
//    private List<PickingUnitLoad> unitLoads;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DESTINATION_ID")
    private StorageLocation destination;

    @ManyToOne(optional = true)
    @JoinColumn(name = "STRATEGY_ID")
    private OrderStrategy strategy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PROCESSPATH_ID")
    private ProcessPath processPath;

//    @ManyToOne(optional = true)
//    @JoinColumn(name = "PICKSTARTBAY_ID")
//    private Bay pickStartBay;
//
//    @ManyToOne(optional = true)
//    @JoinColumn(name = "PICKSTARTLOCATION_ID")
//    private StorageLocation pickStartLocation;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCustomerShipmentNumber() {
        return customerShipmentNumber;
    }

    public void setCustomerShipmentNumber(String customerShipmentNumber) {
        this.customerShipmentNumber = customerShipmentNumber;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public boolean isManualCreation() {
        return manualCreation;
    }

    public void setManualCreation(boolean manualCreation) {
        this.manualCreation = manualCreation;
    }

    public List<PickingPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<PickingPosition> positions) {
        this.positions = positions;
    }

//    public List<PickingUnitLoad> getUnitLoads() {
//        return unitLoads;
//    }
//
//    public void setUnitLoads(List<PickingUnitLoad> unitLoads) {
//        this.unitLoads = unitLoads;
//    }

    public StorageLocation getDestination() {
        return destination;
    }

    public void setDestination(StorageLocation destination) {
        this.destination = destination;
    }

    public OrderStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(OrderStrategy strategy) {
        this.strategy = strategy;
    }

    public ProcessPath getProcessPath() {
        return processPath;
    }

    public void setProcessPath(ProcessPath processPath) {
        this.processPath = processPath;
    }

//    public Bay getPickStartBay() {
//        return pickStartBay;
//    }

//    public void setPickStartBay(Bay pickStartBay) {
//        this.pickStartBay = pickStartBay;
//    }

//    public StorageLocation getPickStartLocation() {
//        return pickStartLocation;
//    }

//    public void setPickStartLocation(StorageLocation pickStartLocation) {
//        this.pickStartLocation = pickStartLocation;
//    }

//    public void addAssignedShipments(CustomerShipment shipment) {
//        if (!getAssignedShipments().contains(shipment)){
//            getAssignedShipments().add(shipment);
//        }
//    }
//
//    public void removeAssignedShipments(CustomerShipment shipment) throws IllegalArgumentException{
//        if (getAssignedShipments().contains(shipment)){
//            for (PickingPosition p : getPositions()){
//                if (shipment.getPositions().contains(p.getCustomerShipmentPosition())){
//                    throw new IllegalArgumentException();
//                }
//            }
//            getAssignedShipments().remove(shipment);
//        }
//    }
//
//    public boolean containsAssignedShipments(CustomerShipment shipment) {
//        return getAssignedShipments().contains(shipment);
//    }

    @Override
    public String toUniqueString() {
        return number;
    }
}
