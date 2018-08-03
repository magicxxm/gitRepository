package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.RobotType;

import javax.validation.constraints.NotNull;

public class RobotTypeDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    @NotNull
    private String type;


    public RobotTypeDTO() {
    }

    public RobotTypeDTO(RobotType entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
