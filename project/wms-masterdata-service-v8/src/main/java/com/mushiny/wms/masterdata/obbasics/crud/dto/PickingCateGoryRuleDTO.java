package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.masterdata.obbasics.common.exception.VersionException;
import com.mushiny.wms.masterdata.obbasics.constants.PickingComparisonType;
import com.mushiny.wms.masterdata.obbasics.constants.PickingOperator;
import com.mushiny.wms.masterdata.obbasics.constants.PickingRuleKey;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryRule;

import java.util.List;

public class PickingCateGoryRuleDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private PickingRuleKey key;

    private String name;

    private PickingComparisonType comparisonType;

    private List<PickingOperator> operators;

    private List<?> values;

    public PickingCateGoryRuleDTO() {
    }

    public PickingCateGoryRuleDTO(PickingCateGoryRule pickingCateGoryRule) {
        super(pickingCateGoryRule);
    }

    public PickingRuleKey getKey() {
        return key;
    }

    public void setKey(PickingRuleKey key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PickingComparisonType getComparisonType() {
        return comparisonType;
    }

    public void setComparisonType(PickingComparisonType comparisonType) {
        this.comparisonType = comparisonType;
    }

    public List<PickingOperator> getOperators() {
        return operators;
    }

    public void setOperators(List<PickingOperator> operators) {
        this.operators = operators;
    }

    public List<?> getValues() {
        return values;
    }

    public void setValues(List<?> values) {
        this.values = values;
    }

    public void merge(PickingCateGoryRule pickingCategory) throws VersionException {
        super.merge(pickingCategory);
    }
}
