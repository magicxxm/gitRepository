package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPathType;

import javax.validation.constraints.NotNull;

public class ProcessPathTypeDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;
    @NotNull
    private String pickFlow;
    @NotNull
    private String pickWay;

    public ProcessPathTypeDTO() {
    }

    public ProcessPathTypeDTO(ProcessPathType entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPickFlow() {
        return pickFlow;
    }

    public void setPickFlow(String pickFlow) {
        this.pickFlow = pickFlow;
    }

    public String getPickWay() {
        return pickWay;
    }

    public void setPickWay(String pickWay) {
        this.pickWay = pickWay;
    }
}
