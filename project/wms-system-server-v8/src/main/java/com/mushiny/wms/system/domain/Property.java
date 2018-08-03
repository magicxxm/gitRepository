package com.mushiny.wms.system.domain;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;

@Entity
@Table(name = "SYS_SYSTEMPROPERTY")
public class Property extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "SYSTEM_KEY")
    private String systemKey;
    @Column(name = "SYSTEM_VALUE")
    private String systemValue;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
