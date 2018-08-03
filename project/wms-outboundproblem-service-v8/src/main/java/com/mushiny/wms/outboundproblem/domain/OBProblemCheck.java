package com.mushiny.wms.outboundproblem.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.outboundproblem.domain.common.InboundProblemRule;
import com.mushiny.wms.outboundproblem.domain.common.ItemData;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "OBP_OBPROBLEM_CHECK")
public class OBProblemCheck extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "PROBLEM_TYPE")
    private String problemType;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "RULE")
    private InboundProblemRule inboundProblemRule;

    @Column(name = "REPORT_BY")
    private String reportBy;

    @Column(name = "SOLVED_AMOUNT")
    private BigDecimal solveAmount = BigDecimal.ZERO;

    @Column(name = "REPORT_DATE")
    private LocalDateTime reportDate = LocalDateTime.now();

    @Column(name = "SOLVED_BY")
    private String solvedBy;

    @Column(name = "JOB_TYPE")
    private String jobType;


    @Column(name = "SKU_NO")
    private String skuNo;

    @Column(name = "STATE")
    private String state;


    @Column(name = "LOT_NO")
    private String lotNo;

    @Column(name = "ITEM_NO")
    private String itemNo;

    @Column(name = "PROBLEM_STORAGELOCATION")
    private String problemStoragelocation;


    @Column(name = "AMOUNT")
    private BigDecimal amount = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }


    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReportBy() {
        return reportBy;
    }

    public void setReportBy(String reportBy) {
        this.reportBy = reportBy;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }


    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getProblemStoragelocation() {
        return problemStoragelocation;
    }

    public void setProblemStoragelocation(String problemStoragelocation) {
        this.problemStoragelocation = problemStoragelocation;
    }


    public String getSolvedBy() {
        return solvedBy;
    }

    public void setSolvedBy(String solvedBy) {
        this.solvedBy = solvedBy;
    }

    public BigDecimal getSolveAmount() {
        return solveAmount;
    }

    public void setSolveAmount(BigDecimal solveAmount) {
        this.solveAmount = solveAmount;
    }

    public InboundProblemRule getInboundProblemRule() {
        return inboundProblemRule;
    }

    public void setInboundProblemRule(InboundProblemRule inboundProblemRule) {
        this.inboundProblemRule = inboundProblemRule;
    }
}
