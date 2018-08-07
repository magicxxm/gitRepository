package com.mushiny.common.entity;

import com.mushiny.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseWarehouseAssignedEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
}
