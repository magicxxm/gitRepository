package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.masterdata.obbasics.common.exception.VersionException;
import com.mushiny.wms.masterdata.obbasics.constants.PickingOperator;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryPosition;

public class PickingCateGoryPositionDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private PickingCateGoryRuleDTO rule;

    private String ruleId;

    private PickingOperator operator;

    private String value;

    private String categoryId;

    public PickingCateGoryPositionDTO() {
    }

    public PickingCateGoryPositionDTO(PickingCateGoryPosition pickingCateGoryPosition) {
        super(pickingCateGoryPosition);
    }

    public PickingCateGoryRuleDTO getRule() {
        return rule;
    }

    public void setRule(PickingCateGoryRuleDTO rule) {
        this.rule = rule;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public PickingOperator getOperator() {
        return operator;
    }

    public void setOperator(PickingOperator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void merge(PickingCateGoryPosition pickingCateGoryPosition) throws VersionException {
        super.merge(pickingCateGoryPosition);
    }
}
