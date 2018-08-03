package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveCategoryPosition;

import javax.validation.constraints.NotNull;

public class ReceiveCategoryPositionDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private int positionNo;

    @NotNull
    private String operator;

    private String compKey;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String categoryId;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ruleId;

    private ReceiveCategoryDTO receiveCategory;

    private ReceiveCategoryRuleDTO receiveCategoryRule;

    public ReceiveCategoryPositionDTO() {
    }

    public ReceiveCategoryPositionDTO(ReceiveCategoryPosition entity) {
        super(entity);
    }

    public int getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(int positionNo) {
        this.positionNo = positionNo;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCompKey() {
        return compKey;
    }

    public void setCompKey(String compKey) {
        this.compKey = compKey;
    }

    public ReceiveCategoryDTO getReceiveCategory() {
        return receiveCategory;
    }

    public void setReceiveCategory(ReceiveCategoryDTO receiveCategory) {
        this.receiveCategory = receiveCategory;
    }

    public ReceiveCategoryRuleDTO getReceiveCategoryRule() {
        return receiveCategoryRule;
    }

    public void setReceiveCategoryRule(ReceiveCategoryRuleDTO receiveCategoryRule) {
        this.receiveCategoryRule = receiveCategoryRule;
    }
}
