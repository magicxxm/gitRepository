package com.mushiny.wms.outboundproblem.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocation;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "OBP_OBPSOLVECHECK")
public class OBPSolveCheck extends BaseClientAssignedEntity  {
    private static final long serialVersionUID = 1L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PROBLEMCHECK_ID")
    private OBProblemCheck obProblem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "STORAGELOCATION_ID")
    private StorageLocation storageLocation;

    @Column(name = "LOCATION_AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "LOCATION_PROBLEM_AMOUNT")
    private BigDecimal problemAmount;

    @Column(name = "LOCATION_ITEMDATA_AMOUNT")
    private BigDecimal itemDataAmount;

    @Column(name = "STORAGELOCATION_AMOUNT")
    private BigDecimal storageLocationAmount;

    @Column(name = "STATE", nullable = false)
    private String state;

    @Column(name = "CHECK_BY")
    private String checkBy;

    @Column(name = "CHECK_DATE")
    private LocalDateTime checkDate;

    public StorageLocation getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getProblemAmount() {
        return problemAmount;
    }

    public void setProblemAmount(BigDecimal problemAmount) {
        this.problemAmount = problemAmount;
    }

    public BigDecimal getItemDataAmount() {
        return itemDataAmount;
    }

    public void setItemDataAmount(BigDecimal itemDataAmount) {
        this.itemDataAmount = itemDataAmount;
    }

    public BigDecimal getStorageLocationAmount() {
        return storageLocationAmount;
    }

    public void setStorageLocationAmount(BigDecimal storageLocationAmount) {
        this.storageLocationAmount = storageLocationAmount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCheckBy() {
        return checkBy;
    }

    public void setCheckBy(String checkBy) {
        this.checkBy = checkBy;
    }

    public LocalDateTime getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(LocalDateTime checkDate) {
        this.checkDate = checkDate;
    }

    public OBProblemCheck getObProblem() {
        return obProblem;
    }

    public void setObProblem(OBProblemCheck obProblem) {
        this.obProblem = obProblem;
    }
}
