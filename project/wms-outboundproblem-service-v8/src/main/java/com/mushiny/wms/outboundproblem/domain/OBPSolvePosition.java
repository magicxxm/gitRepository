package com.mushiny.wms.outboundproblem.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.outboundproblem.domain.common.CustomerShipment;
import com.mushiny.wms.outboundproblem.domain.common.ItemData;
import com.mushiny.wms.outboundproblem.domain.common.Resource;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "OBP_OBPSOLVEPOSITION")
public class OBPSolvePosition extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "SOLVE_BY")
    private String solveBy;

    @Column(name = "SOLVE_DATE")
    private LocalDateTime solveDate;

    @Column(name = "STATE")
    private String state;

    @Column(name = "ITEMDATA")
    private String itemDataNo;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "LOCATION_CONTAINER")
    private String locationContainer;

    @Column(name = "AMOUNT_SCANED")
    private BigDecimal amountScaned = BigDecimal.ZERO;

    @Column(name = "SHIPMENT_NO")
    private String shipmentNo;

    @Column(name = "SOLVE_KEY")
    private String solveKey;

    @ManyToOne(optional = false)
    @JoinColumn(name = "SOLVE_ID")
    private OBPSolve obpSolve;

    public String getSolveBy() {
        return solveBy;
    }

    public void setSolveBy(String solveBy) {
        this.solveBy = solveBy;
    }

    public LocalDateTime getSolveDate() {
        return solveDate;
    }

    public void setSolveDate(LocalDateTime solveDate) {
        this.solveDate = solveDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getItemDataNo() {
        return itemDataNo;
    }

    public void setItemDataNo(String itemDataNo) {
        this.itemDataNo = itemDataNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationContainer() {
        return locationContainer;
    }

    public void setLocationContainer(String locationContainer) {
        this.locationContainer = locationContainer;
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    public OBPSolve getObpSolve() {
        return obpSolve;
    }

    public void setObpSolve(OBPSolve obpSolve) {
        this.obpSolve = obpSolve;
    }

    public String getSolveKey() {
        return solveKey;
    }

    public void setSolveKey(String solveKey) {
        this.solveKey = solveKey;
    }

    public BigDecimal getAmountScaned() {
        return amountScaned;
    }

    public void setAmountScaned(BigDecimal amountScaned) {
        this.amountScaned = amountScaned;
    }
}
