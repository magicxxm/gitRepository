package com.mushiny.wms.masterdata.ibbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveCategoryRule;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ReceiveCategoryRuleDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    @NotNull
    private List<String> operators = new ArrayList<>();

    @NotNull
    private String decisionKey;

    @NotNull
    private String comparisonType;

    private String compKey;

    private List<?> selectList = new ArrayList<>();

    public ReceiveCategoryRuleDTO() {
    }

    public ReceiveCategoryRuleDTO(ReceiveCategoryRule entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOperators() {
        return operators;
    }

    public void setOperators(List<String> operators) {
        this.operators = operators;
    }

    public String getDecisionKey() {
        return decisionKey;
    }

    public void setDecisionKey(String decisionKey) {
        this.decisionKey = decisionKey;
    }

    public String getComparisonType() {
        return comparisonType;
    }

    public void setComparisonType(String comparisonType) {
        this.comparisonType = comparisonType;
    }

    public String getCompKey() {
        return compKey;
    }

    public void setCompKey(String compKey) {
        this.compKey = compKey;
    }

    public List<?> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<?> selectList) {
        this.selectList = selectList;
    }
}
