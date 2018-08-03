package com.mushiny.wms.system.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.system.domain.Client;
import com.mushiny.wms.system.domain.Property;

import javax.validation.constraints.NotNull;

public class PropertyDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String systemKey;
    @NotNull
    private String systemValue;

    private String description;

    public PropertyDTO() {
    }

    public PropertyDTO(Property entity) {
        super(entity);
    }

    public String getSystemKey() {
        return systemKey;
    }

    public void setSystemKey(String systemKey) {
        this.systemKey = systemKey;
    }

    public String getSystemValue() {
        return systemValue;
    }

    public void setSystemValue(String systemValue) {
        this.systemValue = systemValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
