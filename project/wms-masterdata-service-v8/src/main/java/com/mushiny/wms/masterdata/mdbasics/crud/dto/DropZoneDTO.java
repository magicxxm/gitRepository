package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.DropZone;

import javax.validation.constraints.NotNull;

public class DropZoneDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    private Integer standardTime;

    public DropZoneDTO() {
    }

    public DropZoneDTO(DropZone entity) {
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

    public Integer getStandardTime() {
        return standardTime;
    }

    public void setStandardTime(Integer standardTime) {
        this.standardTime = standardTime;
    }
}
