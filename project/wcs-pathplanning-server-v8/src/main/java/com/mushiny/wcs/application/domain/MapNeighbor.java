package com.mushiny.wcs.application.domain;

import com.mushiny.wcs.common.entity.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@Table(name = "WD_NEIGHBOR")
public class MapNeighbor extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "IN_ID")
    private MapNode inNode;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "OUT_ID")
    private MapNode outNode;

    @Column(name = "CARRYINGCOST")
    private Integer carryingCost;

    @Column(name = "COST")
    private Integer cost;

    @Column(name = "NEW_COST")
    private Integer newCost;

    @Column(name = "COSTTYPE")
    private String costType;

    @Column(name = "BLOCKED")
    private boolean blocked;

    @Column(name = "MAP_ID")
    private String mapId;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    public void setCarryingCost(Integer carryingCost) {
        this.carryingCost = carryingCost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getNewCost() {
        return newCost;
    }

    public void setNewCost(Integer newCost) {
        this.newCost = newCost;
    }

    public int getCarryingCost() {
        return carryingCost;
    }

    public void setCarryingCost(int carryingCost) {
        this.carryingCost = carryingCost;
    }

    public MapNode getInNode() {
        return inNode;
    }

    public void setInNode(MapNode inNode) {
        this.inNode = inNode;
    }

    public MapNode getOutNode() {
        return outNode;
    }

    public void setOutNode(MapNode outNode) {
        this.outNode = outNode;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
}
