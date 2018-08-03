package com.mushiny.wms.outboundproblem.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBProblemDTO;
import com.mushiny.wms.outboundproblem.domain.OBPSolveCheck;
import com.mushiny.wms.outboundproblem.crud.common.dto.StorageLocationDTO;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OBPSolveCheckDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String problemId;

    private OBProblemCheckDTO obProblem;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String storageLocationId;

    private StorageLocationDTO storageLocation;

    @NotNull
    private BigDecimal amount = BigDecimal.ZERO;

    private BigDecimal problemAmount;

    private BigDecimal itemDataAmount;

    private BigDecimal storageLocationAmount;

    @NotNull
    private String state;

    private String checkBy;

    private LocalDateTime checkDate;

    public OBPSolveCheckDTO() {
    }

    public OBPSolveCheckDTO(OBPSolveCheck entity) {
        super(entity);
    }

    public String getStorageLocationId() {
        return storageLocationId;
    }

    public void setStorageLocationId(String storageLocationId) {
        this.storageLocationId = storageLocationId;
    }

    public StorageLocationDTO getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(StorageLocationDTO storageLocation) {
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

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }


    public OBProblemCheckDTO getObProblem() {
        return obProblem;
    }

    public void setObProblem(OBProblemCheckDTO obProblem) {
        this.obProblem = obProblem;
    }
}
