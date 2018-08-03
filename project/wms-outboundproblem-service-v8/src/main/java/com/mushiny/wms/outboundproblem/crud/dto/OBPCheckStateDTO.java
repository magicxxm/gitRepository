package com.mushiny.wms.outboundproblem.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.outboundproblem.crud.common.dto.InboundProblemRuleDTO;
import com.mushiny.wms.outboundproblem.crud.common.dto.StorageLocationDTO;
import com.mushiny.wms.outboundproblem.domain.OBPCheckState;
import com.mushiny.wms.outboundproblem.domain.common.Client;
import com.mushiny.wms.outboundproblem.domain.common.ItemData;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OBPCheckStateDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private BigDecimal amount;

    private BigDecimal amountProblem;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String inboundProblemRuleId;

    private InboundProblemRuleDTO inboundProblemRule;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String storageLocationId;

    private StorageLocationDTO storageLocation;

    private OBProblemCheckDTO obProblem;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String problemId;

    private String solveBy;

    private LocalDateTime solveDate;

    private String cell;

    private String problemType;

    private String reportBy;

    private String jobType;

    private LocalDateTime reportDate;

    private LocalDateTime obpsCreateDate;

    private ItemData itemData;

    @NotNull
    private String state;

    private String problemStorageLocation;

    private String clientId;

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

    public OBProblemCheckDTO getObProblem() {
        return obProblem;
    }

    public void setObProblem(OBProblemCheckDTO obProblem) {
        this.obProblem = obProblem;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public BigDecimal getAmountProblem() {
        return amountProblem;
    }

    public void setAmountProblem(BigDecimal amountProblem) {
        this.amountProblem = amountProblem;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }



    public InboundProblemRuleDTO getInboundProblemRule() {
        return inboundProblemRule;
    }

    public void setInboundProblemRule(InboundProblemRuleDTO inboundProblemRule) {
        this.inboundProblemRule = inboundProblemRule;
    }


    public StorageLocationDTO getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocationDTO storageLocation) {
        this.storageLocation = storageLocation;
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


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public OBPCheckStateDTO() {
    }

    public String getProblemStorageLocation() {
        return problemStorageLocation;
    }

    public void setProblemStorageLocation(String problemStorageLocation) {
        this.problemStorageLocation = problemStorageLocation;
    }

    public OBPCheckStateDTO(OBPCheckState entity) {
        super(entity);
    }


    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getReportBy() {
        return reportBy;
    }

    public void setReportBy(String reportBy) {
        this.reportBy = reportBy;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    public LocalDateTime getObpsCreateDate() {
        return obpsCreateDate;
    }

    public void setObpsCreateDate(LocalDateTime obpsCreateDate) {
        this.obpsCreateDate = obpsCreateDate;
    }

    public String getInboundProblemRuleId() {
        return inboundProblemRuleId;
    }

    public String getStorageLocationId() {
        return storageLocationId;
    }

    public void setInboundProblemRuleId(String inboundProblemRuleId) {
        this.inboundProblemRuleId = inboundProblemRuleId;
    }

    public void setStorageLocationId(String storageLocationId) {
        this.storageLocationId = storageLocationId;
    }
}
