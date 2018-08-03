package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.masterdata.obbasics.common.exception.VersionException;
import com.mushiny.wms.masterdata.obbasics.constants.PickingOperator;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryPosition;

public class PickingCateGoryRulePositionDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private PickingOperator operator;

    public PickingCateGoryRulePositionDTO() {
    }

    public PickingCateGoryRulePositionDTO(PickingCateGoryPosition pickingCateGoryPosition) {
        super(pickingCateGoryPosition);
    }

    public PickingOperator getOperator() {
        return operator;
    }

    public void setOperator(PickingOperator operator) {
        this.operator = operator;
    }

    public void merge(PickingCateGoryPosition pickingCateGoryPosition) throws VersionException {
        super.merge(pickingCateGoryPosition);
    }
}
