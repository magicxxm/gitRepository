package com.mushiny.wms.outboundproblem.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.outboundproblem.domain.common.CustomerShipment;
import com.mushiny.wms.outboundproblem.domain.common.InboundProblemRule;
import com.mushiny.wms.outboundproblem.domain.common.ItemData;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocation;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "OBP_OBPSOLVE")
public class OBPSolve extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "AMOUNT")
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "AMOUNT_PROBLEM")
    private BigDecimal amountProblem = BigDecimal.ZERO;

    @Column(name = "AMOUNT_SHIPMENT")
    private BigDecimal amountShipment = BigDecimal.ZERO;

    @Column(name = "AMOUNT_SCANED")
    private BigDecimal amountScaned = BigDecimal.ZERO;

    @Column(name = "AMOUNT_SCANED_PROBLEM")
    private BigDecimal amountScanedProblem = BigDecimal.ZERO;

    @Column(name = "STATE")
    private String state;

    @ManyToOne
    @JoinColumn(name = "OPERATE_RULE")
    private InboundProblemRule inboundProblemRule;

    @Column(name = "SOLVE_BY")
    private String solveBy;

    @Column(name = "SOLVE_DATE")
    private LocalDateTime solveDate;

    @Column(name = "SCANED")
    private String scaned;

    @OneToOne
    @JoinColumn(name = "PROBLEM_ID")
    private OBProblem obproblem;

//    @Column(name = "PROBLEM_ID")
//    private String problemId;

    @ManyToOne
    @JoinColumn(name = "STORAGELOCATION_ID")
    private StorageLocation storageLocation;

    @ManyToOne
    @JoinColumn(name = "STATION_ID")
    private OBPStation obpStation;

    @ManyToOne
    @JoinColumn(name = "WALL_ID")
    private OBPWall obpWall;

    @ManyToOne
    @JoinColumn(name = "CELL_ID")
    private OBPCell obpCell;

    @ManyToOne
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    @ManyToOne
    @JoinColumn(name = "SHIPMENT_ID")
    private CustomerShipment customerShipment;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getScaned() {
        return scaned;
    }

    public void setScaned(String scaned) {
        this.scaned = scaned;
    }

    public OBProblem getObproblem() {
        return obproblem;
    }

    public void setObproblem(OBProblem obproblem) {
        this.obproblem = obproblem;
    }

    public OBPStation getObpStation() {
        return obpStation;
    }

    public void setObpStation(OBPStation obpStation) {
        this.obpStation = obpStation;
    }

    public OBPWall getObpWall() {
        return obpWall;
    }

    public void setObpWall(OBPWall obpWall) {
        this.obpWall = obpWall;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountProblem() {
        return amountProblem;
    }

    public void setAmountProblem(BigDecimal amountProblem) {
        this.amountProblem = amountProblem;
    }

    public BigDecimal getAmountShipment() {
        return amountShipment;
    }

    public void setAmountShipment(BigDecimal amountShipment) {
        this.amountShipment = amountShipment;
    }

    public BigDecimal getAmountScaned() {
        return amountScaned;
    }

    public void setAmountScaned(BigDecimal amountScaned) {
        this.amountScaned = amountScaned;
    }

    public CustomerShipment getCustomerShipment() {

        return customerShipment;
    }

    public void setCustomerShipment(CustomerShipment customerShipment) {
        this.customerShipment = customerShipment;
    }

    public OBPCell getObpCell() {
        return obpCell;
    }

    public void setObpCell(OBPCell obpCell) {
        this.obpCell = obpCell;
    }

    public BigDecimal getAmountScanedProblem() {
        return amountScanedProblem;
    }

    public void setAmountScanedProblem(BigDecimal amountScanedProblem) {
        this.amountScanedProblem = amountScanedProblem;
    }

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public InboundProblemRule getInboundProblemRule() {
        return inboundProblemRule;
    }

    public void setInboundProblemRule(InboundProblemRule inboundProblemRule) {
        this.inboundProblemRule = inboundProblemRule;
    }

//    public String getProblemId() {
//        return problemId;
//    }
//
//    public void setProblemId(String problemId) {
//        this.problemId = problemId;
//    }
}
