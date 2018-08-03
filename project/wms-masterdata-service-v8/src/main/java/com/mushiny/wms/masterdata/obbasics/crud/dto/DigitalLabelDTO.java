package com.mushiny.wms.masterdata.obbasics.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.obbasics.domain.DigitalLabel;

import javax.validation.constraints.NotNull;

public class DigitalLabelDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private int numOrder=0;
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String labelControllerId;

    private LabelControllerDTO labelController;


    public DigitalLabelDTO() {
    }

    public DigitalLabelDTO(DigitalLabel entity) {
        super(entity);
    }

    public int getNumOrder() {
        return numOrder;
    }

    public void setNumOrder(int numOrder) {
        this.numOrder = numOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabelControllerId() {
        return labelControllerId;
    }

    public void setLabelControllerId(String labelControllerId) {
        this.labelControllerId = labelControllerId;
    }

    public LabelControllerDTO getLabelController() {
        return labelController;
    }

    public void setLabelController(LabelControllerDTO labelController) {
        this.labelController = labelController;
    }
}
