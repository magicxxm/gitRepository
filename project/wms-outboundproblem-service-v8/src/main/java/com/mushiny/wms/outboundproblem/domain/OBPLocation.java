package com.mushiny.wms.outboundproblem.domain;


import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="OBP_OBPLOCATION")
public class OBPLocation extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "AMOUNT")
    private int amount = 0;

    @Column(name = "AMOUNT_SCANED")
    private int amountScaned = 0;

    @Column(name = "LOCATION")
    private String location ;

    @Column(name = "ITEMDATA")
    private String itemNo;

    @Column(name = "SOLVE_ID")
    private String solveId;

    @Column(name = "SOLVEPOSITION_ID")
    private String solvePositionId;

    @Column(name = "STATE")
    private String state;

    @Column(name = "SHIPMENT_NO")
    private String shipmentNo;

    @Column(name="SOLVE_BY")
    private String solveBy;

    @Column(name="SOLVE_DATE")
    private LocalDateTime solveDate;

    @Column(name="CELLNAME")
    private String cellName;

    @Column(name = "ISCALLPOD")
    private boolean isCallPod;

    @Column(name = "ITEMNAME")
    private String itemName;

    public OBPLocation() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmountScaned() {
        return amountScaned;
    }

    public void setAmountScaned(int amountScaned) {
        this.amountScaned = amountScaned;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getSolveId() {
        return solveId;
    }

    public void setSolveId(String solveId) {
        this.solveId = solveId;
    }

    public String getSolvePositionId() {
        return solvePositionId;
    }

    public void setSolvePositionId(String solvePositionId) {
        this.solvePositionId = solvePositionId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

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

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public boolean isCallPod() {
        return isCallPod;
    }

    public void setCallPod(boolean callPod) {
        isCallPod = callPod;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}
