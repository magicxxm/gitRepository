package com.mushiny.wms.outboundproblem.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.outboundproblem.domain.common.InboundProblemRule;
import com.mushiny.wms.outboundproblem.domain.common.ItemData;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocation;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "OBP_OBSOLVE_CHECK")
public class OBPCheckState extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "AMOUNT")
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "AMOUNT_PROBLEM")
    private BigDecimal amountProblem = BigDecimal.ZERO;


    @Column(name = "STATE")
    private String state;

    @ManyToOne
    @JoinColumn(name = "OPERATE_RULE")
    private InboundProblemRule inboundProblemRule;

    @Column(name = "SOLVE_BY")
    private String solveBy;

    @Column(name = "SOLVE_DATE")
    private LocalDateTime solveDate;


    @OneToOne
    @JoinColumn(name = "PROBLEMCHECK_ID")
    private OBProblemCheck obProblem;


    @ManyToOne
    @JoinColumn(name = "STORAGELOCATION_ID")
    private StorageLocation storageLocation;


    @ManyToOne
    @JoinColumn(name = "CELL_ID")
    private OBPCell obpCell;

    @ManyToOne
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;


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


    public OBPCell getObpCell() {
        return obpCell;
    }

    public void setObpCell(OBPCell obpCell) {
        this.obpCell = obpCell;
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

    public OBProblemCheck getObproblem() {
        return obProblem;
    }

    public void setObproblem(OBProblemCheck obproblem) {
        this.obProblem = obproblem;
    }
}
