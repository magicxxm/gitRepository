package com.mushiny.wms.outboundproblem.business.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.outboundproblem.domain.OBPSolve;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SolveDamagedDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;


    private String itemNo;

    private String itemName;

    private BigDecimal amountScaned = BigDecimal.ZERO;

    private BigDecimal amount = BigDecimal.ZERO;

    private String scaned;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String problemType;

    private String resourceValue;

    private String state;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal amountProblem = BigDecimal.ZERO;

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getAmountScaned() {
        return amountScaned;
    }

    public void setAmountScaned(BigDecimal amountScaned) {
        this.amountScaned = amountScaned;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getScaned() {
        return scaned;
    }

    public void setScaned(String scaned) {
        this.scaned = scaned;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public BigDecimal getAmountProblem() {
        return amountProblem;
    }

    public void setAmountProblem(BigDecimal amountProblem) {
        this.amountProblem = amountProblem;
    }
}
