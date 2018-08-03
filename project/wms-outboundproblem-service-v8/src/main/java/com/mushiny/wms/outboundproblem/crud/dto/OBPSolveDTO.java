package com.mushiny.wms.outboundproblem.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.outboundproblem.crud.common.dto.InboundProblemRuleDTO;
import com.mushiny.wms.outboundproblem.crud.common.dto.StorageLocationDTO;
import com.mushiny.wms.outboundproblem.domain.OBPSolve;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OBPSolveDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    private String problemId;

    private OBProblemDTO obProblem;

    private BigDecimal amount;

    private BigDecimal amountProblem;

    private String inboundProblemRuleId;

    private InboundProblemRuleDTO inboundProblemRule;

    private String storageLocationId;

    private StorageLocationDTO storageLocation;

    private String solveBy;

    private LocalDateTime solveDate;

    private String shipmentNo;

    private String cell;

    private String problemType;

    private String reportBy;

    private String jobType;

    private LocalDateTime reportDate;

    private LocalDateTime obpsCreateDate;

    private LocalDateTime exSD;

    private String timeCondition;

    private String serialRecordType;

    private String state;

    private String shipmentState;

    private String scaned;

    private String problemStorageLocation;

    public BigDecimal getAmountProblem() {
        return amountProblem;
    }

    public void setAmountProblem(BigDecimal amountProblem) {
        this.amountProblem = amountProblem;
    }

    public String getShipmentState() {
        return shipmentState;
    }

    public void setShipmentState(String shipmentState) {
        this.shipmentState = shipmentState;
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public OBProblemDTO getObProblem() {
        return obProblem;
    }

    public void setObProblem(OBProblemDTO obProblem) {
        this.obProblem = obProblem;
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

    public String getScaned() {
        return scaned;
    }

    public void setScaned(String scaned) {
        this.scaned = scaned;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public OBPSolveDTO() {
    }

    public String getProblemStorageLocation() {
        return problemStorageLocation;
    }

    public void setProblemStorageLocation(String problemStorageLocation) {
        this.problemStorageLocation = problemStorageLocation;
    }

    public OBPSolveDTO(OBPSolve entity) {
        super(entity);
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
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

    public LocalDateTime getExSD() {
        return exSD;
    }

    public void setExSD(LocalDateTime exSD) {
        this.exSD = exSD;
    }

    public String getTimeCondition() {
        return timeCondition;
    }

    public void setTimeCondition(String timeCondition) {
        this.timeCondition = timeCondition;
    }

    public String getSerialRecordType() {
        return serialRecordType;
    }

    public void setSerialRecordType(String serialRecordType) {
        this.serialRecordType = serialRecordType;
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
