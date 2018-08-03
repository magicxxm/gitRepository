package com.mushiny.wms.outboundproblem.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.common.utils.ConversionUtil;
import com.mushiny.wms.outboundproblem.crud.common.dto.InboundProblemRuleDTO;
import com.mushiny.wms.outboundproblem.crud.common.dto.ItemDataDTO;
import com.mushiny.wms.outboundproblem.domain.OBProblemCheck;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OBProblemCheckDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;


    private String description;

    private String rule;

    private InboundProblemRuleDTO inboundProblemRule;

    private String problemType;

    private BigDecimal amount = BigDecimal.ZERO;

    private BigDecimal solveAmount = BigDecimal.ZERO;

    private String solvedBy;

    private String jobType;

    private String reportBy;

    private LocalDateTime reportDate;

    private String problemStoragelocation;

    private String state;

    private String lotNo;


    private String skuNo;

    private String itemNo;

    private String itemDataId;

    private ItemDataDTO itemData;


    public OBProblemCheckDTO() {

    }

    public OBProblemCheckDTO(OBProblemCheck entity) {
        super(entity);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
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

    public void setReportDate(String reportDate) {
        this.reportDate = ConversionUtil.toZonedDateTime(reportDate.replace("T"," ")).toLocalDateTime();
    }

    public String getProblemStoragelocation() {
        return problemStoragelocation;
    }

    public void setProblemStoragelocation(String problemStoragelocation) {
        this.problemStoragelocation = problemStoragelocation;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }


    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }


    public ItemDataDTO getItemData() {
        return itemData;
    }

    public void setItemData(ItemDataDTO itemData) {
        this.itemData = itemData;
    }

    public BigDecimal getSolveAmount() {
        return solveAmount;
    }

    public void setSolveAmount(BigDecimal solveAmount) {
        this.solveAmount = solveAmount;
    }

    public String getSolvedBy() {
        return solvedBy;
    }

    public void setSolvedBy(String solvedBy) {
        this.solvedBy = solvedBy;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public InboundProblemRuleDTO getInboundProblemRule() {
        return inboundProblemRule;
    }

    public void setInboundProblemRule(InboundProblemRuleDTO inboundProblemRule) {
        this.inboundProblemRule = inboundProblemRule;
    }
}
