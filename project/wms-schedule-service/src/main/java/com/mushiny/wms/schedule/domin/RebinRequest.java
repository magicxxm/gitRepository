package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.schedule.common.RebinRequestState;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_REBINREQUEST")
public class RebinRequest extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "REBINREQUEST_NO", unique = true, nullable = false)
    private String number;

    @Column(name = "STATE", nullable = true)
    @Enumerated(EnumType.STRING)
    private RebinRequestState rebinState = RebinRequestState.RAW;

    @Column(name = "NUMBER_OF_REBINWALLS", nullable = false)
    private int numberOfRebinWalls = 0;

    @ManyToOne(optional = true)
    @JoinColumn(name = "REBINWALL1_ID")
    private RebinWall rebinWall1;

    @ManyToOne(optional = true)
    @JoinColumn(name = "REBINWALL2_ID")
    private RebinWall rebinWall2;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PICKINGORDER_ID")
    private PickingOrder pickingOrder;

    @ManyToOne(optional = false)
    @JoinColumn(name = "REBINWALLTYPE_ID")
    private RebinWallType rebinWallType;

    @ManyToOne
    @JoinColumn(name = "OPERATOR_ID")
    private User operator;

    @ManyToOne
    @JoinColumn(name = "REBINSTATION_ID")
    private RebinStation rebinStation;

    @OneToMany(mappedBy = "rebinRequest")
    private List<RebinRequestPosition> positions = new ArrayList<>();

    @OneToMany(mappedBy = "rebinRequest")
    private List<RebinCustomerShipment> shipments = new ArrayList<>();

    @OneToMany(mappedBy = "rebinRequest")
    @OrderBy("id")
    private List<RebinUnitLoad> unitLoads = new ArrayList<>();

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public RebinRequestState getRebinState() {
        return rebinState;
    }

    public void setRebinState(RebinRequestState rebinState) {
        this.rebinState = rebinState;
    }

    public PickingOrder getPickingOrder() {
        return pickingOrder;
    }

    public void setPickingOrder(PickingOrder pickingOrder) {
        this.pickingOrder = pickingOrder;
    }

    public RebinWallType getRebinWallType() {
        return rebinWallType;
    }

    public void setRebinWallType(RebinWallType rebinWallType) {
        this.rebinWallType = rebinWallType;
    }

    public int getNumberOfRebinWalls() {
        return numberOfRebinWalls;
    }

    public void setNumberOfRebinWalls(int numberOfRebinWalls) {
        this.numberOfRebinWalls = numberOfRebinWalls;
    }

    public RebinWall getRebinWall1() {
        return rebinWall1;
    }

    public void setRebinWall1(RebinWall rebinWall1) {
        this.rebinWall1 = rebinWall1;
    }

    public RebinWall getRebinWall2() {
        return rebinWall2;
    }

    public void setRebinWall2(RebinWall rebinWall2) {
        this.rebinWall2 = rebinWall2;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public RebinStation getRebinStation() {
        return rebinStation;
    }

    public void setRebinStation(RebinStation rebinStation) {
        this.rebinStation = rebinStation;
    }

    public List<RebinRequestPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<RebinRequestPosition> positions) {
        this.positions = positions;
    }

    public List<RebinCustomerShipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<RebinCustomerShipment> shipments) {
        this.shipments = shipments;
    }

    public List<RebinUnitLoad> getUnitLoads() {
        return unitLoads;
    }

    public void setUnitLoads(List<RebinUnitLoad> unitLoads) {
        this.unitLoads = unitLoads;
    }

}
