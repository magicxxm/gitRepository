package com.mushiny.model;

import com.mushiny.common.entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "SYS_SYSTEMPROPERTY", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"WAREHOUSE_ID", "SYSTEM_KEY"})
})
public class SystemProperty extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "SYSTEM_KEY", nullable = false)
    private String key;

    @Column(name = "SYSTEM_VALUE", nullable = false)
    private String value;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "WAREHOUSE_ID")
    private String warehouseId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }
}
