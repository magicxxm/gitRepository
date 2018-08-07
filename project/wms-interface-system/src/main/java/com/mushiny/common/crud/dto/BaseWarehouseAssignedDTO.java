package com.mushiny.common.crud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mushiny.common.crud.dto.BaseDTO;
import com.mushiny.common.entity.BaseEntity;

import javax.validation.constraints.NotNull;

public class BaseWarehouseAssignedDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String warehouseId;

    private String warehouse;

    public BaseWarehouseAssignedDTO() {
    }

    public BaseWarehouseAssignedDTO(BaseEntity baseEntity) {
        super(baseEntity);
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }
}
